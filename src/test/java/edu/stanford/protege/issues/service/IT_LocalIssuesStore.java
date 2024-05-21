package edu.stanford.protege.issues.service;

import edu.stanford.protege.github.issues.*;
import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.protege.github.GitHubUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-26
 */
@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@ExtendWith({MongoTestExtension.class})
public class IT_LocalIssuesStore {

    protected static final String NODE_ID = "abc123";

    @Autowired
    private LocalIssueStore issueStore;

    private IssueRecord issueRecord;

    private GitHubIssue issue;

    private OboId oboId;

    private Iri iri;

    private GitHubRepositoryCoordinates repoCoords;

    @BeforeEach
    void setUp() {
        repoCoords = GitHubRepositoryCoordinates.of("ACME", "R1");
        var now = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        issue = GitHubIssue.get("https://example.org/issues/1",
                                1,
                                NODE_ID,
                                1,
                                "My issue",
                                GitHubUser.empty(),
                                List.of(),
                                "https://example.org/issues",
                                GitHubState.OPEN,
                                false,
                                GitHubUser.empty(),
                                List.of(),
                                null,
                                0, now, now,
                                null,
                                null,
                                GitHubAuthorAssociation.COLLABORATOR,
                                "",
                                "The body",
                                GitHubReactions.empty(),
                                null);
        oboId = OboId.valueOf("ID:1234567");
        iri = Iri.valueOf("http://example.org/A");
        issueRecord = new IssueRecord(NODE_ID,
                                      GitHubRepositoryCoordinates.of("ACME", "R1"),
                                      issue,
                                      Set.of(oboId),
                                      Set.of(iri));
    }

    @AfterEach
    void tearDown() {
        issueStore.deleteAll();
    }

    @Test
    void shouldSaveIssue() {
        issueStore.save(issueRecord);
        var count = issueStore.count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    void shouldNotSaveDuplicateIssue() {
        issueStore.save(issueRecord);
        // Save the same record again
        issueStore.save(issueRecord);
        var count = issueStore.count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    void shouldFindByNodeId() {
        issueStore.save(issueRecord);
        var found = issueStore.findById(NODE_ID);
        assertThat(found).map(IssueRecord::nodeId).contains(NODE_ID);
    }

    @Test
    void shouldRoundTripRecord() {
        issueStore.save(issueRecord);
        var found = issueStore.findById(NODE_ID);
        assertThat(found).isPresent();
        var foundRecord = found.get();
        assertThat(foundRecord.nodeId()).isEqualTo(NODE_ID);
        assertThat(foundRecord.iris()).containsExactly(iri);
        assertThat(foundRecord.oboIds()).containsExactly(oboId);
        assertThat(foundRecord.issue()).isEqualTo(issue);
    }

    @Test
    void shouldDeleteIssuesWithRepoCoords() {
        issueStore.save(issueRecord);
        assertThat(issueStore.count()).isEqualTo(1);
        issueStore.deleteAllByRepoCoords(repoCoords);
        assertThat(issueStore.count()).isEqualTo(0);
    }

    @Test
    void shouldFindByIri() {
        issueStore.save(issueRecord);
        var found = issueStore.findAllByIris(iri);
        assertThat(found).hasSize(1);
        assertThat(found).first().hasFieldOrPropertyWithValue("nodeId", NODE_ID);
    }

    @Test
    void shouldFindByOboId() {
        issueStore.save(issueRecord);
        var found = issueStore.findAllByOboIds(oboId);
        assertThat(found).hasSize(1);
        assertThat(found).first().hasFieldOrPropertyWithValue("nodeId", NODE_ID);
    }

    @Test
    void shouldFindByRepoCoordsIdAndOboIds() {
        issueStore.save(issueRecord);
        var found = issueStore.findAllByRepoCoordsAndOboIds(repoCoords, oboId);
        assertThat(found).hasSize(1);
        assertThat(found).first().hasFieldOrPropertyWithValue("nodeId", NODE_ID);
    }

    @Test
    void shouldFindByProjectIdAndIris() {
        issueStore.save(issueRecord);
        var found = issueStore.findAllByRepoCoordsAndIris(repoCoords, iri);
        assertThat(found).hasSize(1);
        assertThat(found).first().hasFieldOrPropertyWithValue("nodeId", NODE_ID);
    }

    @Test
    void shouldModifyIssue() {
        issueStore.save(issueRecord);
        var updatedTitle = "Updated title";
        var nextIssue = GitHubIssue.get(
                issue.url(),
                issue.id(),
                issue.nodeId(),
                issue.number(), updatedTitle,
                issue.user(),
                issue.labels(),
                issue.htmlUrl(),
                issue.state(),
                issue.locked(),
                issue.assignee(),
                issue.assignees(),
                issue.milestone(),
                issue.comments(),
                issue.createdAt(),
                issue.updatedAt(),
                issue.closedAt(),
                issue.closedBy(),
                issue.authorAssociation(),
                issue.activeLockReason(),
                issue.body(),
                issue.reactions(),
                issue.stateReason()
        );
        var nextRecord = IssueRecord.of(
                                         nextIssue,
                                        repoCoords,
                                        issueRecord.oboIds(),
                                        issueRecord.iris());
        issueStore.save(nextRecord);
        assertThat(issueStore.count()).isEqualTo(1);
        assertThat(issueStore.findById(NODE_ID)).map(IssueRecord::nodeId).contains(NODE_ID);
        assertThat(issueStore.findById(NODE_ID)).map(rec -> rec.issue().title()).contains(updatedTitle);
    }
}

package edu.stanford.protege.issues.service;

import static org.junit.jupiter.api.Assertions.*;

import edu.stanford.protege.github.issues.GitHubIssue;
import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GitHubIssueTranslatorTest {

    private static final String NODE_ID = "TheNodeId";

    private final OboId oboId = OboId.valueOf("GO:1234567");

    private final Iri iri = Iri.valueOf("http://example.org/A");

    @Mock
    private GitHubRepositoryCoordinates repoCoords;

    @Mock
    private GitHubIssue issue;

    @Mock
    private TermIdExtractor termIdExtractor;

    private GitHubIssueTranslator translator;

    @BeforeEach
    public void setUp() {
        translator = new GitHubIssueTranslator(termIdExtractor);
    }

    @Test
    public void shouldReturnIssueRecord() {
        when(issue.nodeId()).thenReturn(NODE_ID);

        when(termIdExtractor.extractTermIds(anyString()))
                .thenReturn(new TermIdExtractor.ExtractedTermIds(Set.of(oboId),
                                                                 Set.of(iri)));

        var issueRecord = translator.getIssueRecord(issue, repoCoords);

        assertThat(issueRecord.repoCoords()).isEqualTo(repoCoords);
        assertThat(issueRecord.issue()).isEqualTo(issue);
        assertThat(issueRecord.oboIds()).containsExactly(oboId);
        assertThat(issueRecord.iris()).containsExactly(iri);
        assertThat(issueRecord.nodeId()).isEqualTo(NODE_ID);
    }

    @Test
    public void shouldThrowNpeWithNullIssue() {
        var projectId = mock(ProjectId.class);
        assertThrows(NullPointerException.class, () -> translator.getIssueRecord(null, repoCoords));
    }

    @Test
    public void shouldThrowNpeWithNullProjectId() {
        assertThrows(NullPointerException.class, () -> translator.getIssueRecord(issue, null));
    }
}


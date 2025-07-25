package edu.stanford.protege.issues.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.stanford.protege.github.issues.GitHubIssue;
import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.io.StringReader;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IssueRecordTest {

    protected static final long ID = 33;

    @Mock
    private GitHubIssue issue;

    private GitHubRepositoryCoordinates repoCoords = GitHubRepositoryCoordinates.of("ACME", "R1");

    private Set<OboId> oboIds = Set.of(mock(OboId.class));

    private Set<Iri> iris = Set.of(mock(Iri.class));

    private JacksonTester<IssueRecord> tester;

    @BeforeEach
    void setUp() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void shouldCreateObjectWithConstructor() {
        when(issue.id()).thenReturn(ID);
        var issueRecord = new IssueRecord(ID, repoCoords, issue, oboIds, iris);

        assertThat(issueRecord).isNotNull();
        assertThat(issueRecord.id()).isEqualTo(ID);
        assertThat(issueRecord.repoCoords()).isEqualTo(repoCoords);
        assertThat(issueRecord.issue()).isEqualTo(issue);
        assertThat(issueRecord.oboIds()).isEqualTo(oboIds);
        assertThat(issueRecord.iris()).isEqualTo(iris);
    }

    @Test
    public void createIssueRecord_NullArguments_ShouldThrowNullPointerException() {
        // Act and Assert
        assertThrows(NullPointerException.class, () -> new IssueRecord(null, null, null, null, null));
    }


    @Test
    public void shouldCreateObjectWithFactoryMethod() {
        when(issue.id()).thenReturn(ID);

        var issueRecord = IssueRecord.of(issue, repoCoords, oboIds, iris);

        assertThat(issueRecord).isNotNull();
        assertThat(issueRecord.id()).isEqualTo(ID);
        assertThat(issueRecord.repoCoords()).isEqualTo(repoCoords);
        assertThat(issueRecord.issue()).isEqualTo(issue);
        assertThat(issueRecord.oboIds()).isEqualTo(oboIds);
        assertThat(issueRecord.iris()).isEqualTo(iris);
    }

    @Test
    public void shouldThrowNpeWithFactoryMethod() {
        assertThrows(NullPointerException.class, () -> IssueRecord.of(null, null, null, null));
    }

    @Test
    public void shouldDeserializeJson() throws IOException {
        var inputStream = IssueRecordTest.class.getResourceAsStream("/IssueRecord.json");
        var issueRecord = tester.readObject(inputStream);
        assertThat(issueRecord.id()).isEqualTo(ID);
        assertThat(issueRecord.repoCoords()).isEqualTo(GitHubRepositoryCoordinates.of("ACME", "R1"));
        assertThat(issueRecord.iris()).containsExactly(Iri.valueOf("https://example.org/A"));
        assertThat(issueRecord.oboIds()).containsExactly(OboId.valueOf("GO:1234567"));
    }

    @Test
    void shouldRoundTrip() throws IOException {
        var inputStream = IssueRecordTest.class.getResourceAsStream("/IssueRecord.json");
        var issueRecord = tester.readObject(inputStream);
        var written = tester.write(issueRecord);
        var read = tester.readObject(new StringReader(written.getJson()));
        assertThat(issueRecord).isEqualTo(read);
    }

    @Test
    void shouldBeEqual() {
        when(issue.id()).thenReturn(ID);
        var issueRecordA = new IssueRecord(ID, repoCoords, issue, Set.of(), Set.of());
        var issueRecordB = new IssueRecord(ID, repoCoords, issue, Set.of(), Set.of());
        assertThat(issueRecordA).isEqualTo(issueRecordB);
    }
}
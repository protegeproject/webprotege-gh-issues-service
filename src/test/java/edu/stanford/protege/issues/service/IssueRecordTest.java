package edu.stanford.protege.issues.service;

import edu.stanford.protege.issues.shared.GitHubIssue;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@JsonTest
@AutoConfigureJson
@ExtendWith(MockitoExtension.class)
public class IssueRecordTest {

    protected static final String NODE_ID = "MDU6SXNzdWUx";

    @Mock
    private GitHubIssue issue;

    @Mock
    private ProjectId projectId;

    private Set<OboId> oboIds = Set.of(mock(OboId.class));

    private Set<Iri> iris = Set.of(mock(Iri.class));

    @Autowired
    private JacksonTester<IssueRecord> tester;

    @Test
    public void shouldCreateObjectWithConstructor() {
        when(issue.nodeId()).thenReturn(NODE_ID);
        var issueRecord = new IssueRecord(NODE_ID, projectId, issue, oboIds, iris);

        assertThat(issueRecord).isNotNull();
        assertThat(issueRecord.nodeId()).isEqualTo(NODE_ID);
        assertThat(issueRecord.projectId()).isEqualTo(projectId);
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
        when(issue.nodeId()).thenReturn(NODE_ID);

        var issueRecord = IssueRecord.of(projectId, issue, oboIds, iris);

        assertThat(issueRecord).isNotNull();
        assertThat(issueRecord.nodeId()).isEqualTo(NODE_ID);
        assertThat(issueRecord.projectId()).isEqualTo(projectId);
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
        assertThat(issueRecord.nodeId()).isEqualTo(NODE_ID);
        assertThat(issueRecord.projectId()).isEqualTo(ProjectId.valueOf("11111111-2222-3333-4444-555555555555"));
        assertThat(issueRecord.iris()).containsExactly(Iri.valueOf("https://example.org/A"));
        assertThat(issueRecord.oboIds()).containsExactly(OboId.valueOf("GO:1234567"));
    }
}
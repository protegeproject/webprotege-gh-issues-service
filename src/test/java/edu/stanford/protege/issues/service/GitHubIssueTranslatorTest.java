package edu.stanford.protege.issues.service;

import static org.junit.jupiter.api.Assertions.*;

import edu.stanford.protege.issues.shared.GitHubIssue;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GitHubIssueTranslatorTest {

    protected static final String ISSUE_TITLE = "Issue Title";

    protected static final String ISSUE_BODY = "Issue Body";

    private GitHubIssueTranslator translator;

    private TermIdExtractor termIdExtractor;

    @BeforeEach
    public void setUp() {
        termIdExtractor = mock(TermIdExtractor.class);
        translator = new GitHubIssueTranslator(termIdExtractor);
    }

    @Test
    public void getIssueRecord_ReturnsIssueRecordWithCorrectValues() {
        // Arrange
        var issue = mock(GitHubIssue.class);
        when(issue.title()).thenReturn(ISSUE_TITLE);
        when(issue.body()).thenReturn(ISSUE_BODY);

        var projectId = mock(ProjectId.class);

        // Mock the behavior of the TermIdExtractor
        var oboIdMentions = new LinkedHashSet<String>();
        oboIdMentions.add("OBO:123");
        var iriMentions = new LinkedHashSet<String>();
        iriMentions.add("http://example.com/iri");

        doAnswer(inv -> {
            Set<String> oboIds = inv.getArgument(1);
            Set<String> iris = inv.getArgument(2);
            oboIds.addAll(oboIdMentions);
            iris.addAll(iriMentions);
            return null;
        }).when(termIdExtractor).extractMentionedTermIds(anyString(), anySet(), anySet());


        // Act
        var issueRecord = translator.getIssueRecord(issue, projectId);

        // Assert
        assertThat(issueRecord.projectId()).isEqualTo(projectId);
        assertThat(issueRecord.issue()).isEqualTo(issue);
        assertThat(issueRecord.oboIds()).containsExactly(new OboId("OBO:123"));
        assertThat(issueRecord.iris()).containsExactly(new Iri("http://example.com/iri"));
    }

    @Test
    public void getIssueRecord_ThrowsNullPointerException_WhenIssueIsNull() {
        var projectId = mock(ProjectId.class);
        assertThrows(NullPointerException.class, () -> translator.getIssueRecord(null, projectId));
    }

    @Test
    public void getIssueRecord_ThrowsNullPointerException_WhenProjectIdIsNull() {
        var mockIssue = mock(GitHubIssue.class);
        assertThrows(NullPointerException.class, () -> translator.getIssueRecord(mockIssue, null));
    }
}


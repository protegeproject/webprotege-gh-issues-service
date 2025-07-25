package edu.stanford.webprotege.issues;

import static org.junit.jupiter.api.Assertions.*;

import edu.stanford.protege.github.issues.GitHubIssue;
import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.webprotege.issues.entity.Iri;
import edu.stanford.webprotege.issues.entity.OboId;
import edu.stanford.webprotege.issues.parser.TermIdExtractor;
import edu.stanford.webprotege.issues.translator.GitHubIssueTranslator;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.assertj.core.api.Assertions;
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

    private static final long ID = 33;

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
        when(issue.id()).thenReturn(ID);

        when(termIdExtractor.extractTermIds(anyString()))
                .thenReturn(new TermIdExtractor.ExtractedTermIds(Set.of(oboId),
                                                                 Set.of(iri)));

        var issueRecord = translator.getIssueRecord(issue, repoCoords);

        Assertions.assertThat(issueRecord.repoCoords()).isEqualTo(repoCoords);
        Assertions.assertThat(issueRecord.issue()).isEqualTo(issue);
        Assertions.assertThat(issueRecord.oboIds()).containsExactly(oboId);
        Assertions.assertThat(issueRecord.iris()).containsExactly(iri);
        Assertions.assertThat(issueRecord.id()).isEqualTo(ID);
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


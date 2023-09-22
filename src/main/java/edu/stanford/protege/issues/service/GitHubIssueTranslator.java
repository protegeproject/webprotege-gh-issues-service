package edu.stanford.protege.issues.service;

import edu.stanford.protege.issues.shared.GitHubIssue;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 *  The {@code GitHubIssueTranslator} class is responsible for translating a GitHubIssue
 *  into an {@link IssueRecord} object by extracting relevant information and term IDs
 *  from the issue's title and body.
 *
 *  Matthew Horridge
 *  Stanford Center for Biomedical Informatics Research
 *  2023-09-21
 */
@Component
public class GitHubIssueTranslator {

    private final TermIdExtractor termIdExtractor;

    public GitHubIssueTranslator(TermIdExtractor termIdExtractor) {
        this.termIdExtractor = Objects.requireNonNull(termIdExtractor);
    }

    /**
     * Translates a GitHubIssue into an {@link IssueRecord} object.
     * <p>
     * This method extracts term mentions from the issue's title and body using a
     * {@link TermIdExtractor} and constructs an {@link IssueRecord} containing
     * the project ID, GitHubIssue, OBO IDs, and IRIs based on the mentions.
     * </p>
     *
     * @param issue The GitHub issue to be translated. It should not be {@code null}.
     * @param projectId The project ID associated with the GitHub issue. It should not be {@code null}.
     * @return An {@link IssueRecord} representing the translated GitHub issue.
     *
     * @throws NullPointerException if {@code issue} or {@code projectId} is {@code null}.
     */
    @Nonnull
    public IssueRecord getIssueRecord(@Nonnull GitHubIssue issue,
                                      @Nonnull ProjectId projectId) {

        Objects.requireNonNull(issue);
        Objects.requireNonNull(projectId);

        var combinedText = issue.title() + "  " + issue.body();

        var oboIdMentions = new LinkedHashSet<String>();
        var iriMentions = new LinkedHashSet<String>();
        termIdExtractor.extractMentionedTermIds(combinedText, oboIdMentions, iriMentions);

        var oboIds = oboIdMentions.stream().map(OboId::new).collect(Collectors.toSet());
        var iris = iriMentions.stream().map(Iri::new).collect(Collectors.toSet());
        return IssueRecord.of(projectId, issue, oboIds, iris);
    }


}

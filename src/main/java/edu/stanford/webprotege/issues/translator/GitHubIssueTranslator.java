package edu.stanford.webprotege.issues.translator;

import edu.stanford.protege.github.issues.GitHubIssue;
import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.webprotege.issues.persistence.IssueRecord;
import edu.stanford.webprotege.issues.parser.TermIdExtractor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Objects;

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
     * Translates a GitHubIssue into an {@link IssueRecord}.
     * <p>
     * This method extracts term mentions from the issue's title and body using a
     * {@link TermIdExtractor} and constructs an {@link IssueRecord} containing
     * the project ID, GitHubIssue, OBO IDs, and IRIs based on the extracted Term IDs.
     * </p>
     *
     * @param issue The GitHub issue to be translated. It should not be {@code null}.
     * @param repoCoords The project ID associated with the GitHub issue. It should not be {@code null}.
     * @return An {@link IssueRecord} representing the translated GitHub issue.
     *
     * @throws NullPointerException if {@code issue} or {@code projectId} is {@code null}.
     */
    @Nonnull
    public IssueRecord getIssueRecord(@Nonnull GitHubIssue issue,
                                      @Nonnull GitHubRepositoryCoordinates repoCoords) {

        Objects.requireNonNull(issue);
        Objects.requireNonNull(repoCoords);

        var combinedText = issue.title() + "  " + issue.body();

        var extractedIds = termIdExtractor.extractTermIds(combinedText);
        return IssueRecord.of(issue, repoCoords, extractedIds.extractedOboIds(), extractedIds.extractedIris());
    }


}

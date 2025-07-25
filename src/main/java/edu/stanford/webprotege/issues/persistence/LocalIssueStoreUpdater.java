package edu.stanford.webprotege.issues.persistence;

import edu.stanford.protege.github.issues.GitHubIssue;
import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.webprotege.issues.translator.GitHubIssueTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-03
 * The LocalIssueStoreUpdater class is responsible for updating the local issue store with GitHub issues in a specific project.
 * It provides methods for translating GitHub issues to records and saving them to the local issue store.
 */
@Component
public class LocalIssueStoreUpdater {

    private static final Logger logger = LoggerFactory.getLogger(LocalIssueStoreLoader.class);

    private final GitHubIssueTranslator gitHubIssueTranslator;

    private final LocalIssueStore localIssueStore;

    /**
     * Constructs a new LocalIssueStoreUpdater object.
     *
     * @param gitHubIssueTranslator The translator for converting translated issues to issue records. Must not be null.
     * @param localIssueStore       The local issue store to update. Must not be null.
     */
    public LocalIssueStoreUpdater(GitHubIssueTranslator gitHubIssueTranslator,
                                  LocalIssueStore localIssueStore) {
        this.gitHubIssueTranslator = gitHubIssueTranslator;
        this.localIssueStore = localIssueStore;
    }

    public synchronized void updateIssues(@Nonnull GitHubRepositoryCoordinates repoCoords,
                                         @Nonnull List<GitHubIssue> issues) {
        logger.info("Updating issues {} in {}", issues.size(), repoCoords);
        var issueIds = issues.stream()
                .map(GitHubIssue::id)
                .toList();
        localIssueStore.deleteAllById(issueIds);
        var records = issues.stream()
                .map(issue -> gitHubIssueTranslator.getIssueRecord(issue, repoCoords))
                .toList();
        localIssueStore.saveAll(records);
    }

    /**
     * Replaces all locally stored issues in a specific GitHub repository with the provided GitHub issues.
     *
     * @param repoCoords The coordinates of the GitHub repository to which the GitHub issues belong. Must not be null.
     * @param issues The list of GitHub issues to update the local store with.
     */
    public synchronized void replaceAll(@Nonnull GitHubRepositoryCoordinates repoCoords,
                                        @Nonnull List<GitHubIssue> issues) {
        Objects.requireNonNull(repoCoords);
        // Translate to records and save
        var issueRecords = translateToIssueRecords(repoCoords, issues);

        // Delete any existing issues with the specified repoCoords
        localIssueStore.deleteAllByRepoCoords(repoCoords);
        logger.info("{} Deleted all locally stored issues for {}", repoCoords, repoCoords);

        localIssueStore.saveAll(issueRecords);
        logger.info("{} Stored {} issues in the local issue store", repoCoords, issueRecords.size());
    }

    /**
     * Translates a list of GitHub issues into a list of issue records specific to a project.
     *
     * @param repoCoords The coordinates of the GitHub repo for which the translation is performed.
     * @param allIssues The list of GitHub issues to translate.
     * @return A list of issue records.
     */
    private List<IssueRecord> translateToIssueRecords(GitHubRepositoryCoordinates repoCoords,
                                                      List<GitHubIssue> allIssues) {
        return allIssues.stream()
                        .map(issue -> gitHubIssueTranslator.getIssueRecord(issue, repoCoords))
                        .toList();
    }
}


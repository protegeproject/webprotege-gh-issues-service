package edu.stanford.protege.issues.service;

import edu.stanford.protege.github.issues.GitHubIssue;
import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.kohsuke.github.GHIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private final GHIssueTranslator ghIssueTranslator;

    private final GitHubIssueTranslator gitHubIssueTranslator;

    private final LocalIssueStore localIssueStore;

    private final GitHubIssueTranslator issueTranslator;

    /**
     * Constructs a new LocalIssueStoreUpdater object.
     *
     * @param ghIssueTranslator     The translator for converting GitHub issues to records. Must not be null.
     * @param gitHubIssueTranslator The translator for converting translated issues to issue records. Must not be null.
     * @param localIssueStore       The local issue store to update. Must not be null.
     * @param issueTranslator
     */
    public LocalIssueStoreUpdater(GHIssueTranslator ghIssueTranslator,
                                  GitHubIssueTranslator gitHubIssueTranslator,
                                  LocalIssueStore localIssueStore, GitHubIssueTranslator issueTranslator) {
        this.ghIssueTranslator = ghIssueTranslator;
        this.gitHubIssueTranslator = gitHubIssueTranslator;
        this.localIssueStore = localIssueStore;
        this.issueTranslator = issueTranslator;
    }

    public synchronized void updateIssues(@Nonnull GitHubRepositoryCoordinates repoCoords,
                                         @Nonnull List<GitHubIssue> issues) {
        var records = issues.stream()
                .map(issue -> issueTranslator.getIssueRecord(issue, repoCoords))
                .toList();
        var issueIds = issues.stream()
                .map(GitHubIssue::nodeId)
                .collect(Collectors.toSet());
        var issuesToUpdate = localIssueStore.findAllById(issueIds);

//        localIssueStore.deleteAllById();
        localIssueStore.saveAll(records);
    }

    /**
     * Replaces all locally stored issues in a specific GitHub repository with the provided GitHub issues.
     *
     * @param repoCoords The coordinates of the GitHub repository to which the GitHub issues belong. Must not be null.
     * @param ghIssues The list of GitHub issues to update the local store with.
     */
    public synchronized void replaceAll(@Nonnull GitHubRepositoryCoordinates repoCoords,
                                        @Nonnull List<GHIssue> ghIssues) {
        Objects.requireNonNull(repoCoords);
        // Translate to records and save
        var issueRecords = translateToIssueRecords(repoCoords, ghIssues);

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
                                                      List<GHIssue> allIssues) {
        return allIssues.stream()
                        .map(ghIssueTranslator::translate)
                        .map(issue -> gitHubIssueTranslator.getIssueRecord(issue, repoCoords))
                        .toList();
    }
}


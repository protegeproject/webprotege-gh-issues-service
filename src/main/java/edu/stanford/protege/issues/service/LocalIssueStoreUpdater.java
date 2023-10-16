package edu.stanford.protege.issues.service;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.kohsuke.github.GHIssue;
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

    private final GHIssueTranslator ghIssueTranslator;

    private final GitHubIssueTranslator gitHubIssueTranslator;

    private final LocalIssueStore localIssueStore;

    /**
     * Constructs a new LocalIssueStoreUpdater object.
     *
     * @param ghIssueTranslator The translator for converting GitHub issues to records. Must not be null.
     * @param gitHubIssueTranslator The translator for converting translated issues to issue records. Must not be null.
     * @param localIssueStore The local issue store to update. Must not be null.
     */
    public LocalIssueStoreUpdater(GHIssueTranslator ghIssueTranslator,
                                  GitHubIssueTranslator gitHubIssueTranslator,
                                  LocalIssueStore localIssueStore) {
        this.ghIssueTranslator = ghIssueTranslator;
        this.gitHubIssueTranslator = gitHubIssueTranslator;
        this.localIssueStore = localIssueStore;
    }

    /**
     * Replaces all locally stored issues in a specific project with the provided GitHub issues.
     *
     * @param projectId The ID of the project to which the GitHub issues belong. Must not be null.
     * @param ghIssues The list of GitHub issues to update the local store with.
     */
    public synchronized void replaceAllInProject(@Nonnull ProjectId projectId,
                                                 @Nonnull List<GHIssue> ghIssues) {
        Objects.requireNonNull(projectId);
        // Translate to records and save
        var issueRecords = translateToIssueRecords(projectId, ghIssues);

        // Delete any existing issues with the specified projectId
        localIssueStore.deleteAllByProjectId(projectId);
        logger.info("{} Deleted all locally stored issues for {}", projectId, projectId);

        localIssueStore.saveAll(issueRecords);
        logger.info("{} Stored {} issues in the local issue store", projectId, issueRecords.size());
    }

    /**
     * Translates a list of GitHub issues into a list of issue records specific to a project.
     *
     * @param projectId The ID of the project for which the translation is performed.
     * @param allIssues The list of GitHub issues to translate.
     * @return A list of issue records.
     */
    private List<IssueRecord> translateToIssueRecords(ProjectId projectId,
                                                      List<GHIssue> allIssues) {
        return allIssues.stream()
                        .map(ghIssueTranslator::translate)
                        .map(issue -> gitHubIssueTranslator.getIssueRecord(issue, projectId))
                        .toList();
    }
}


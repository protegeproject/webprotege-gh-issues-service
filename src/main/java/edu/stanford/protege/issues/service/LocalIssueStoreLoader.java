package edu.stanford.protege.issues.service;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-21
 */
@Component
public class LocalIssueStoreLoader {

    private static final Logger logger = LoggerFactory.getLogger(LocalIssueStoreLoader.class);

    private final LocalIssueStoreUpdater localIssueStoreUpdater;

    private final GitHub gitHub;

    public LocalIssueStoreLoader(LocalIssueStoreUpdater localIssueStoreUpdater, GitHub gitHub) {
        this.localIssueStoreUpdater = localIssueStoreUpdater;
        this.gitHub = gitHub;
    }

    /**
     * Retrieves issues from a specified GitHub repository and saves them as {@link IssueRecord}s
     * in the local issue store, associated with the provided project ID.
     * <p>
     *
     * @param projectId The project ID to associate with the loaded issues. It should not be {@code null}.
     * @param repositoryCoordinates The coordinates of the GitHub repository to load issues from.
     *                             It should not be {@code null}.
     * @throws IOException If an I/O error occurs during the loading or saving process.
     *
     * @throws NullPointerException if {@code projectId} or {@code repositoryCoordinates} is {@code null}.
     */
    public synchronized void load(@Nonnull ProjectId projectId,
                     @Nonnull GitHubRepositoryCoordinates repositoryCoordinates) throws IOException {

        Objects.requireNonNull(projectId);
        Objects.requireNonNull(repositoryCoordinates);

        // Retrieve the issues from GitHub
        var gitHubRepo = gitHub.getRepository(repositoryCoordinates.getName());

        logger.info("{} Retrieving issues from GitHub repository: {}", projectId, repositoryCoordinates);
        var allIssues = gitHubRepo.getIssues(GHIssueState.ALL);
        logger.info("{} Retrieved {} issues from {}", projectId, allIssues.size(), repositoryCoordinates);

        localIssueStoreUpdater.replaceAllInProject(projectId, allIssues);
    }
}

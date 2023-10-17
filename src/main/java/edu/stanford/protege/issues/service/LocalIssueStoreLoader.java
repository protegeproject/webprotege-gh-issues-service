package edu.stanford.protege.issues.service;

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
     * @param repoCoords The coordinates of the GitHub repository to load issues from.
     *                             It should not be {@code null}.
     * @throws IOException If an I/O error occurs during the loading or saving process.
     *
     * @throws NullPointerException if {@code repoCoords} or {@code repoCoords} is {@code null}.
     */
    public synchronized void load(@Nonnull GitHubRepositoryCoordinates repoCoords) throws IOException {

        Objects.requireNonNull(repoCoords);
        Objects.requireNonNull(repoCoords);

        // Retrieve the issues from GitHub
        var gitHubRepo = gitHub.getRepository(repoCoords.getFullName());

        logger.info("{} Retrieving issues from GitHub repository: {}", repoCoords, repoCoords);
        var allIssues = gitHubRepo.getIssues(GHIssueState.ALL);
        logger.info("{} Retrieved {} issues from {}", repoCoords, allIssues.size(), repoCoords);

        localIssueStoreUpdater.replaceAll(repoCoords, allIssues);
    }
}

package edu.stanford.webprotege.issues.persistence;

import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.webprotege.issues.service.GitHubIssueFetcherService;
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

    private final GitHubIssueFetcherService issueFetcherService;

    public LocalIssueStoreLoader(LocalIssueStoreUpdater localIssueStoreUpdater, GitHubIssueFetcherService issueFetcherService) {
        this.localIssueStoreUpdater = localIssueStoreUpdater;
        this.issueFetcherService = issueFetcherService;
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
        logger.info("{} Retrieving issues from GitHub repository: {}", repoCoords, repoCoords);
        var issuesFlux = issueFetcherService.fetchAllIssues(repoCoords);
        var issuesMono = issuesFlux.collectList();
        var issues = issuesMono.block();
        logger.info("{} Retrieved {} issues from {}", repoCoords, issues.size(), repoCoords);

        localIssueStoreUpdater.replaceAll(repoCoords, issues);
    }
}

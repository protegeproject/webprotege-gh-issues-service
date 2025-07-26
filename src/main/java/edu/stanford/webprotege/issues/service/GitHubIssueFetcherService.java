package edu.stanford.webprotege.issues.service;

import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.protege.github.issues.GitHubIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * A service for retrieving all GitHub issues for a given repository using the GitHub REST API v3.
 * <p>
 * This service handles pagination transparently by following the {@code Link} response header provided by GitHub.
 * It streams all issues (open, closed, or otherwise) across multiple pages, up to the full result set.
 * </p>
 *
 * <p>This class assumes:</p>
 * <ul>
 *     <li>The {@link WebClient} is preconfigured for GitHub's API base URL and headers.</li>
 *     <li>The {@link GitHubInstallationTokenService} is responsible for providing authentication tokens (if needed).</li>
 *     <li>The {@link NextPageExtractor} handles parsing the {@code Link} header to determine if pagination should continue.</li>
 * </ul>
 *
 * <p><b>Usage:</b></p>
 * <pre>{@code
 * GitHubRepositoryCoordinates coords = GitHubRepositoryCoordinates.of("octocat", "hello-world");
 * Flux<GitHubIssue> issues = retrieverService.fetchAllIssues(coords);
 * }</pre>
 */
@Service
public class GitHubIssueFetcherService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubIssueFetcherService.class);

    /**
     * The maximum number of issues returned per page (GitHub's limit is 100).
     */
    public static final int PAGE_SIZE = 100;

    private final WebClient webClient;
    private final GitHubInstallationTokenService tokenService;
    private final NextPageExtractor nextPageExtractor;

    /**
     * Constructs a new {@link GitHubIssueFetcherService}.
     *
     * @param webClient        A pre-configured {@link WebClient} for making GitHub API calls.
     * @param tokenService     The service used to retrieve authentication tokens for GitHub App installations.
     * @param nextPageExtractor A helper for extracting pagination info from GitHub response headers.
     */
    public GitHubIssueFetcherService(WebClient webClient,
                                     GitHubInstallationTokenService tokenService,
                                     NextPageExtractor nextPageExtractor) {
        this.webClient = webClient;
        this.tokenService = tokenService;
        this.nextPageExtractor = nextPageExtractor;
    }

    /**
     * Fetches all issues (open, closed, or otherwise) for the specified GitHub repository.
     *
     * @param coordinates The GitHub repository owner and name.
     * @return A {@link Flux} stream of all {@link GitHubIssue}s found in the repository.
     */
    public Flux<GitHubIssue> fetchAllIssues(GitHubRepositoryCoordinates coordinates) {
        return fetchAllIssuesFromPage(coordinates, 1);
    }

    /**
     * Recursively fetches issues starting from a given page number, combining all pages using {@link Flux#concat}.
     *
     * @param coordinates The repository coordinates (owner and repo name).
     * @param page        The current page number to fetch.
     * @return A {@link Flux} of {@link GitHubIssue}s aggregated across pages.
     */
    private Flux<GitHubIssue> fetchAllIssuesFromPage(GitHubRepositoryCoordinates coordinates, int page) {
        return fetchIssuesPage(coordinates, page)
                .flatMapMany(issuePage -> {
                    var current = Flux.fromIterable(issuePage.issues());
                    return issuePage.nextPage()
                            .map(next -> Flux.concat(current, fetchAllIssuesFromPage(coordinates, next)))
                            .orElse(current);
                });
    }

    /**
     * Fetches a single page of issues from the GitHub API and determines if another page exists using the
     * {@code Link} response header.
     *
     * @param coordinates The repository coordinates.
     * @param page        The specific page number to request.
     * @return A {@link Mono} containing an {@link IssuePage}, which wraps the issues and optional next page number.
     */
    private Mono<IssuePage> fetchIssuesPage(GitHubRepositoryCoordinates coordinates, int page) {
        logger.debug("Fetching GitHub issues for repo: {} page: {}", coordinates.getFullName(), page);

        return tokenService.getInstallationToken(coordinates)
                .flatMap(token -> webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/repos/{owner}/{repo}/issues")
                                .queryParam("state", "all")
                                .queryParam("per_page", PAGE_SIZE)
                                .queryParam("page", page)
                                .build(coordinates.ownerName(), coordinates.repositoryName()))
                        .headers(httpHeaders -> httpHeaders.setBearerAuth(token.token()))
                        .exchangeToMono(response -> {
                            var nextPage = nextPageExtractor.extractNextPage(response.headers().asHttpHeaders());
                            return response.bodyToFlux(GitHubIssue.class)
                                    .collectList()
                                    .doOnNext(issues -> logger.debug(
                                            "Received {} issues for repo: {} page: {} (next page: {})",
                                            issues.size(),
                                            coordinates.getFullName(),
                                            page,
                                            nextPage.map(String::valueOf).orElse("none")
                                    ))
                                    .map(issues -> new IssuePage(issues, nextPage));
                        }))
                .doOnError(ex -> logger.error("Failed to fetch issues for {} page {}: {}", coordinates.getFullName(), page, ex.getMessage(), ex));
    }

    /**
     * A simple data holder for a single page of GitHub issues and the optional number of the next page.
     *
     * @param issues   The list of issues in the current page.
     * @param nextPage The next page number, if one exists.
     */
    record IssuePage(List<GitHubIssue> issues, Optional<Integer> nextPage) {}
}
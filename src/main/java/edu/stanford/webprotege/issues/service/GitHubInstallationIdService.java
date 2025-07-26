package edu.stanford.webprotege.issues.service;

import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.webprotege.issues.auth.GitHubJwt;
import edu.stanford.webprotege.issues.auth.GitHubJwtFactory;
import edu.stanford.webprotege.issues.model.GitHubAppInstallationMetadata;
import edu.stanford.webprotege.issues.model.GitHubInstallationId;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A service for retrieving and caching GitHub App Installation IDs associated with repositories.
 * <p>
 * This service makes authenticated GitHub REST API calls using a GitHub App JWT to retrieve the installation
 * ID for a given repository (i.e., the ID representing the GitHub App's installation in that repository context).
 * </p>
 *
 * <p>
 * Installation IDs are memoized in an in-memory cache per {@link GitHubRepositoryCoordinates}, ensuring that
 * duplicate API requests are avoided for the same repository. The results are cached using Reactor's {@code .cache()}
 * operator, meaning they are only computed once and shared with all subscribers.
 */
@Service
public class GitHubInstallationIdService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubInstallationIdService.class);

    private final GitHubJwtFactory jwtFactory;

    private final WebClient webClient;

    /**
     * Cache for memoizing installation ID retrieval results keyed by repository coordinates.
     */
    private final Map<GitHubRepositoryCoordinates, Mono<GitHubInstallationId>> installationIdCache = new ConcurrentHashMap<>();

    /**
     * Constructs a new {@link GitHubInstallationIdService}.
     *
     * @param jwtFactory A factory for producing JWTs used to authenticate as the GitHub App.
     * @param webClient  A pre-configured {@link WebClient} for calling GitHub's API.
     */
    public GitHubInstallationIdService(GitHubJwtFactory jwtFactory, WebClient webClient) {
        this.jwtFactory = jwtFactory;
        this.webClient = webClient;
    }

    /**
     * Retrieves the GitHub App installation ID for the given repository.
     * <p>
     * If the installation ID was previously retrieved, the cached value is returned.
     * Otherwise, the service will make an authenticated request to:
     * {@code GET /repos/{owner}/{repo}/installation}
     *
     * @param repoCoords The repository owner and name.
     * @return A {@link Mono} emitting the {@link GitHubInstallationId}, or an error if the installation is not found or unauthorized.
     */
    public Mono<GitHubInstallationId> getAppInstallationId(GitHubRepositoryCoordinates repoCoords) {
        // Return memoized Mono if it exists; otherwise compute and cache it
        return installationIdCache.computeIfAbsent(repoCoords, this::fetchInstallationIdForRepo);
    }

    /**
     * Performs the actual GitHub API call to fetch the installation ID for the given repository.
     *
     * @param repoCoords The repository coordinates.
     * @return A cached {@link Mono} that emits the installation ID or an error.
     */
    private Mono<GitHubInstallationId> fetchInstallationIdForRepo(GitHubRepositoryCoordinates repoCoords) {
        GitHubJwt jwt = jwtFactory.getJwt();

        return webClient.get()
                .uri("/repos/{owner}/{repo}/installation" , repoCoords.ownerName(), repoCoords.repositoryName())
                .headers(headers -> headers.setBearerAuth(jwt.token()))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> handle4xx(response, repoCoords))
                .onStatus(HttpStatusCode::is5xxServerError, GitHubInstallationIdService::handle5xx)
                .bodyToMono(GitHubAppInstallationMetadata.class)
                .map(GitHubAppInstallationMetadata::id)
                .map(GitHubInstallationId::valueOf)
                .doOnError(WebClientResponseException.class, ex ->
                        logger.error("Error retrieving GitHub App installation ID (status {}): {}" ,
                                ex.getStatusCode(), ex.getMessage()))
                // Cache the result of the Mono so that the GitHub API call is made only once.
                // Subsequent subscribers will receive the same cached result without triggering another request.
                // This ensures that concurrent calls for the same repository do not cause duplicate HTTP traffic.
                .cache();
    }

    /**
     * Handles 4xx client errors from the GitHub API and produces more descriptive exceptions.
     *
     * @param response   The {@link ClientResponse} from the API call.
     * @param repoCoords The repository coordinates used in the request.
     * @return A {@link Mono} that emits an appropriate error.
     */
    private Mono<Throwable> handle4xx(ClientResponse response, GitHubRepositoryCoordinates repoCoords) {
        var status = response.statusCode();
        if (status == HttpStatus.NOT_FOUND) {
            return Mono.error(new IllegalStateException(
                    "GitHub App is not installed on repository: " + repoCoords.getFullName()));
        } else if (status == HttpStatus.FORBIDDEN) {
            return Mono.error(new IllegalStateException(
                    "Access forbidden when fetching installation ID. Check token permissions." ));
        } else {
            return response.bodyToMono(String.class)
                    .defaultIfEmpty("Unknown client error" )
                    .flatMap(body -> Mono.error(new IllegalStateException(
                            "Client error (" + status + "): " + body)));
        }
    }

    /**
     * Handles 5xx server errors from the GitHub API.
     *
     * @param response The {@link ClientResponse} containing the status code.
     * @return A {@link Mono} that emits an error with a descriptive message.
     */
    private static @NotNull Mono<Throwable> handle5xx(ClientResponse response) {
        return Mono.error(new IllegalStateException("GitHub server error: " + response.statusCode()));
    }
}
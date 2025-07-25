package edu.stanford.webprotege.issues.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.github.GitHubRepository;
import edu.stanford.protege.github.issues.GitHubIssue;

/**
 * Payload for an {@code issues} event sent by GitHub webhooks.
 *
 * @param action     the action performed on the issue (e.g., {@code opened}, {@code closed}, {@code edited})
 * @param issue      the issue object associated with the event
 * @param repository the repository where the issue resides
 * @param sender     the GitHub user who triggered the event
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubIssuesEvent(
        @JsonProperty("action") GitHubIssueAction action,
        @JsonProperty("issue") GitHubIssue issue,
        @JsonProperty("repository") GitHubRepository repository,
        @JsonProperty("sender") GitHubSender sender
) {
    /**
     * The GitHub event type identifier for issues.
     */
    public static final String TYPE = "issues";
}
package edu.stanford.protege.issues.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.github.issues.shared.GitHubIssue;
import edu.stanford.protege.github.shared.GitHubRepository;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-16
 */
public record GitHubWebhookIssueEventPayload(@JsonProperty("action") String action,
                                             @JsonProperty("issue") GitHubIssue issue,
                                             @JsonProperty("repository") GitHubRepository repository) {

    @JsonCreator
    public GitHubWebhookIssueEventPayload(@JsonProperty("action") String action,
                                          @JsonProperty("issue") GitHubIssue issue,
                                          @JsonProperty("repository") GitHubRepository repository) {
        this.action = action;
        this.issue = issue;
        this.repository = repository;
    }
}

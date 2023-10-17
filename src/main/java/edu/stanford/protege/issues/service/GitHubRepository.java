package edu.stanford.protege.issues.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubRepository(@JsonProperty("id") long id,
                               @JsonProperty("node_id") String nodeId,
                               @JsonProperty("name") String name,
                               @JsonProperty("full_name") String fullName) {

    @JsonIgnore
    public GitHubRepositoryCoordinates getCoordinates() {
        return GitHubRepositoryCoordinates.fromFullName(fullName);
    }
}

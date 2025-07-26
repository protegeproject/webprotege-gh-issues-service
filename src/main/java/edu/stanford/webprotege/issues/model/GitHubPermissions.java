package edu.stanford.webprotege.issues.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the set of permissions granted to a GitHub App installation
 * on a repository or organization scope.  These are the permissions we are interested in
 * for WebProtégé – we don't track all possible permissions.
 *
 * @param issues   Permission level for issue-related actions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubPermissions(
        @JsonProperty("issues") GitHubPermissionLevel issues
) {
}
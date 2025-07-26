package edu.stanford.webprotege.issues.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.webprotege.issues.model.GitHubPermissions;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubInstallationToken(@JsonProperty("token") String token,
                                      @JsonProperty("expires_at") Instant expiresAt,
                                      @JsonProperty("permissions") GitHubPermissions permissions) {
}

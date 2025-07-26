package edu.stanford.webprotege.issues.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

/**
 * Represents metadata about a GitHub App installation on a specific repository.
 * This data is returned from the GitHub API when querying
 * {@code /repos/{owner}/{repo}/installation} using an app JWT.
 *
 * @param id                  The unique ID of the installation.
 * @param account             The user or organization account where the app is installed.
 * @param repositorySelection Indicates whether the app is installed on all or selected repositories.
 * @param accessTokensUrl     The URL for creating access tokens for this installation.
 * @param repositoriesUrl     The URL for listing repositories accessible to this installation.
 * @param htmlUrl             The URL to view the installation in the GitHub UI.
 * @param appId               The unique ID of the GitHub App.
 * @param appSlug             The slug (URL-friendly name) of the GitHub App.
 * @param permissions         The set of permissions granted to the app for this installation.
 * @param events              The list of events the app is subscribed to.
 * @param createdAt           The timestamp when the installation was created.
 * @param updatedAt           The timestamp when the installation was last updated.
 * @param singleFileName      If the app has single file access, this field contains the file path.
 */
public record GitHubAppInstallationMetadata(
        @JsonProperty("id" ) long id,
        @JsonProperty("account" ) GitHubAccount account,
        @JsonProperty("repository_selection" ) String repositorySelection,
        @JsonProperty("access_tokens_url" ) String accessTokensUrl,
        @JsonProperty("repositories_url" ) String repositoriesUrl,
        @JsonProperty("html_url" ) String htmlUrl,
        @JsonProperty("app_id" ) long appId,
        @JsonProperty("app_slug" ) String appSlug,
        @JsonProperty("permissions" ) GitHubPermissions permissions,
        @JsonProperty("events" ) List<GitHubEvent> events,
        @JsonProperty("created_at" ) Instant createdAt,
        @JsonProperty("updated_at" ) Instant updatedAt,
        @JsonProperty("single_file_name" ) String singleFileName
) {

}
package edu.stanford.webprotege.issues.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents a GitHub App installation ID.
 * <p>
 * This ID uniquely identifies a specific installation of a GitHub App across organizations or repositories.
 * It is typically received from GitHub webhook payloads or authentication APIs.
 * </p>
 *
 * <p>
 * When serialized to JSON (e.g. for API responses or persistence), this object is automatically
 * represented by its raw string value via the {@link JsonValue} annotation.
 * </p>
 *
 * <p><b>Example:</b>
 * <pre>
 * {@code
 * GitHubInstallationId id = GitHubInstallationId.valueOf("123456");
 * String raw = id.id();  // "123456"
 * }</pre>
 * </p>
 *
 * @param id The raw string value of the GitHub installation ID.
 */
public record GitHubInstallationId(@JsonValue long id) {

    /**
     * Factory method to create a {@link GitHubInstallationId} from a string value.
     *
     * @param id The installation ID string (must not be null).
     * @return A new {@link GitHubInstallationId} instance wrapping the given string.
     */
    public static GitHubInstallationId valueOf(long id) {
        return new GitHubInstallationId(id);
    }
}
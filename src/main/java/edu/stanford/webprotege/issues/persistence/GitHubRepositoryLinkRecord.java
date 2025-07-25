package edu.stanford.webprotege.issues.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.data.annotation.Id;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-16
 * A record representing a link between a WebProtege Project and a GitHub repository.
 * This record stores information about the link, including the project ID,
 * GitHub repository coordinates
 */
public record GitHubRepositoryLinkRecord(
        @Id @JsonProperty("projectId") @Nonnull ProjectId projectId,
        @JsonProperty("repoCoords") @Nonnull GitHubRepositoryCoordinates repoCoords,
        @JsonProperty("updatedAt") @Nullable Instant updatedAt,
        @JsonProperty("updateRequired") boolean updateRequired) {

    /**
     * Constructs a new GitHubLinkRecord.
     *
     * @param projectId The project ID.
     * @param repoCoords The GitHub repository coordinates.
     * @param updatedAt The last update timestamp, or null if not available.
     * @param updateRequired Indicates whether an update is required.
     */
    @JsonCreator
    public GitHubRepositoryLinkRecord(
            @JsonProperty("projectId")
            @Nonnull ProjectId projectId,
            @JsonProperty("repoCoords")
            @Nonnull GitHubRepositoryCoordinates repoCoords,
            @JsonProperty("updatedAt")
            @Nullable Instant updatedAt,
            @JsonProperty("updateRequired")
            boolean updateRequired) {
        this.projectId = Objects.requireNonNull(projectId);
        this.repoCoords = Objects.requireNonNull(repoCoords);
        this.updatedAt = Objects.requireNonNullElse(updatedAt, Instant.EPOCH);
        this.updateRequired = updateRequired;
    }

    /**
     * Create a new GitHubLinkRecord with default values.
     *
     * @param projectId The project ID.
     * @param repoCoords The GitHub repository coordinates.
     * @return A new GitHubLinkRecord with default values and the given project ID and repository coordinates.
     */
    @Nonnull
    public static GitHubRepositoryLinkRecord of(@Nonnull ProjectId projectId, @Nonnull GitHubRepositoryCoordinates repoCoords) {
        return new GitHubRepositoryLinkRecord(projectId, repoCoords, null, true);
    }

    /**
     * Create a new GitHubLinkRecord with updated status information.
     *
     * @param updatedAt The updated timestamp.
     * @param updateRequired Indicates whether an update is required.
     * @return A new GitHubLinkRecord with the same project ID and repository coordinates but with updated status.
     */
    @Nonnull
    public GitHubRepositoryLinkRecord withUpdatedStatus(@Nonnull Instant updatedAt, boolean updateRequired) {
        return new GitHubRepositoryLinkRecord(this.projectId, this.repoCoords, updatedAt, updateRequired);
    }

    /**
     * Create a new GitHubLinkRecord with updated "updateRequired" status.
     *
     * @param updateRequired Indicates whether an update is required.
     * @return A new GitHubLinkRecord with the same project ID, repository coordinates, and last update timestamp,
     * but with the updated "updateRequired" status.
     */
    @Nonnull
    public GitHubRepositoryLinkRecord withUpdateRequired(boolean updateRequired) {
        return new GitHubRepositoryLinkRecord(this.projectId, this.repoCoords, this.updatedAt, updateRequired);
    }

    /**
     * Create a new GitHubLinkRecord with a failed update message.
     *
     * This method is used when an update fails, and the original record is retained without changes.
     *
     * @param message The message indicating the reason for the failed update.
     * @return The current GitHubLinkRecord without any modifications.
     */
    public GitHubRepositoryLinkRecord withUpdateFailed(String message) {
        return this;
    }
}
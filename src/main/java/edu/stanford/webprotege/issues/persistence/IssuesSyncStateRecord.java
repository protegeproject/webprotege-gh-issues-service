package edu.stanford.webprotege.issues.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "IssuesSyncState")
public record IssuesSyncStateRecord(
        @Id @JsonProperty("projectId") @Nonnull ProjectId projectId,
        @JsonProperty("repoCoords") @Nonnull GitHubRepositoryCoordinates repoCoords,
        @JsonProperty("updatedAt") @Nullable Instant updatedAt,
        @JsonProperty("syncState") IssuesSyncState syncState) {

    /**
     * Constructs a new GitHubLinkRecord.
     *
     * @param projectId The project ID.
     * @param repoCoords The GitHub repository coordinates.
     * @param updatedAt The last update timestamp, or null if not available.
     * @param syncState Indicates the sync state of the issues
     */
    @JsonCreator
    public IssuesSyncStateRecord(
            @JsonProperty("projectId")
            @Nonnull ProjectId projectId,
            @JsonProperty("repoCoords")
            @Nonnull GitHubRepositoryCoordinates repoCoords,
            @JsonProperty("updatedAt")
            @Nullable Instant updatedAt,
            @JsonProperty("syncState")
            IssuesSyncState syncState) {
        this.projectId = Objects.requireNonNull(projectId);
        this.repoCoords = Objects.requireNonNull(repoCoords);
        this.updatedAt = Objects.requireNonNullElse(updatedAt, Instant.EPOCH);
        this.syncState = syncState;
    }

    /**
     * Create a new GitHubLinkRecord with default values.
     *
     * @param projectId The project ID.
     * @param repoCoords The GitHub repository coordinates.
     * @return A new GitHubLinkRecord with default values and the given project ID and repository coordinates.
     */
    @Nonnull
    public static IssuesSyncStateRecord of(@Nonnull ProjectId projectId, @Nonnull GitHubRepositoryCoordinates repoCoords) {
        return new IssuesSyncStateRecord(projectId, repoCoords, null, IssuesSyncState.NOT_SYNCED);
    }

    /**
     * Create a new GitHubLinkRecord with updated status information.
     *
     * @param syncState The sync state for the issues
     * @return A new GitHubLinkRecord with the same project ID and repository coordinates but with updated status.
     */
    @Nonnull
    public IssuesSyncStateRecord withSyncState(IssuesSyncState syncState) {
        return new IssuesSyncStateRecord(this.projectId, this.repoCoords, updatedAt, syncState);
    }


    /**
     * Create a new GitHubLinkRecord with a failed update message.
     *
     * This method is used when an update fails, and the original record is retained without changes.
     *
     * @param message The message indicating the reason for the failed update.
     * @return The current GitHubLinkRecord without any modifications.
     */
    public IssuesSyncStateRecord withUpdateFailed(String message) {
        return this;
    }
}
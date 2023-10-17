package edu.stanford.protege.issues.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 */
public record ProjectGitHubLinkRecord(@Id @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                      @JsonProperty("repoCoords") @Nonnull GitHubRepositoryCoordinates repoCoords,
                                      @JsonProperty("updatedAt") @Nullable Instant updatedAt,
                                      @JsonProperty("updateRequired") boolean updateRequired) {

    @JsonCreator
    public ProjectGitHubLinkRecord(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                   @JsonProperty("repoCoords") @Nonnull GitHubRepositoryCoordinates repoCoords,
                                   @JsonProperty("updatedAt") @Nullable Instant updatedAt,
                                   @JsonProperty("updateRequired") boolean updateRequired) {
        this.projectId = Objects.requireNonNull(projectId);
        this.repoCoords = Objects.requireNonNull(repoCoords);
        this.updatedAt = Objects.requireNonNullElse(updatedAt, Instant.EPOCH);
        this.updateRequired = updateRequired;
    }

    @Nonnull
    public static ProjectGitHubLinkRecord of(@Nonnull ProjectId projectId,
                                      @Nonnull GitHubRepositoryCoordinates repoCoords) {
        return new ProjectGitHubLinkRecord(projectId, repoCoords, null, true);
    }

    @Nonnull
    public ProjectGitHubLinkRecord withUpdatedStatus(@Nonnull Instant updatedAt, boolean updateRequired) {
        return new ProjectGitHubLinkRecord(this.projectId,
                                           this.repoCoords,
                                           updatedAt,
                                           updateRequired);
    }

    @Nonnull
    public ProjectGitHubLinkRecord withUpdateRequired(boolean updateRequired) {
        return new ProjectGitHubLinkRecord(this.projectId,
                                           this.repoCoords,
                                           this.updatedAt,
                                           updateRequired);
    }

    public ProjectGitHubLinkRecord withUpdateFailed(String message) {
        return this;
    }
}

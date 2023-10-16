package edu.stanford.protege.issues.service;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-21
 *
 * Constructs a new GitHubRepositoryCoordinates object with the specified organization and repository names.
 *
 * @param organizationName The name of the GitHub organization to which the repository belongs. Must not be null.
 * @param repositoryName The name of the GitHub repository. Must not be null.
 *
 */
public record GitHubRepositoryCoordinates(@Nonnull String organizationName,
                                          @Nonnull String repositoryName) {

    public GitHubRepositoryCoordinates(@Nonnull String organizationName, @Nonnull String repositoryName) {
        this.organizationName = Objects.requireNonNull(organizationName);
        this.repositoryName = Objects.requireNonNull(repositoryName);
    }

    @Nonnull
    public static GitHubRepositoryCoordinates of(@Nonnull String organizationName,
                                                 @Nonnull String repoName) {
        return new GitHubRepositoryCoordinates(organizationName, repoName);
    }

    /**
     * Gets the full name of the GitHub repository in the format "organizationName/repositoryName."
     *
     * @return The full name of the GitHub repository.
     */
    public String getName() {
        return organizationName() + "/" + repositoryName();
    }
}

package edu.stanford.protege.issues.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-21
 *
 * Constructs a new GitHubRepositoryCoordinates object with the specified owner and repository names.
 *
 * @param ownerName The name of the owner (organization or individual name) to which the repository belongs. Must not be null.
 * @param repositoryName The name of the GitHub repository. Must not be null.
 *
 */
public record GitHubRepositoryCoordinates(@JsonProperty("ownerName") @Nonnull String ownerName,
                                          @JsonProperty("repositoryName") @Nonnull String repositoryName) {

    public static final Pattern NAME_PATTERN = Pattern.compile("[A-Za-z0-9_.-]");

    public static final Pattern FULL_NAME_PATTERN = Pattern.compile("^(" + NAME_PATTERN.pattern() + ")/(" + NAME_PATTERN.pattern() + ")$");

    public GitHubRepositoryCoordinates(@Nonnull String ownerName, @Nonnull String repositoryName) {
        this.ownerName = Objects.requireNonNull(ownerName);
        this.repositoryName = Objects.requireNonNull(repositoryName);
    }

    @Nonnull
    public static GitHubRepositoryCoordinates of(@Nonnull String organizationName,
                                                 @Nonnull String repoName) {
        return new GitHubRepositoryCoordinates(organizationName, repoName);
    }

    public static GitHubRepositoryCoordinates fromFullName(@Nonnull String fullName) {
        Objects.requireNonNull(fullName);
        var fullNameMatcher = FULL_NAME_PATTERN.matcher(fullName);
        if(!fullNameMatcher.matches()) {
            throw new IllegalArgumentException("Invalid full name");
        }
        return GitHubRepositoryCoordinates.of(fullNameMatcher.group(1), fullNameMatcher.group(2));
    }

    /**
     * Gets the full name of the GitHub repository in the format "ownerName/repositoryName."
     *
     * @return The full name of the GitHub repository.
     */
    public String getFullName() {
        return ownerName() + "/" + repositoryName();
    }
}

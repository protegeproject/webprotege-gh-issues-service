package edu.stanford.protege.issues.service;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.github.issues.GitHubIssue;
import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-21
 */
@Document(collection = "GitHubIssueRecords")
@JsonIgnoreProperties(ignoreUnknown = true)
public record IssueRecord(@JsonProperty("_id") @Id Long id,
                          @JsonProperty("repoCoords") GitHubRepositoryCoordinates repoCoords,
                          @JsonProperty("issue") @Nonnull GitHubIssue issue,
                          @JsonProperty("oboIds") @Nonnull Set<OboId> oboIds,
                          @JsonProperty("iris") @Nonnull Set<Iri> iris) {

    public IssueRecord(@JsonProperty("_id") Long id,
                       @JsonProperty("repoCoords") GitHubRepositoryCoordinates repoCoords,
                       @JsonProperty("issue") @Nonnull GitHubIssue issue,
                       @JsonProperty("oboIds") @Nonnull Set<OboId> oboIds,
                       @JsonProperty("iris") @Nonnull Set<Iri> iris) {
        this.id = Objects.requireNonNull(id);
        this.repoCoords = Objects.requireNonNull(repoCoords);
        this.issue = Objects.requireNonNull(issue);
        this.oboIds = Objects.requireNonNull(oboIds);
        this.iris = Objects.requireNonNull(iris);
        if(!id.equals(issue.id())) {
            throw new IllegalArgumentException("_id and issue.id must be equal");
        }
    }

    @Nonnull
    public static IssueRecord of(@JsonProperty("issue") @Nonnull GitHubIssue issue,
                                 @JsonProperty("repoCoords") GitHubRepositoryCoordinates repoCoords,
                                 @JsonProperty("oboIds") @Nonnull Set<OboId> oboIds,
                                 @JsonProperty("iris") @Nonnull Set<Iri> iris) {
        return new IssueRecord(issue.id(), repoCoords, issue, oboIds, iris);
    }

    @JsonCreator
    @Nonnull
    static IssueRecord fromJson(@JsonProperty("_id") String issueNodeId,
                                @JsonProperty("repoCoords") GitHubRepositoryCoordinates repoCoords,
                                @JsonProperty("issue") @Nonnull GitHubIssue issue,
                                 @JsonProperty("oboIds") @Nonnull Set<OboId> oboIds,
                                 @JsonProperty("iris") @Nonnull Set<Iri> iris) {
        return new IssueRecord(issue.id(), repoCoords, issue, oboIds, iris);
    }
}

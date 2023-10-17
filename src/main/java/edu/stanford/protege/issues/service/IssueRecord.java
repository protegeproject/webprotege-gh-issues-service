package edu.stanford.protege.issues.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.issues.shared.GitHubIssue;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.data.annotation.Id;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-21
 */
public record IssueRecord(@JsonProperty("_id") @Id String nodeId,
                          @JsonProperty("repoCoords") GitHubRepositoryCoordinates repoCoords,
                          @JsonProperty("issue") @Nonnull GitHubIssue issue,
                          @JsonProperty("oboIds") @Nonnull Set<OboId> oboIds,
                          @JsonProperty("iris") @Nonnull Set<Iri> iris) {

    public IssueRecord(@JsonProperty("_id") String nodeId,
                       @JsonProperty("repoCoords") GitHubRepositoryCoordinates repoCoords,
                       @JsonProperty("issue") @Nonnull GitHubIssue issue,
                       @JsonProperty("oboIds") @Nonnull Set<OboId> oboIds,
                       @JsonProperty("iris") @Nonnull Set<Iri> iris) {
        this.nodeId = Objects.requireNonNull(nodeId);
        this.repoCoords = Objects.requireNonNull(repoCoords);
        this.issue = Objects.requireNonNull(issue);
        this.oboIds = Objects.requireNonNull(oboIds);
        this.iris = Objects.requireNonNull(iris);
        if(!nodeId.equals(issue.nodeId())) {
            throw new IllegalArgumentException("nodeId and issue.nodeId must be equal");
        }
    }

    @Nonnull
    public static IssueRecord of(@JsonProperty("issue") @Nonnull GitHubIssue issue,
                                 @JsonProperty("repoCoords") GitHubRepositoryCoordinates repoCoords,
                                 @JsonProperty("oboIds") @Nonnull Set<OboId> oboIds,
                                 @JsonProperty("iris") @Nonnull Set<Iri> iris) {
        return new IssueRecord(issue.nodeId(), repoCoords, issue, oboIds, iris);
    }

    @JsonCreator
    @Nonnull
    static IssueRecord fromJson(@JsonProperty("_id") String issueNodeId,
                                @JsonProperty("repoCoords") GitHubRepositoryCoordinates repoCoords,
                                @JsonProperty("issue") @Nonnull GitHubIssue issue,
                                 @JsonProperty("oboIds") @Nonnull Set<OboId> oboIds,
                                 @JsonProperty("iris") @Nonnull Set<Iri> iris) {
        return new IssueRecord(issueNodeId, repoCoords, issue, oboIds, iris);
    }
}

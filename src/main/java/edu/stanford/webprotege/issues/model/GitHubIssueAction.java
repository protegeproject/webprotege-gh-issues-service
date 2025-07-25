package edu.stanford.webprotege.issues.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the type of action performed on a GitHub issue.
 */
public enum GitHubIssueAction {

    /** The issue was opened. */
    @JsonProperty("opened")
    OPENED,

    /** The issue was edited. */
    @JsonProperty("edited")
    EDITED,

    /** The issue was deleted. */
    @JsonProperty("deleted")
    DELETED,

    /** The issue was closed. */
    @JsonProperty("closed")
    CLOSED,

    /** The issue was reopened. */
    @JsonProperty("reopened")
    REOPENED,

    /** A user was assigned to the issue. */
    @JsonProperty("assigned")
    ASSIGNED,

    /** A user was unassigned from the issue. */
    @JsonProperty("unassigned")
    UNASSIGNED,

    /** A label was added to the issue. */
    @JsonProperty("labeled")
    LABELED,

    /** A label was removed from the issue. */
    @JsonProperty("unlabeled")
    UNLABELED,

    /** The issue was locked. */
    @JsonProperty("locked")
    LOCKED,

    /** The issue was unlocked. */
    @JsonProperty("unlocked")
    UNLOCKED,

    /** The issue was transferred to another repository. */
    @JsonProperty("transferred")
    TRANSFERRED,

    /** The issue was pinned. */
    @JsonProperty("pinned")
    PINNED,

    /** The issue was unpinned. */
    @JsonProperty("unpinned")
    UNPINNED,

    /** A milestone was added to the issue. */
    @JsonProperty("milestoned")
    MILESTONED,

    /** A milestone was removed from the issue. */
    @JsonProperty("demilestoned")
    DEMILESTONED;

    /**
     * Fallback creator to deserialize a GitHub issue action value into an enum constant.
     *
     * @param value the string from the webhook payload
     * @return the matching enum value
     * @throws IllegalArgumentException if no match is found
     */
    @JsonCreator
    public static GitHubIssueAction fromString(String value) {
        for (GitHubIssueAction action : GitHubIssueAction.values()) {
            JsonProperty annotation = null;
            try {
                annotation = GitHubIssueAction.class
                        .getField(action.name())
                        .getAnnotation(JsonProperty.class);
            } catch (NoSuchFieldException ignored) {}

            if (annotation != null && annotation.value().equals(value)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Unknown GitHub issue action: " + value);
    }
}
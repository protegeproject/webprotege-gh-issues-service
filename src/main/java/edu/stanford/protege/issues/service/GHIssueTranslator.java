package edu.stanford.protege.issues.service;

import com.google.common.collect.HashMultiset;
import edu.stanford.protege.issues.shared.*;
import org.kohsuke.github.GHIssue;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-18
 */
@Component
public class GHIssueTranslator {

    private final GHUserTranslator userTranslator;

    private final GHLabelTranslator labelTranslator;

    private final GHMilestoneTranslator milestoneTranslator;

    private final GHReactionTranslator reactionTranslator;

    public GHIssueTranslator(GHUserTranslator userTranslator, GHLabelTranslator labelTranslator,
                             GHMilestoneTranslator milestoneTranslator, GHReactionTranslator reactionTranslator) {
        this.userTranslator = userTranslator;
        this.labelTranslator = labelTranslator;
        this.milestoneTranslator = milestoneTranslator;
        this.reactionTranslator = reactionTranslator;
    }

    @Nonnull
    public GitHubIssue translate(@Nonnull GHIssue issue) {
        return GitHubIssue.get(
                issue.getUrl().toString(),
                issue.getId(),
                issue.getNodeId(),
                issue.getNumber(),
                issue.getTitle(),
                getUser(issue),
                getLabels(issue),
                issue.getHtmlUrl().toString(),
                getState(issue),
                issue.isLocked(),
                getAssignee(issue),
                getAssignees(issue),
                milestoneTranslator.translate(issue.getMilestone()),
                issue.getCommentsCount(),
                getCreatedAt(issue),
                getUpdatedAt(issue),
                getClosedAt(issue),
                getClosedBy(issue),
                GitHubAuthorAssociation.NONE,
                "",
                issue.getBody(),
                getReactions(issue),
                null);
    }

    private GitHubState getState(GHIssue issue) {
        return switch (issue.getState()) {
            case ALL -> GitHubState.OPEN;
            case CLOSED -> GitHubState.CLOSED;
            case OPEN -> GitHubState.OPEN;
        };
    }

    private static Instant getClosedAt(GHIssue issue) {
        return issue.getClosedAt().toInstant();
    }

    private static Instant getUpdatedAt(GHIssue issue) {
        try {
            return issue.getUpdatedAt().toInstant();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Instant getCreatedAt(GHIssue issue) {
        try {
            return issue.getCreatedAt().toInstant();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private GitHubUser getClosedBy(GHIssue issue) {
        try {
            return userTranslator.translate(issue.getClosedBy());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Nonnull
    private GitHubReactions getReactions(@Nonnull GHIssue issue) {
        try {
            var reactions = issue.listReactions().toList();
            return reactionTranslator.translate(reactions);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    private List<GitHubLabel> getLabels(GHIssue issue) {
        return issue.getLabels().stream().map(labelTranslator::translate).toList();
    }

    private GitHubUser getAssignee(GHIssue issue) {
        try {
            return userTranslator.translate(issue.getAssignee());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<GitHubUser> getAssignees(GHIssue issue) {
        return issue.getAssignees().stream().map(userTranslator::translate).toList();
    }

    private GitHubUser getUser(GHIssue issue) {
        try {
            return userTranslator.translate(issue.getUser());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

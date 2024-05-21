package edu.stanford.protege.issues.service;

import edu.stanford.protege.github.GitHubUser;
import edu.stanford.protege.github.issues.*;
import org.kohsuke.github.GHIssue;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
                getUser(issue).orElse(null),
                getLabels(issue),
                issue.getHtmlUrl().toString(),
                getState(issue),
                issue.isLocked(),
                getAssignee(issue).orElse(null),
                getAssignees(issue),
                milestoneTranslator.translate(issue.getMilestone()).orElse(null),
                issue.getCommentsCount(),
                getCreatedAt(issue),
                getUpdatedAt(issue),
                getClosedAt(issue),
                getClosedBy(issue).orElse(null),
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
        return Optional.ofNullable(issue.getClosedAt()).map(Date::toInstant).orElse(null);
    }

    private static Instant getUpdatedAt(GHIssue issue) {
        try {
            return Optional.ofNullable(issue.getUpdatedAt()).map(Date::toInstant).orElse(null);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Instant getCreatedAt(GHIssue issue) {
        try {
            return Optional.ofNullable(issue.getCreatedAt()).map(Date::toInstant).orElse(null);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Optional<GitHubUser> getClosedBy(GHIssue issue) {
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

    private Optional<GitHubUser> getAssignee(GHIssue issue) {
        try {
            return userTranslator.translate(issue.getAssignee());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<GitHubUser> getAssignees(GHIssue issue) {
        return issue.getAssignees().stream().map(userTranslator::translate).filter(Optional::isPresent).map(Optional::get).toList();
    }

    private Optional<GitHubUser> getUser(GHIssue issue) {
        try {
            return userTranslator.translate(issue.getUser());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

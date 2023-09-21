package edu.stanford.protege.issues.service;

import edu.stanford.protege.issues.shared.GitHubMilestone;
import edu.stanford.protege.issues.shared.GitHubState;
import edu.stanford.protege.issues.shared.GitHubUser;
import org.kohsuke.github.GHMilestone;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-18
 */
@Component
public class GHMilestoneTranslator {

    private final GHUserTranslator userTranslator;

    public GHMilestoneTranslator(GHUserTranslator userTranslator) {
        this.userTranslator = userTranslator;
    }

    @Nonnull
    public GitHubMilestone translate(@Nonnull GHMilestone milestone) {
        return GitHubMilestone.get(
                milestone.getUrl().toString(),
                milestone.getId(),
                milestone.getNodeId(),
                milestone.getNumber(),
                milestone.getTitle(),
                milestone.getDescription(),
                getCreator(milestone),
                milestone.getOpenIssues(),
                milestone.getClosedIssues(), getState(milestone), getCreatedAt(milestone), getUpdatedAt(milestone),
                milestone.getDueOn().toInstant(), getClosedAt(milestone)
        );
    }

    private static Instant getClosedAt(GHMilestone milestone) {
        try {
            return milestone.getClosedAt().toInstant();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Instant getUpdatedAt(GHMilestone milestone) {
        try {
            return milestone.getUpdatedAt().toInstant();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Instant getCreatedAt(GHMilestone milestone) {
        try {
            return milestone.getCreatedAt().toInstant();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static GitHubState getState(GHMilestone milestone) {
        return switch (milestone.getState()) {
            case OPEN -> GitHubState.OPEN;
            case CLOSED -> GitHubState.CLOSED;
        };
    }

    private GitHubUser getCreator(GHMilestone milestone) {
        try {
            return userTranslator.translate(milestone.getCreator());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

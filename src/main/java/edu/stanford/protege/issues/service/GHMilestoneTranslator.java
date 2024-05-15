package edu.stanford.protege.issues.service;

import edu.stanford.protege.github.issues.shared.GitHubMilestone;
import edu.stanford.protege.github.issues.shared.GitHubState;
import edu.stanford.protege.github.shared.GitHubUser;
import org.kohsuke.github.GHMilestone;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

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
    public Optional<GitHubMilestone> translate(@Nullable GHMilestone milestone) {
        if(milestone ==  null) {
            return Optional.empty();
        }
        var translated = GitHubMilestone.get(
                milestone.getUrl().toString(),
                milestone.getId(),
                milestone.getNodeId(),
                milestone.getNumber(),
                milestone.getTitle(),
                milestone.getDescription(),
                getCreator(milestone).orElse(null),
                milestone.getOpenIssues(),
                milestone.getClosedIssues(), getState(milestone), getCreatedAt(milestone), getUpdatedAt(milestone),
                milestone.getDueOn().toInstant(), getClosedAt(milestone)
        );
        return Optional.of(translated);
    }

    private static Instant getClosedAt(GHMilestone milestone) {
        try {
            return Optional.ofNullable(milestone.getClosedAt()).map(Date::toInstant).orElse(null);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Instant getUpdatedAt(GHMilestone milestone) {
        try {
            return Optional.ofNullable(milestone.getUpdatedAt()).map(Date::toInstant).orElse(null);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Instant getCreatedAt(GHMilestone milestone) {
        try {
            return Optional.ofNullable(milestone.getCreatedAt()).map(Date::toInstant).orElse(null);
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

    private Optional<GitHubUser> getCreator(GHMilestone milestone) {
        try {
            return userTranslator.translate(milestone.getCreator());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

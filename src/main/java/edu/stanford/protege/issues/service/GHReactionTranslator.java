package edu.stanford.protege.issues.service;

import com.google.common.collect.HashMultiset;
import edu.stanford.protege.issues.shared.GitHubReaction;
import edu.stanford.protege.issues.shared.GitHubReactions;
import org.kohsuke.github.GHReaction;
import org.kohsuke.github.ReactionContent;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-18
 */
@Component
public class GHReactionTranslator {

    @Nonnull
    public GitHubReactions translate(@Nonnull List<GHReaction> reactions) {
        var multiMap = HashMultiset.<GitHubReaction>create();
        reactions.forEach(r -> {
            multiMap.add(getReaction(r.getContent()));
        });
        return GitHubReactions.get("", multiMap.size(), multiMap.count(GitHubReaction.PLUS_1),
                                   multiMap.count(GitHubReaction.MINUS_1),
                                   multiMap.count(GitHubReaction.LAUGH),
                                   multiMap.count(GitHubReaction.HOORAY),
                                   multiMap.count(GitHubReaction.CONFUSED),
                                   multiMap.count(GitHubReaction.HEART),
                                   multiMap.count(GitHubReaction.ROCKET),
                                   multiMap.count(GitHubReaction.EYES));
    }

    private GitHubReaction getReaction(ReactionContent reactionContent) {
        return switch (reactionContent) {
            case PLUS_ONE -> GitHubReaction.PLUS_1;
            case MINUS_ONE -> GitHubReaction.MINUS_1;
            case LAUGH -> GitHubReaction.LAUGH;
            case CONFUSED -> GitHubReaction.CONFUSED;
            case HEART -> GitHubReaction.HEART;
            case HOORAY -> GitHubReaction.HOORAY;
            case ROCKET -> GitHubReaction.ROCKET;
            case EYES -> GitHubReaction.EYES;
        };
    }

}

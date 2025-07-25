package edu.stanford.webprotege.issues.translator;

import edu.stanford.protege.github.GitHubUser;
import edu.stanford.protege.github.GitHubUserType;
import org.kohsuke.github.GHUser;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-18
 */
@Component
public class GHUserTranslator {

    @Nonnull
    public Optional<GitHubUser> translate(@Nullable GHUser user) {
        if(user == null) {
            return Optional.empty();
        }
        var translated = GitHubUser.get(
                user.getLogin(),
                user.getId(),
                user.getNodeId(),
                user.getAvatarUrl(),
                user.getUrl().toString(),
                user.getHtmlUrl().toString(),
                GitHubUserType.get(getType(user)),
                isSiteAdmin(user)
        );
        return Optional.of(translated);
    }

    private static boolean isSiteAdmin(GHUser user) {
        try {
            return user.isSiteAdmin();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String getType(GHUser user) {
        try {
            return user.getType();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

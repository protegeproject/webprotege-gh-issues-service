package edu.stanford.protege.issues.service;

import edu.stanford.protege.issues.shared.GitHubUser;
import edu.stanford.protege.issues.shared.GitHubUserType;
import org.kohsuke.github.GHUser;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-18
 */
@Component
public class GHUserTranslator {

    @Nonnull
    public GitHubUser translate(@Nonnull GHUser user) {
        return GitHubUser.get(
                user.getLogin(),
                user.getId(),
                user.getNodeId(),
                user.getAvatarUrl(),
                user.getUrl().toString(),
                user.getHtmlUrl().toString(),
                GitHubUserType.get(getType(user)),
                isSiteAdmin(user)
        );
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

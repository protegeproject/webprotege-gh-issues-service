package edu.stanford.webprotege.issues.auth;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import edu.stanford.webprotege.issues.model.GitHubInstallationId;

public class GitHubInstallationTokenCacheProvider {

    public Cache<GitHubInstallationId, GitHubInstallationToken> createTokenCache() {
        return Caffeine.newBuilder()
                .expireAfter(new GitHubInstallationTokenExpiry())
                .build();
    }
}

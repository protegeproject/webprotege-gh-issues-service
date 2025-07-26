package edu.stanford.webprotege.issues.auth;

import com.github.benmanes.caffeine.cache.Expiry;
import edu.stanford.webprotege.issues.model.GitHubInstallationId;

import java.time.Duration;
import java.time.Instant;

public class GitHubInstallationTokenExpiry implements Expiry<GitHubInstallationId, GitHubInstallationToken> {

    @Override
    public long expireAfterCreate(GitHubInstallationId key, GitHubInstallationToken token, long currentTimeNanos) {
        Instant now = Instant.now();
        Duration untilExpiry = Duration.between(now, token.expiresAt().minusSeconds(5));
        long durationNanos = untilExpiry.toNanos();
        return Math.max(durationNanos, 0);
    }

    @Override
    public long expireAfterUpdate(GitHubInstallationId key, GitHubInstallationToken token, long currentTimeNanos, long currentDurationNanos) {
        return currentDurationNanos;
    }

    @Override
    public long expireAfterRead(GitHubInstallationId key, GitHubInstallationToken token, long currentTimeNanos, long currentDurationNanos) {
        return currentDurationNanos;
    }
}

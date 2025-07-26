package edu.stanford.webprotege.issues.auth;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import edu.stanford.webprotege.issues.model.GitHubInstallationId;

@Configuration
public class GitHubAuthConfig {

    @Bean
    public WebClient githubWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.github.com" )
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json" )
                .build();
    }

    @Bean
    GitHubInstallationTokenCacheProvider gitHubInstallationTokenCacheProvider() {
        return new GitHubInstallationTokenCacheProvider();
    }

    @Bean
    public Cache<GitHubInstallationId, GitHubInstallationToken> tokenCache(GitHubInstallationTokenCacheProvider tokenCacheProvider) {
        return tokenCacheProvider.createTokenCache();
    }

}

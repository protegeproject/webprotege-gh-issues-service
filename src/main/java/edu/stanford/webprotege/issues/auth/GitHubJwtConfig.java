package edu.stanford.webprotege.issues.auth;


import edu.stanford.webprotege.issues.model.GitHubAppId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitHubJwtConfig {

    public static final Logger logger = LoggerFactory.getLogger(GitHubJwtConfig.class);

    @Bean
    public GitHubPrivateKeyLoader gitHubPrivateKeyLoader(@Value("${webprotege.github.pem-path:}") String pemPath) throws Exception {
        if(pemPath.isEmpty()) {
            logger.warn("Missing required property: webprotege.github.pem-path");
        }
        return new GitHubPrivateKeyLoader(pemPath);
    }

    /**
     * Supplies a new short-lived JWT token (valid for 9 minutes) signed with GitHub App private key.
     */
    @Bean
    public GitHubJwtFactory githubJwtSupplier(GitHubAppId appId,
                                              GitHubPrivateKeyLoader privateKeyLoader) {
        return new GitHubJwtFactory(appId, privateKeyLoader);
    }

    @Bean
    GitHubAppId gitHubAppId(@Value("${webprotege.github.app-id}") String appId) {
        return new GitHubAppId(appId);
    }
}

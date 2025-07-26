package edu.stanford.webprotege.issues.auth;

import edu.stanford.webprotege.issues.model.GitHubAppId;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class GitHubJwtFactory {

    private final GitHubAppId appId;

    private final GitHubPrivateKeyLoader privateKeyLoader;

    public GitHubJwtFactory(GitHubAppId appId,
                            GitHubPrivateKeyLoader privateKeyLoader) {
        this.appId = appId;
        this.privateKeyLoader = privateKeyLoader;
    }

    public GitHubJwt getJwt() {
        var now = Instant.now();
        var privateKey = privateKeyLoader.getPrivateKey();
        var token = Jwts.builder().issuer(appId.id())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(10, ChronoUnit.MINUTES)))  // GitHub max is 10 minutes
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
        return new GitHubJwt(token);
    }

}

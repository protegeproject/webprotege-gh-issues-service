package edu.stanford.protege.issues.service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-23
 */
public class AuthUtil {

    private static final Logger logger = LoggerFactory.getLogger(AuthUtil.class);

    protected static final String RSA_ALGORITHM = "RSA";

    static PrivateKey get(Path pathToPrivateKeyDerFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(pathToPrivateKeyDerFile);
        logger.info("Read private key file: {} bytes", keyBytes.length);
        var spec = new PKCS8EncodedKeySpec(keyBytes);
        var kf = KeyFactory.getInstance(RSA_ALGORITHM);
        return kf.generatePrivate(spec);
    }

    static String createJWT(String githubAppId, long ttlMillis) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        //The JWT signature algorithm we will be using to sign the token
        var signatureAlgorithm = SignatureAlgorithm.RS256;

        long nowMillis = System.currentTimeMillis();
        var now = new Date(nowMillis);

        //We will sign our JWT with our private key
        var pathToPrivateKeyDerFile = Path.of("/run/secrets/gh_private_key");

        Key signingKey = get(pathToPrivateKeyDerFile);

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                                 .setIssuedAt(now)
                                 .setIssuer(githubAppId)
                                 .signWith(signingKey, signatureAlgorithm);

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
//
//    public static void main(String[] args) throws Exception {
//        String jwtToken = createJWT("44435", 600000); //sdk-github-api-app-test
//        GitHub gitHubApp = new GitHubBuilder().withJwtToken(jwtToken).build();
//    }

}

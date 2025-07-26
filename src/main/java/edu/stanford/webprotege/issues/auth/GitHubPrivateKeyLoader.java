package edu.stanford.webprotege.issues.auth;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;

public class GitHubPrivateKeyLoader {

    private final String pemPath;

    public GitHubPrivateKeyLoader(String pemPath) {
        this.pemPath = pemPath;
    }

    public PrivateKey getPrivateKey() {
        if(pemPath.isEmpty()) {
            throw new IllegalStateException("Path to .pem file is empty.  Make sure that the path to .pem file is specified in the webprotege.github.pem-path property.");
        }
        try (var pemReader = new PemReader(new FileReader(pemPath))) {
            var content = pemReader.readPemObject().getContent();
            var rsa = RSAPrivateKey.getInstance(ASN1Sequence.getInstance(content));

            var keySpec = new RSAPrivateCrtKeySpec(
                    rsa.getModulus(),
                    rsa.getPublicExponent(),
                    rsa.getPrivateExponent(),
                    rsa.getPrime1(),
                    rsa.getPrime2(),
                    rsa.getExponent1(),
                    rsa.getExponent2(),
                    rsa.getCoefficient()
            );
            var keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}


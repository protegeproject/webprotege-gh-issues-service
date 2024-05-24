package edu.stanford.protege.issues.service;

import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.WebProtegeIpcApplication;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static edu.stanford.protege.issues.service.AuthUtil.createJWT;

@SpringBootApplication
@Import({WebProtegeIpcApplication.class, WebProtegeJacksonApplication.class})
public class WebprotegeGhIssuesServiceApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(WebprotegeGhIssuesServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WebprotegeGhIssuesServiceApplication.class, args);
    }

    @Bean
    public GitHub gitHub(@Value("${gh.app.id}") String gitHubAppId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    var envMap = System.getenv();
    logger.info("Env: " + envMap);
    logger.info("Creating GitHub client");
        var jwtToken = createJWT(gitHubAppId, 600000); //sdk-github-api-app-test
        logger.info("Created JWT for GitHub");
        return new GitHubBuilder().withJwtToken(jwtToken).build();
    }

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        logger.warn("Forcing linked repo");
        var bean = context.getBean(GitHubRepositoryLinkRecordStore.class);
        bean
                .save(new GitHubRepositoryLinkRecord(ProjectId.valueOf("c6a5fed1-47eb-4be1-9570-7d3eefd9b579"), new GitHubRepositoryCoordinates("matthewhorridge", "testrepo"), null, true));

        bean
                .save(new GitHubRepositoryLinkRecord(ProjectId.valueOf("e1e130b0-388e-490d-99f6-1aeda4a926f3"), new GitHubRepositoryCoordinates("geneontology", "go-ontology"), null, true));


    }
}

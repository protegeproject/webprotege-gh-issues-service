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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.io.IOException;

@SpringBootApplication
@Import({WebProtegeIpcApplication.class, WebProtegeJacksonApplication.class})
public class WebprotegeGhIssuesServiceApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(WebprotegeGhIssuesServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WebprotegeGhIssuesServiceApplication.class, args);
    }

    @Bean
    public GitHub gitHub() throws IOException {
        return GitHubBuilder.fromEnvironment().build();
    }

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        logger.warn("Forcing linked repo");
        context.getBean(GitHubRepositoryLinkRecordStore.class)
                .save(new GitHubRepositoryLinkRecord(ProjectId.valueOf("c6a5fed1-47eb-4be1-9570-7d3eefd9b579"), new GitHubRepositoryCoordinates("matthewhorridge", "testrepo"), null, true));
    }
}

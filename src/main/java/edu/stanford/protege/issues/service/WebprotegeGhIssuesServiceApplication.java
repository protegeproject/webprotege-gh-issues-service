package edu.stanford.protege.issues.service;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.io.IOException;

@SpringBootApplication
public class WebprotegeGhIssuesServiceApplication implements CommandLineRunner {

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
    }
}

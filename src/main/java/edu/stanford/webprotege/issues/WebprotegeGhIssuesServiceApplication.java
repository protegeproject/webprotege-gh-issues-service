package edu.stanford.webprotege.issues;

import edu.stanford.protege.webprotege.ipc.WebProtegeIpcApplication;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.io.IOException;

@SpringBootApplication
@Import({WebProtegeIpcApplication.class, WebProtegeJacksonApplication.class})
public class WebprotegeGhIssuesServiceApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(WebprotegeGhIssuesServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WebprotegeGhIssuesServiceApplication.class, args);
    }

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
    }
}

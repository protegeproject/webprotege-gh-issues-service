package edu.stanford.protege.issues.service;

import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-16
 */
@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@ExtendWith(MongoTestExtension.class)
public class IT_LocalIssueStoreLoader {

    @Autowired
    private LocalIssueStore localIssueStore;

    @Autowired
    private LocalIssueStoreLoader localIssueStoreLoader;

    private static GitHubRepositoryCoordinates repoCoords = GitHubRepositoryCoordinates.of("protegeproject", "webprotege-gh-issues-test-repo");

    @BeforeEach
    public void setUp() throws IOException {
        localIssueStoreLoader.load(repoCoords);
    }

    @Test
    void shouldLoadIssuesFromGitHubRepository() {
        assertThat(localIssueStore.count()).isGreaterThan(0);
    }

    @Test
    void shouldLoadTestIssue() {
        assertThat(localIssueStore.findAll())
                .map(IssueRecord::issue)
                .anyMatch(issue -> issue.title().equals("Testing Issue #1"));
    }
}


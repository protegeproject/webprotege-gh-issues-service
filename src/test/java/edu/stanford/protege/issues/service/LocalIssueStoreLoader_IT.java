package edu.stanford.protege.issues.service;

import edu.stanford.protege.webprotege.common.ProjectId;
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
@SpringBootTest
@ExtendWith(MongoTestExtension.class)
public class LocalIssueStoreLoader_IT {

    @Autowired
    private LocalIssueStore localIssueStore;

    @Autowired
    private LocalIssueStoreLoader localIssueStoreLoader;

    private ProjectId projectId;

    @BeforeEach
    void setUp() {
        projectId = ProjectId.generate();
    }

    @Test
    void shouldLoadIssuesFromGitHubRepository() throws IOException {
        var repoCoords = GitHubRepositoryCoordinates.of("matthewhorridge", "T1");
        localIssueStoreLoader.load(projectId,
                                   repoCoords);
        var recordsCount = localIssueStore.count();
        assertThat(recordsCount).isEqualTo(2);
        localIssueStore.findAll()
                .forEach(issueRecord -> System.out.println(issueRecord));
    }
}


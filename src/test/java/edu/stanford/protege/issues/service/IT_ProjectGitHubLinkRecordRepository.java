package edu.stanford.protege.issues.service;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MongoTestExtension.class)
public class IT_ProjectGitHubLinkRecordRepository {

    @Autowired
    private ProjectGitHubLinkRecordRepository repository;

    private ProjectId projectId;

    private GitHubRepositoryCoordinates repoCoords;

    private Instant updatedAt;

    @BeforeEach
    void setUp() {
        projectId = ProjectId.generate();
        repoCoords = GitHubRepositoryCoordinates.of("TestOrg", "TestRepoName");
        updatedAt = Instant.now();
        repository.save(new ProjectGitHubLinkRecord(projectId, repoCoords, updatedAt, true));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveRecord() {
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void shouldFindByProjectId() {
        var found = repository.findById(projectId);
        assertThat(found).isPresent();
    }

    @Test
    void shouldFindByRepoCoords() {
        var found = repository.findAllByRepoCoords(repoCoords);
        assertThat(found).hasSize(1);
    }
}
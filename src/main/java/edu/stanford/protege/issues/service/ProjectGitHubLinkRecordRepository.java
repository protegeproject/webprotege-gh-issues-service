package edu.stanford.protege.issues.service;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-16
 */
@Repository
public interface ProjectGitHubLinkRecordRepository extends CrudRepository<ProjectGitHubLinkRecord, ProjectId> {

    @Nonnull
    List<ProjectGitHubLinkRecord> findAllByRepoCoords(@Nonnull GitHubRepositoryCoordinates repoCoords);

}

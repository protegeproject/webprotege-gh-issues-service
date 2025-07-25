package edu.stanford.webprotege.issues.persistence;

import edu.stanford.protege.github.GitHubRepositoryCoordinates;
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
public interface GitHubRepositoryLinkRecordStore extends CrudRepository<GitHubRepositoryLinkRecord, ProjectId> {

    @Nonnull
    List<GitHubRepositoryLinkRecord> findAllByRepoCoords(@Nonnull GitHubRepositoryCoordinates repoCoords);

}

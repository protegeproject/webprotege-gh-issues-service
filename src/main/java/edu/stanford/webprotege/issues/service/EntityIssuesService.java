package edu.stanford.webprotege.issues.service;

import edu.stanford.protege.github.issues.GitHubIssue;
import edu.stanford.webprotege.issues.persistence.*;
import edu.stanford.webprotege.issues.entity.Iri;
import edu.stanford.webprotege.issues.entity.OboIdUtilities;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Manages retrieval of GitHub issues that are linked to OWL entities in a given WebProtege project.
 * <p>
 * Issues may be linked to an entity either via:
 * <ul>
 *   <li>Direct IRI match</li>
 *   <li>OBO-style identifier derived from the entity IRI</li>
 * </ul>
 * This class ensures that the local GitHub issue store is up-to-date before attempting any lookups.
 *
 * @author Matthew Horridge<br>
 * Stanford Center for Biomedical Informatics Research<br>
 * @since 2023-09-15
 */
@Service
public class EntityIssuesService {

    private static final Logger logger = LoggerFactory.getLogger(EntityIssuesService.class);

    private final LocalIssueStoreManager localIssueStoreManager;

    private final LocalIssueStore localIssueStore;

    private final IssuesSyncStateRecordRepository linkRecordRepository;

    /**
     * Constructs a new {@link EntityIssuesService}.
     *
     * @param localIssueStoreManager The manager responsible for ensuring the local store is in sync with GitHub.
     * @param localIssueStore        The local issue store to query.
     * @param linkRecordRepository   The repository that stores GitHub repository links for projects.
     */
    public EntityIssuesService(LocalIssueStoreManager localIssueStoreManager,
                               LocalIssueStore localIssueStore,
                               IssuesSyncStateRecordRepository linkRecordRepository) {
        this.localIssueStoreManager = localIssueStoreManager;
        this.localIssueStore = localIssueStore;
        this.linkRecordRepository = linkRecordRepository;
    }

    /**
     * Retrieves a list of GitHub issues associated with a given OWL entity in a specified project.
     * <p>
     * Issues may be linked either directly by the entity's IRI or via an OBO-style identifier derived from it.
     * </p>
     *
     * @param projectId The project in which to look up linked GitHub issues.
     * @param entity    The OWL entity for which to retrieve associated issues.
     * @return A list of unique {@link GitHubIssue} instances linked to the entity, possibly empty.
     */
    @Nonnull
    public List<GitHubIssue> getIssues(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity) {
        // Ensure the local issue store reflects the latest GitHub state
        localIssueStoreManager.ensureLocalStoreIsUpToDate(projectId);

        // Attempt to retrieve the GitHub repository link for the given project
        return linkRecordRepository.findById(projectId)
                .map(IssuesSyncStateRecord::repoCoords)
                .map(repoCoord -> {
                    // Retrieve issues linked directly by IRI
                    var byIris = localIssueStore.findAllByRepoCoordsAndIris(repoCoord, Iri.valueOf(entity))
                            .stream()
                            .map(IssueRecord::issue);

                    // Retrieve issues linked by OBO ID, if one can be derived from the entity
                    var optOboId = OboIdUtilities.getOboIdFromEntity(entity);
                    var byOboIds = optOboId
                            .map(localIssueStore::findAllByOboIds)
                            .stream()
                            .flatMap(Collection::stream)
                            .map(IssueRecord::issue);

                    // Combine and deduplicate issues
                    return Stream.concat(byIris, byOboIds)
                            .distinct()
                            .toList();
                })
                .orElseGet(() -> {
                    logger.debug("No GitHub repository link found for project {}" , projectId);
                    return List.of();
                });
    }
}
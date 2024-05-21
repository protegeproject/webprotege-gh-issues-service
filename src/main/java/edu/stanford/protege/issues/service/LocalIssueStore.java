package edu.stanford.protege.issues.service;

import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-15
 * The LocalIssueStore interface represents a repository for locally storing and retrieving issue records.
 * It extends the `CrudRepository` interface and provides methods for managing and querying issue records.
 */
@Component
public interface LocalIssueStore extends CrudRepository<IssueRecord, String> {

    /**
     * Finds and returns a list of issue records associated with a specific IRI (Internationalized Resource Identifier).
     *
     * @param iri The IRI for which issue records are queried. Must not be null.
     * @return A list of issue records related to the specified IRI.
     */
    @Nonnull
    List<IssueRecord> findAllByIris(@Nonnull Iri iri);

    /**
     * Finds and returns a list of issue records associated with a specific OBO ID (Open Biological and Biomedical Ontologies Identifier).
     *
     * @param oboId The OBO ID for which issue records are queried. Must not be null.
     * @return A list of issue records related to the specified OBO ID.
     */
    @Nonnull
    List<IssueRecord> findAllByOboIds(@Nonnull OboId oboId);

    /**
     * Finds and returns a list of issue records associated with a specific GitHub repo and OBO ID.
     *
     * @param repoCoords The coordinates of the GitHub for which issue records are queried. Must not be null.
     * @param oboId The OBO ID for which issue records are queried. Must not be null.
     * @return A list of issue records that mention the specified project ID and OBO ID.
     */
    @Nonnull
    List<IssueRecord> findAllByRepoCoordsAndOboIds(@Nonnull GitHubRepositoryCoordinates repoCoords, @Nonnull OboId oboId);

    /**
     * Finds and returns a list of issue records associated with a specific project ID and IRI.
     *
     * @param repoCoords The ID of the project for which issue records are queried. Must not be null.
     * @param iri The IRI for which issue records are queried. Must not be null.
     * @return A list of issue records related to the specified project ID and IRI.
     */
    @Nonnull
    List<IssueRecord> findAllByRepoCoordsAndIris(@Nonnull GitHubRepositoryCoordinates repoCoords, @Nonnull Iri iri);

    void deleteAllByRepoCoords(@Nonnull GitHubRepositoryCoordinates repoCoords);

    List<IssueRecord> findAllByRepoCoords(GitHubRepositoryCoordinates repoCoord);
}

package edu.stanford.protege.issues.service;

import edu.stanford.protege.issues.shared.GitHubIssue;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-15
 */
@Component
public class GitHubIssuesManager {

    private final LocalIssueStoreManager localIssueStoreManager;

    private final LocalIssueStore localIssueStore;

    private final GitHubRepositoryLinkRecordStore linkRecordRepository;

    public GitHubIssuesManager(LocalIssueStoreManager localIssueStoreManager, LocalIssueStore localIssueStore,
                               GitHubRepositoryLinkRecordStore linkRecordRepository) {
        this.localIssueStoreManager = localIssueStoreManager;
        this.localIssueStore = localIssueStore;
        this.linkRecordRepository = linkRecordRepository;
    }

    @Nonnull
        public synchronized List<GitHubIssue> getIssues(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity) {
            return linkRecordRepository.findById(projectId)
                    .map(GitHubRepositoryLinkRecord::repoCoords)
                    .map(repoCoord -> {
                        localIssueStoreManager.ensureLocalStoreIsUpToDate(projectId);
                        return localIssueStore.findAllByRepoCoordsAndIris(repoCoord, Iri.valueOf(entity.getIRI().toString()))
                                       .stream()
                                       .map(IssueRecord::issue)
                                       .toList();
                    }).orElse(List.of());
        }

        public List<GitHubIssue> getIssues(@Nonnull ProjectId projectId) {
            return linkRecordRepository.findById(projectId)
                                       .map(GitHubRepositoryLinkRecord::repoCoords)
                                       .map(repoCoord -> {
                                           localIssueStoreManager.ensureLocalStoreIsUpToDate(projectId);
                                           return localIssueStore.findAllByRepoCoords(repoCoord)
                                                                 .stream()
                                                                 .map(IssueRecord::issue)
                                                                 .toList();
                                       }).orElse(List.of());
        }

}

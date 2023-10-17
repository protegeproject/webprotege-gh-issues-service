package edu.stanford.protege.issues.service;

import edu.stanford.protege.issues.shared.GitHubIssue;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-15
 */
@Component
public class GitHubIssuesManagerImpl implements GitHubIssuesManager {

    private static final Logger logger = LoggerFactory.getLogger(GitHubIssuesManagerImpl.class);

    private final LocalIssueStoreManager localIssueStoreManager;

    private final LocalIssueStore localIssueStore;

    private final GitHubWebHookConfigurationManager webhookManager;

    private final ProjectGitHubLinkRecordRepository linkRecordRepository;

    public GitHubIssuesManagerImpl(LocalIssueStoreManager localIssueStoreManager, LocalIssueStore localIssueStore,
                                   GitHubWebHookConfigurationManager webhookManager,
                                   ProjectGitHubLinkRecordRepository linkRecordRepository) {
        this.localIssueStoreManager = localIssueStoreManager;
        this.localIssueStore = localIssueStore;
        this.webhookManager = webhookManager;
        this.linkRecordRepository = linkRecordRepository;
    }

    @Override
    public void linkGitHubRepo(@Nonnull ProjectId projectId,
                               @Nonnull GitHubRepositoryCoordinates repositoryCoordinates) {
        try {
            var existingLinkRecord = linkRecordRepository.findById(projectId);
            var update = existingLinkRecord.map(ProjectGitHubLinkRecord::updateRequired).orElse(true);
            if(update) {
                logger.info("{} Linking WebProtege project to GitHub repo {}...", projectId, repositoryCoordinates);
                var linkRecord = new ProjectGitHubLinkRecord(projectId, repositoryCoordinates, null, true);
                linkRecordRepository.save(linkRecord);
                logger.info("{} ... project linked to {}", projectId, repositoryCoordinates);
                webhookManager.installWebHook(repositoryCoordinates);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Nonnull
    @Override
    public synchronized List<GitHubIssue> getIssues(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity) {
        return linkRecordRepository.findById(projectId)
                .map(ProjectGitHubLinkRecord::repoCoords)
                .map(repoCoord -> {
                    localIssueStoreManager.ensureLocalStoreIsUpToDate(projectId);
                    return localIssueStore.findAllByRepoCoordsAndIris(repoCoord, Iri.valueOf(entity.getIRI().toString()))
                                   .stream()
                                   .map(IssueRecord::issue)
                                   .toList();
                }).orElse(List.of());
    }

    @Override
    public List<GitHubIssue> getIssues(@Nonnull ProjectId projectId) {
        return linkRecordRepository.findById(projectId)
                                   .map(ProjectGitHubLinkRecord::repoCoords)
                                   .map(repoCoord -> {
                                       localIssueStoreManager.ensureLocalStoreIsUpToDate(projectId);
                                       return localIssueStore.findAllByRepoCoords(repoCoord)
                                                             .stream()
                                                             .map(IssueRecord::issue)
                                                             .toList();
                                   }).orElse(List.of());
    }
}

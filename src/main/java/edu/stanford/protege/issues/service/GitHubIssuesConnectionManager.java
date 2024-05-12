package edu.stanford.protege.issues.service;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-15
 */
@Component
public class GitHubIssuesConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(GitHubIssuesConnectionManager.class);

    private final GitHubWebHookConfigurationManager webhookManager;

    private final GitHubRepositoryLinkRecordStore linkRecordRepository;

    public GitHubIssuesConnectionManager(GitHubWebHookConfigurationManager webhookManager,
                                         GitHubRepositoryLinkRecordStore linkRecordRepository) {
        this.webhookManager = webhookManager;
        this.linkRecordRepository = linkRecordRepository;
    }

    public void linkGitHubRepo(@Nonnull ProjectId projectId,
                               @Nonnull GitHubRepositoryCoordinates repositoryCoordinates) {
        try {
            var existingLinkRecord = linkRecordRepository.findById(projectId);
            var update = existingLinkRecord.map(GitHubRepositoryLinkRecord::updateRequired).orElse(true);
            if (update) {
                logger.info("{} Linking WebProtege project to GitHub repo {}...", projectId, repositoryCoordinates);
                var linkRecord = new GitHubRepositoryLinkRecord(projectId, repositoryCoordinates, null, true);
                linkRecordRepository.save(linkRecord);
                logger.info("{} ... project linked to {}", projectId, repositoryCoordinates);
                webhookManager.installWebHook(repositoryCoordinates);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

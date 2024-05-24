package edu.stanford.protege.issues.service;

import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-16
 */
@Component
public class LocalIssueStoreManager {

    private static final Logger logger = LoggerFactory.getLogger(LocalIssueStoreManager.class);

    private final GitHubRepositoryLinkRecordStore linkRepository;

    private final LocalIssueStoreLoader localIssueStoreLoader;

    public LocalIssueStoreManager(GitHubRepositoryLinkRecordStore linkRepository,
                                  LocalIssueStoreLoader localIssueStoreLoader) {
        this.linkRepository = linkRepository;
        this.localIssueStoreLoader = localIssueStoreLoader;
    }

    public void linkProjectToGitHubRepo(@Nonnull ProjectId projectId, @Nonnull GitHubRepositoryCoordinates repoCoords) {
        var existingLink = linkRepository.findById(projectId);
        if(existingLink.isEmpty()) {
            linkRepository.save(GitHubRepositoryLinkRecord.of(projectId, repoCoords));
        }
    }

    public void unlinkProjectFromGitHubRepo(@Nonnull ProjectId projectId) {
        this.linkRepository.deleteById(projectId);
    }

    public void invalidateLocalIssueStore(@Nonnull ProjectId projectId) {
        linkRepository.findById(projectId)
                      .map(record -> record.withUpdateRequired(true))
                      .ifPresent(linkRepository::save);
    }

    public void ensureLocalStoreIsUpToDate(@Nonnull ProjectId projectId) {
        Objects.requireNonNull(projectId);
        var record = linkRepository.findById(projectId);
        record.ifPresentOrElse(r -> {
                                   logger.info("Found linked GitHub repo for {}.  Details {}", projectId, r);
                               },
                               () -> {
                                   logger.info("No GitHub repo linked for {}", projectId);
                               });
        if(record.isPresent()) {
            var theRecord = record.get();
            if (theRecord.updateRequired()) {
                try {
                    localIssueStoreLoader.load(theRecord.repoCoords());
                    var replacementRecord = theRecord.withUpdatedStatus(Instant.now(), false);
                    linkRepository.save(replacementRecord);
                } catch (IOException e) {
                    logger.info("Unable to update repository due to error: {}", e.getMessage(), e);
                    var updateFailed = theRecord.withUpdateFailed(e.getMessage());
                    linkRepository.save(updateFailed);
                }
            }
        }
    }
}

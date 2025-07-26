package edu.stanford.webprotege.issues.persistence;

import edu.stanford.protege.github.GitHubRepositoryCoordinates;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-16
 */
@Component
public class LocalIssueStoreManager {

    private static final Logger logger = LoggerFactory.getLogger(LocalIssueStoreManager.class);

    private final IssuesSyncStateRecordRepository syncStateRepo;

    private final LocalIssueStoreLoader localIssueStoreLoader;

    public LocalIssueStoreManager(IssuesSyncStateRecordRepository syncStateRepo,
                                  LocalIssueStoreLoader localIssueStoreLoader) {
        this.syncStateRepo = syncStateRepo;
        this.localIssueStoreLoader = localIssueStoreLoader;
    }

    public synchronized void linkProjectToGitHubRepo(@Nonnull ProjectId projectId, @Nonnull GitHubRepositoryCoordinates repoCoords) {
        var existingLink = syncStateRepo.findById(projectId);
        if(existingLink.isEmpty()) {
            syncStateRepo.save(IssuesSyncStateRecord.of(projectId, repoCoords));
        }
    }

    public synchronized void invalidateLocalIssueStore(@Nonnull ProjectId projectId) {
        syncStateRepo.findById(projectId)
                      .map(record -> record.withSyncState(IssuesSyncState.NOT_SYNCED))
                      .ifPresent(syncStateRepo::save);
    }

    public synchronized void ensureLocalStoreIsUpToDate(@Nonnull ProjectId projectId) {
        Objects.requireNonNull(projectId);
        var record = syncStateRepo.findById(projectId);
        record.ifPresentOrElse(r -> {
                                   logger.info("Found linked GitHub repo for {}.  Details {}", projectId, r);
                               },
                               () -> {
                                   logger.info("No GitHub repo linked for {}", projectId);
                               });
        if(record.isPresent()) {
            var theRecord = record.get();
            if (theRecord.syncState().equals(IssuesSyncState.NOT_SYNCED)) {
                try {
                    var replacementRecord = theRecord.withSyncState(IssuesSyncState.SYNCING);
                    syncStateRepo.save(replacementRecord);
                    localIssueStoreLoader.load(theRecord.repoCoords());
                    var syncedRecord = theRecord.withSyncState(IssuesSyncState.SYNCED);
                    syncStateRepo.save(syncedRecord);
                } catch (IOException e) {
                    logger.error("Unable to update repository", e);
                    var updateFailed = theRecord.withUpdateFailed(e.getMessage());
                    syncStateRepo.save(updateFailed);
                }
            }
        }
    }
}

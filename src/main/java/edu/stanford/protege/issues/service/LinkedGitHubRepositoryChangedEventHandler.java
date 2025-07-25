package edu.stanford.protege.issues.service;

import edu.stanford.protege.webprotege.ipc.EventHandler;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebProtegeHandler
public class LinkedGitHubRepositoryChangedEventHandler implements EventHandler<LinkedGitHubRepositoryChangedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(LinkedGitHubRepositoryChangedEventHandler.class);

    private final GitHubRepositoryLinkRecordStore store;

    public LinkedGitHubRepositoryChangedEventHandler(GitHubRepositoryLinkRecordStore store) {
        this.store = store;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return LinkedGitHubRepositoryChangedEvent.CHANNEL;
    }

    @NotNull
    @Override
    public String getHandlerName() {
        return "LinkedGitHubRepositoryUpdated";
    }

    @Override
    public Class<LinkedGitHubRepositoryChangedEvent> getEventClass() {
        return LinkedGitHubRepositoryChangedEvent.class;
    }

    @Override
    public void handleEvent(LinkedGitHubRepositoryChangedEvent event) {
        logger.info("Handling linked GitHubRepositoryChangedEvent: {}", event);
        var repoCoords = event.repositoryCoordinates();

        if (repoCoords != null) {
            var linkRecord = GitHubRepositoryLinkRecord.of(
                    event.projectId(),
                    repoCoords
            );
            store.save(linkRecord);
        }
        else {
            store.deleteById(event.projectId());
        }
    }
}

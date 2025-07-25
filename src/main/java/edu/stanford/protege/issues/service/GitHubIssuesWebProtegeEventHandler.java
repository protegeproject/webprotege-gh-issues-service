package edu.stanford.protege.issues.service;

import edu.stanford.protege.webprotege.ipc.EventHandler;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@WebProtegeHandler
public class GitHubIssuesWebProtegeEventHandler implements EventHandler<GitHubIssuesWebProtegeEvent> {

    private final GitHubRepositoryLinkRecordStore store;

    private final LocalIssueStoreUpdater updater;

    private final Logger logger = LoggerFactory.getLogger(GitHubIssuesWebProtegeEventHandler.class);

    public GitHubIssuesWebProtegeEventHandler(GitHubRepositoryLinkRecordStore store, LocalIssueStoreUpdater updater) {
        this.store = store;
        this.updater = updater;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GitHubIssuesWebProtegeEvent.CHANNEL;
    }

    @NotNull
    @Override
    public String getHandlerName() {
        return "GitHubIssuesEventHandler";
    }

    @Override
    public Class<GitHubIssuesWebProtegeEvent> getEventClass() {
        return GitHubIssuesWebProtegeEvent.class;
    }

    @Override
    public void handleEvent(GitHubIssuesWebProtegeEvent event) {
        var issueEvent = event.issuesEvent();
        logger.info("Handling GitHubIssuesWebProtegeEvent: {} on issue {} in repo {}.  Issue: {}",
                issueEvent.action(),
                issueEvent.issue().id(),
                issueEvent.repository().getCoordinates(),
                issueEvent.issue());
        var repoCoords = issueEvent.repository().getCoordinates();
        updater.updateIssues(repoCoords, List.of(issueEvent.issue()));
    }
}

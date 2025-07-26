package edu.stanford.webprotege.issues.handler;

import edu.stanford.webprotege.issues.message.WebProtegeGitHubIssuesEvent;
import edu.stanford.webprotege.issues.persistence.IssuesSyncStateRecordRepository;
import edu.stanford.webprotege.issues.persistence.LocalIssueStoreUpdater;
import edu.stanford.protege.webprotege.ipc.EventHandler;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@WebProtegeHandler
public class WebProtegeGitHubIssuesEventHandler implements EventHandler<WebProtegeGitHubIssuesEvent> {

    private final IssuesSyncStateRecordRepository store;

    private final LocalIssueStoreUpdater updater;

    private final Logger logger = LoggerFactory.getLogger(WebProtegeGitHubIssuesEventHandler.class);

    public WebProtegeGitHubIssuesEventHandler(IssuesSyncStateRecordRepository store, LocalIssueStoreUpdater updater) {
        this.store = store;
        this.updater = updater;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return WebProtegeGitHubIssuesEvent.CHANNEL;
    }

    @NotNull
    @Override
    public String getHandlerName() {
        return "GitHubIssuesEventHandler";
    }

    @Override
    public Class<WebProtegeGitHubIssuesEvent> getEventClass() {
        return WebProtegeGitHubIssuesEvent.class;
    }

    @Override
    public void handleEvent(WebProtegeGitHubIssuesEvent event) {
        var issueEvent = event.issuesEvent();
        logger.info("Handling WebProtegeGitHubIssuesEvent: {} on issue {} in repo {}.  Issue: {}",
                issueEvent.action(),
                issueEvent.issue().id(),
                issueEvent.repository().getCoordinates(),
                issueEvent.issue());
        var repoCoords = issueEvent.repository().getCoordinates();
        updater.updateIssues(repoCoords, List.of(issueEvent.issue()));
    }
}

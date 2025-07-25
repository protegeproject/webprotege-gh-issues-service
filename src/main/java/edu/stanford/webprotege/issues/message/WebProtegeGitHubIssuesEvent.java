package edu.stanford.webprotege.issues.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.webprotege.issues.model.GitHubIssuesEvent;
import edu.stanford.protege.webprotege.common.Event;
import edu.stanford.protege.webprotege.common.EventId;

import static edu.stanford.webprotege.issues.message.WebProtegeGitHubIssuesEvent.CHANNEL;

@JsonTypeName(CHANNEL)
public record WebProtegeGitHubIssuesEvent(@JsonProperty("eventId") EventId eventId,
                                          @JsonProperty("issuesEvent") GitHubIssuesEvent issuesEvent) implements Event {

    public static final String CHANNEL = "webprotege.github.events.IssuesEvent";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}

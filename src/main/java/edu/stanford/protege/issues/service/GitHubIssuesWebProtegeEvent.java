package edu.stanford.protege.issues.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Event;
import edu.stanford.protege.webprotege.common.EventId;

import static edu.stanford.protege.issues.service.GitHubIssuesWebProtegeEvent.CHANNEL;

@JsonTypeName(CHANNEL)
public record GitHubIssuesWebProtegeEvent(@JsonProperty("eventId") EventId eventId,
                                          @JsonProperty("issuesEvent") GitHubIssuesEvent issuesEvent) implements Event {

    public static final String CHANNEL = "webprotege.github.events.IssuesEvent";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}

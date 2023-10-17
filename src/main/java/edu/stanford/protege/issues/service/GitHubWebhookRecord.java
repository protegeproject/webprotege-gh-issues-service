package edu.stanford.protege.issues.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-16
 */
public record GitHubWebhookRecord(@JsonProperty("node_id") @Nonnull String nodeId, long id, String name) {

}

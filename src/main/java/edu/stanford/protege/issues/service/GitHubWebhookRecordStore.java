package edu.stanford.protege.issues.service;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.data.repository.CrudRepository;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-16
 */
public interface GitHubWebhookRecordStore extends CrudRepository<GitHubWebhookRecord, ProjectId> {

}

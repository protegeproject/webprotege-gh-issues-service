package edu.stanford.protege.issues.service;

import edu.stanford.protege.issues.shared.GitHubIssue;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-15
 */
public interface GitHubIssuesManager {

    @Nonnull
    List<GitHubIssue> getIssues(@Nonnull ProjectId projectId,
                                @Nonnull OWLEntity entity);
}

package edu.stanford.protege.issues.service;

import edu.stanford.protege.issues.shared.GitHubIssue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-15
 */
public class GitHubIssueIndexer {

    public void indexIssue(@Nonnull GitHubIssue issue) {
        var title = issue.title();
        var body = issue.body();
    }

}

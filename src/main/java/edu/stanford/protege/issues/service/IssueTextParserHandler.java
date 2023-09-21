package edu.stanford.protege.issues.service;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-15
 */
public interface IssueTextParserHandler {

    void handleUrl(String url);

    void handleOboId(String oboId);
}

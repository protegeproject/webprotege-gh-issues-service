package edu.stanford.protege.issues.service;

import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.StringReader;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-15
 */
@Component
public class IssueTextParserFactory {

    @Nonnull
    public IssueTextParser createParser(@Nonnull String text) {
        return new IssueTextParser(new StringReader(text));
    }
}

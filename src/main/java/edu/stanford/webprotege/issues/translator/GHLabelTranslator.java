package edu.stanford.webprotege.issues.translator;

import edu.stanford.protege.github.issues.GitHubLabel;
import org.kohsuke.github.GHLabel;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-18
 */
@Component
public class GHLabelTranslator {

    @Nonnull
    public GitHubLabel translate(@Nonnull GHLabel label) {
        return GitHubLabel.get(label.getId(),
                               label.getNodeId(),
                               label.getUrl(),
                               label.getName(),
                               label.getColor(),
                               label.isDefault(),
                               label.getDescription());
    }
}

package edu.stanford.protege.issues.service;

import org.semanticweb.owlapi.model.IRI;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-21
 */
@Component
public class OboIdResolverImpl implements OboIdResolver {

    @Nonnull
    @Override
    public Optional<IRI> resolveOboId(@Nonnull String oboId) {
        if (OboUtilities.isOboId(oboId)) {
            var iri = OboUtilities.getOboLibraryIriFromOboId(oboId);
            return Optional.of(iri);
        }
        else {
            return Optional.empty();
        }

    }
}

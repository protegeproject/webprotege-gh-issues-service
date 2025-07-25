package edu.stanford.webprotege.issues.entity;

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
public class DefaultOboIdResolver implements OboIdResolver {

    @Nonnull
    @Override
    public Optional<IRI> resolveOboId(@Nonnull String oboId) {
        if (OboIdUtilities.isOboId(oboId)) {
            var iri = OboIdUtilities.getOboLibraryIriFromOboId(oboId);
            return Optional.of(iri);
        }
        else {
            return Optional.empty();
        }

    }
}

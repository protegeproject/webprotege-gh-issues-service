package edu.stanford.protege.issues.service;

import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-20
 */
public interface OboIdResolver {

    @Nonnull
    Optional<IRI> resolveOboId(@Nonnull String oboId);
}

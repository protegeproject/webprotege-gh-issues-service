package edu.stanford.protege.issues.service;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

/**
 * An {@code EntityResolver} is responsible for resolving OWL entities
 * based on their Internationalized Resource Identifier (IRI).
 * <p>
 * This interface provides a method to retrieve a set of OWL entities that have
 * a given IRI.
 */
@Component
public interface EntityResolver {

    /**
     * Retrieves a set of OWL entities associated with the provided IRI.
     *
     * @param lookup The Internationalized Resource Identifier (IRI) for which
     *               OWL entities need to be resolved.
     * @return A set of OWLEntity objects that have the given IRI.
     *         If no entities are found, an empty set is returned.
     *
     * @throws NullPointerException if {@code lookup} is {@code null}.
     */
    @Nonnull
    Set<OWLEntity> getEntities(@Nonnull IRI lookup);
}
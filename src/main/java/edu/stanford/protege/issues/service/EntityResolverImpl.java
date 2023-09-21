package edu.stanford.protege.issues.service;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-21
 */
@Component
public class EntityResolverImpl implements EntityResolver {

    @Nonnull
    @Override
    public Set<OWLEntity> getEntities(@Nonnull IRI lookup) {
        return Set.of();
    }
}

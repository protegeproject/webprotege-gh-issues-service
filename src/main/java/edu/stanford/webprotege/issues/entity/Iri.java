package edu.stanford.webprotege.issues.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.Objects;

public record Iri(@JsonValue @Nonnull String lexicalValue) {

    public Iri(@Nonnull String lexicalValue) {
        this.lexicalValue = Objects.requireNonNull(lexicalValue);
    }

    @JsonCreator
    @Nonnull
    public static Iri valueOf(@Nonnull String lexicalValue) {
        return new Iri(lexicalValue);
    }

    @Nonnull
    public static Iri valueOf(@Nonnull IRI iri) {
        return valueOf(iri.toString());
    }

    @Nonnull
    public static Iri valueOf(HasIRI subject) {
        return valueOf(subject.toString());
    }
}

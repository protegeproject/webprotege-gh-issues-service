package edu.stanford.protege.issues.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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
}

package edu.stanford.webprotege.issues.entity;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public record OboId(@Nonnull String id) {

    public OboId(@Nonnull String id) {
        this.id = checkNotNull(id);
    }

    @Nonnull
    public static OboId valueOf(@Nonnull String id) {
        return new OboId(id);
    }

    @JsonValue
    @Override
    public String id() {
        return id;
    }
}

package edu.stanford.webprotege.issues.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Represents a GitHub event that a GitHub App can subscribe to.
 * Example: "push", "pull_request", "issues"
 * @param value The event name in lowercase.
 */
public record GitHubEvent(String value) {

    @JsonCreator
    public GitHubEvent {
        Objects.requireNonNull(value, "event value must not be null");
        value = value.toLowerCase();
    }

    @JsonValue
    public String jsonValue() {
        return value;
    }

    @Override
    public String toString() {
        return "GitHubEvent[" + value + "]";
    }
}


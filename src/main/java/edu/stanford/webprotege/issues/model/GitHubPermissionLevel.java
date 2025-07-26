package edu.stanford.webprotege.issues.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents GitHub App permission levels.
 */
public enum GitHubPermissionLevel {

    READ("read" ),
    WRITE("write" ),
    NONE("none" );

    private final String jsonValue;

    GitHubPermissionLevel(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @JsonCreator
    public static GitHubPermissionLevel fromJson(String value) {
        for (GitHubPermissionLevel level : GitHubPermissionLevel.values()) {
            if (level.jsonValue.equalsIgnoreCase(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown permission level: " + value);
    }

    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }
}


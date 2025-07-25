package edu.stanford.protege.issues.service;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The GitHub user who triggered the event.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubSender(
        /**
         * GitHub login name.
         */
        @JsonProperty("login" ) String login,

        /**
         * GitHub user ID.
         */
        @JsonProperty("id" ) long id,

        /**
         * URL to the user's avatar image.
         */
        @JsonProperty("avatar_url" ) String avatar_url,

        /**
         * URL to the user's GitHub profile.
         */
        @JsonProperty("html_url" ) String html_url
) {

}
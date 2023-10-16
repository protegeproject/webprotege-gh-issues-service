package edu.stanford.protege.issues.service;

import edu.stanford.protege.issues.shared.GitHubUser;
import edu.stanford.protege.issues.shared.GitHubUserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class GHUserTranslatorTest {

    private static final long ID = 33L;

    private static final String LOGIN = "TestLogin";

    protected static final String TEST_AVATAR_URL = "TestAvatarUrl";

    protected static final String URL = "https://example.org/user";

    protected static final String HTML_URL = "https://example.org/hmtl/user";

    protected static final String TEST_NODE_ID = "TestNodeId";

    private GHUserTranslator translator;

    private GHUser user;

    @BeforeEach
    void setUp() {
        user = new GHUserStub();
        translator = new GHUserTranslator();
    }

    @Test
    void shouldTranslateUser() {
        var translation = translator.translate(user);
        assertThat(translation).map(GitHubUser::id).contains(ID);
        assertThat(translation).map(GitHubUser::login).contains(LOGIN);
        assertThat(translation).map(GitHubUser::url).contains(URL);
        assertThat(translation).map(GitHubUser::htmlUrl).contains(HTML_URL);
        assertThat(translation).map(GitHubUser::nodeId).contains(TEST_NODE_ID);
        assertThat(translation).map(GitHubUser::type).contains(GitHubUserType.ORGANIZATION);
        assertThat(translation).map(GitHubUser::siteAdmin).hasValue(true);
    }


    /**
     * I had to create this stub because I couldn't get mockito to work
     * mock GHUser.  My impression is mockito was getting confused by "bridge methods"
     * that this GitHub library creates in byte code to ensure backwards binary compatibility.
     */
    private static class GHUserStub extends GHUser {

        @Override
        public long getId() {
            return ID;
        }

        @Override
        public String getLogin() {
            return LOGIN;
        }

        @Override
        public String getAvatarUrl() {
            return TEST_AVATAR_URL;
        }

        @Override
        public URL getUrl() {
            try {
                return URI.create(URL).toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public java.net.URL getHtmlUrl() {
            try {
                return URI.create(HTML_URL).toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getNodeId() {
            return TEST_NODE_ID;
        }

        @Override
        public String getType() throws IOException {
            return "Organization";
        }

        @Override
        public boolean isSiteAdmin() throws IOException {
            return true;
        }
    }
}
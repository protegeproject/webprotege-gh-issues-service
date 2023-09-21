package edu.stanford.protege.issues.service;

import edu.stanford.protege.issues.shared.GitHubMilestone;
import edu.stanford.protege.issues.shared.GitHubState;
import edu.stanford.protege.issues.shared.GitHubUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHMilestoneState;
import org.kohsuke.github.GHUser;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GHMilestoneTranslatorTest {

    protected static final long ID = 33L;

    protected static final String NODE_ID = "TestNodeId";

    protected static final int NUMBER = 55;

    protected static final String TITLE = "Test Title";

    protected static final String DESCRIPTION = "Test Description";

    protected static final int OPEN_ISSUES = 66;

    protected static final int CLOSED_ISSUES = 77;

    protected static final String URL = "https://example.org/url";

    protected static final Instant CREATED_AT = Instant.parse("2005-03-04T00:00:00Z");

    protected static final Instant UPDATED_AT = Instant.parse("2007-03-04T00:00:00Z");

    protected static final Instant DUE_ON = Instant.parse("2055-03-04T00:00:00Z");

    protected static final Instant CLOSED_AT = Instant.parse("2034-03-05T00:00:00Z");

    private GHMilestone milestone;

    private GHMilestoneTranslator translator;


    @Mock
    private GHUser creator;

    @Mock
    private GitHubUser translatedCreator;

    private GHUserTranslator userTranslator;

    @BeforeEach
    void setUp() {
        milestone = new GHMilestoneStub();
        userTranslator = mock(GHUserTranslator.class);
        translator = new GHMilestoneTranslator(userTranslator);
    }

    @Test
    void shouldTranslateId() {
        var translated = translator.translate(milestone);
        assertThat(translated.id()).isEqualTo(ID);
    }

    @Test
    void shouldTranslateUrl() {
        var translated = translator.translate(milestone);
        assertThat(translated.url()).isEqualTo(URL);
    }

    @Test
    void shouldTranslateNodeId() {
        var translated = translator.translate(milestone);
        assertThat(translated.nodeId()).isEqualTo(NODE_ID);
    }

    @Test
    void shouldTranslateNumber() {
        var translated = translator.translate(milestone);
        assertThat(translated.number()).isEqualTo(NUMBER);
    }

    @Test
    void shouldTranslateTitle() {
        var translated = translator.translate(milestone);
        assertThat(translated.title()).isEqualTo(TITLE);
    }

    @Test
    void shouldTranslateDescription() {
        var translated = translator.translate(milestone);
        assertThat(translated.description()).isEqualTo(DESCRIPTION);
    }


    @Test
    void shouldTranslateCreator() {
        when(userTranslator.translate(creator)).thenReturn(translatedCreator);
        var translated = translator.translate(milestone);
        assertThat(translated.creator()).isEqualTo(translatedCreator);
    }


    @Test
    void shouldTranslateOpenIssues() {
        var translated = translator.translate(milestone);
        assertThat(translated.openIssues()).isEqualTo(OPEN_ISSUES);
    }

    @Test
    void shouldTranslateClosedIssues() {
        var translated = translator.translate(milestone);
        assertThat(translated.closedIssues()).isEqualTo(CLOSED_ISSUES);
    }

    @Test
    void shouldTranslateState() {
        var translated = translator.translate(milestone);
        assertThat(translated.state()).isEqualTo(GitHubState.OPEN);
    }

    @Test
    void shouldTranslateCreatedAt() {
        var translated = translator.translate(milestone);
        assertThat(translated.createdAt()).isEqualTo(CREATED_AT);
    }

    @Test
    void shouldTranslateUpdatedAt() {
        var translated = translator.translate(milestone);
        assertThat(translated.updatedAt()).isEqualTo(UPDATED_AT);
    }


    @Test
    void shouldTranslateClosedAt() {
        var translated = translator.translate(milestone);
        assertThat(translated.closedAt()).isEqualTo(CLOSED_AT);
    }


    @Test
    void shouldTranslateDueOn() {
        var translated = translator.translate(milestone);
        assertThat(translated.dueOn()).isEqualTo(DUE_ON);
    }

    private class GHMilestoneStub extends GHMilestone {



        @Override
        public URL getUrl() {
            try {
                return URI.create(URL).toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public long getId() {
            return ID;
        }

        @Override
        public String getNodeId() {
            return NODE_ID;
        }

        @Override
        public int getNumber() {
            return NUMBER;
        }

        @Override
        public String getTitle() {
            return TITLE;
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }

        @Override
        public GHUser getCreator() throws IOException {
            return creator;
        }

        @Override
        public int getOpenIssues() {
            return OPEN_ISSUES;
        }

        @Override
        public int getClosedIssues() {
            return CLOSED_ISSUES;
        }

        @Override
        public GHMilestoneState getState() {
            return GHMilestoneState.OPEN;
        }

        @Override
        public Date getCreatedAt() throws IOException {
            return new Date(CREATED_AT.toEpochMilli());
        }

        @Override
        public Date getUpdatedAt() throws IOException {
            return new Date(UPDATED_AT.toEpochMilli());
        }

        @Override
        public Date getDueOn() {
            return new Date(DUE_ON.toEpochMilli());
        }

        @Override
        public Date getClosedAt() throws IOException {
            return new Date(CLOSED_AT.toEpochMilli());
        }
    }
}
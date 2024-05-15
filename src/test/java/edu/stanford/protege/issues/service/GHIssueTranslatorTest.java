package edu.stanford.protege.issues.service;

import edu.stanford.protege.github.issues.shared.GitHubLabel;
import edu.stanford.protege.github.issues.shared.GitHubState;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GHIssueTranslatorTest {

    protected static final long ID = 33L;

    protected static final String NODE_ID = "TestNodeId";

    protected static final int NUMBER = 55;

    protected static final String TITLE = "Test Title";

    protected static final String BODY = "Test Body";

    protected static final String URL = "https://example.org/url";

    protected static final Instant CREATED_AT = Instant.parse("2005-03-04T00:00:00Z");

    protected static final Instant UPDATED_AT = Instant.parse("2007-03-04T00:00:00Z");

    protected static final Instant CLOSED_AT = Instant.parse("2034-03-05T00:00:00Z");

    protected static final String HTML_URL = "https://example.org/html/issue";

    protected static final boolean LOCKED = true;

    private GHIssue issue;

    private GHIssueTranslator translator;


    @Mock
    private GHUserTranslator userTranslator;

    @Mock
    private GHMilestoneTranslator milestoneTranslator;

    @Mock
    private GHLabelTranslator labelTranslator;

    @Mock
    private GHReactionTranslator reactionTranslator;

    @Mock
    private GHLabel ghLabel;

    @Mock
    private GitHubLabel translatedLabel;

    @Mock
    private GHUser ghAssignee;

    @Mock
    private GHUser ghUser;

    @Mock
    private GHUser ghClosedBy;

    @Mock
    private PagedIterable<GHReaction> ghReactionsIterator;

    @BeforeEach
    void setUp() {
        issue = new GHIssueStub();
        translator = new GHIssueTranslator(userTranslator,
                                           labelTranslator,
                                           milestoneTranslator,
                                           reactionTranslator);
    }

    @Test
    void shouldTranslateId() {
        var translated = translator.translate(issue);
        assertThat(translated.id()).isEqualTo(ID);
    }

    @Test
    void shouldTranslateUrl() {
        var translated = translator.translate(issue);
        assertThat(translated.htmlUrl()).isEqualTo(HTML_URL);
    }

    @Test
    void shouldTranslateHtmlUrl() {
        var translated = translator.translate(issue);
        assertThat(translated.url()).isEqualTo(URL);
    }

    @Test
    void shouldTranslateNodeId() {
        var translated = translator.translate(issue);
        assertThat(translated.nodeId()).isEqualTo(NODE_ID);
    }

    @Test
    void shouldTranslateNumber() {
        var translated = translator.translate(issue);
        assertThat(translated.number()).isEqualTo(NUMBER);
    }

    @Test
    void shouldTranslateTitle() {
        var translated = translator.translate(issue);
        assertThat(translated.title()).isEqualTo(TITLE);
    }

    @Test
    void shouldTranslateBody() {
        var translated = translator.translate(issue);
        assertThat(translated.body()).isEqualTo(BODY);
    }

    @Test
    void shouldTranslateState() {
        var translated = translator.translate(issue);
        assertThat(translated.state()).isEqualTo(GitHubState.OPEN);
    }

    @Test
    void shouldTranslateCreatedAt() {
        var translated = translator.translate(issue);
        assertThat(translated.createdAt()).isEqualTo(CREATED_AT);
    }

    @Test
    void shouldTranslateUpdatedAt() {
        var translated = translator.translate(issue);
        assertThat(translated.updatedAt()).isEqualTo(UPDATED_AT);
    }

    @Test
    void shouldTranslateClosedAt() {
        var translated = translator.translate(issue);
        assertThat(translated.closedAt()).isEqualTo(CLOSED_AT);
    }

    @Test
    void shouldTranslateLocked() {
        var translated = translator.translate(issue);
        assertThat(translated.locked()).isTrue();
    }

    @Test
    void shouldTranslateLabels() {
        when(labelTranslator.translate(ghLabel)).thenReturn(translatedLabel);
        var translated = translator.translate(issue);
        assertThat(translated.labels()).contains(translatedLabel);
    }

    private class GHIssueStub extends GHIssue {

        @Override
        public java.net.URL getUrl() {
            try {
                return URI.create(URL).toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getBody() {
            return BODY;
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
        public Date getCreatedAt() throws IOException {
            return new Date(CREATED_AT.toEpochMilli());
        }

        @Override
        public Date getUpdatedAt() throws IOException {
            return new Date(UPDATED_AT.toEpochMilli());
        }

        @Override
        public Date getClosedAt() {
            return new Date(CLOSED_AT.toEpochMilli());
        }

        @Override
        public boolean isLocked() {
            return LOCKED;
        }

        @Override
        public GHIssueState getState() {
            return GHIssueState.OPEN;
        }

        @Override
        public Collection<GHLabel> getLabels() {
            return List.of(ghLabel);
        }

        @Override
        public GHUser getAssignee() throws IOException {
            return ghAssignee;
        }

        @Override
        public List<GHUser> getAssignees() {
            return List.of(ghAssignee);
        }

        @Override
        public GHUser getUser() throws IOException {
            return ghUser;
        }

        @Override
        public GHUser getClosedBy() throws IOException {
            return ghClosedBy;
        }

        @Override
        public PagedIterable<GHReaction> listReactions() {
            return ghReactionsIterator;
        }
    }
}
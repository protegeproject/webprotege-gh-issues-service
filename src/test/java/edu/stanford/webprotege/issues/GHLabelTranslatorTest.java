package edu.stanford.webprotege.issues;

import edu.stanford.webprotege.issues.translator.GHLabelTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kohsuke.github.GHLabel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class GHLabelTranslatorTest {

    protected static final long ID = 33L;

    protected static final String NAME = "test name";

    protected static final String DESCRIPTION = "test description";

    protected static final String COLOR = "test color";

    private static final String NODE_ID = "test node id";

    private static final String URL = "test URL";

    private static final boolean DEFAULT = true;

    private GHLabel label;

    private GHLabelTranslator translator;

    @BeforeEach
    void setUp() {
        label = mock(GHLabel.class);
        doReturn(ID).when(label).getId();
        doReturn(NAME).when(label).getName();
        doReturn(DESCRIPTION).when(label).getDescription();
        doReturn(COLOR).when(label).getColor();
        doReturn(NODE_ID).when(label).getNodeId();
        doReturn(URL).when(label).getUrl();
        doReturn(DEFAULT).when(label).isDefault();

        translator = new GHLabelTranslator();
    }

    @Test
    void shouldTranslateLabel() {
        var translatedLabel = translator.translate(label);
        assertThat(translatedLabel.id()).isEqualTo(ID);
        assertThat(translatedLabel.name()).isEqualTo(NAME);
        assertThat(translatedLabel.description()).isEqualTo(DESCRIPTION);
        assertThat(translatedLabel.color()).isEqualTo(COLOR);
        assertThat(translatedLabel.nodeId()).isEqualTo(NODE_ID);
        assertThat(translatedLabel.url()).isEqualTo(URL);
        assertThat(translatedLabel.isDefault()).isEqualTo(DEFAULT);
    }
}
package edu.stanford.webprotege.issues;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.webprotege.issues.entity.OboId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-21
 */
public class OboIdTest {

    protected static final String OBO_ID = "GO:1234567";

    private JacksonTester<OboId> tester;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void shouldCreateOboIdViaConstructor() {
        var OboId = new OboId(OBO_ID);
        assertThat(OboId.id()).isEqualTo(OBO_ID);
    }

    @Test
    public void shouldThrowNpeIfLexicalValueIsNull() {
        // Act and Assert
        assertThatThrownBy(() -> new OboId(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldCreateOboIdViaFactoryMethod() {
        var lexicalValue = "http://example.com/OboId";
        var oboId = OboId.valueOf(lexicalValue);
        assertThat(oboId).isNotNull();
        assertThat(oboId.id()).isEqualTo(lexicalValue);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    public void shouldThrowNpeIfNullIsPassedToFactoryMethod() {
        // Act and Assert
        assertThatThrownBy(() -> OboId.valueOf(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldDeserializeJson() throws IOException {
        var read = tester.readObject(new StringReader("""
                                  "GO:1234567"
                                  """));

        assertThat(read.id()).isEqualTo(OBO_ID);
    }

    @Test
    void shouldSerializeJson() throws IOException {
        var content = tester.write(OboId.valueOf(OBO_ID));
        assertThat(content.getJson()).isEqualTo("\"GO:1234567\"");
    }
}

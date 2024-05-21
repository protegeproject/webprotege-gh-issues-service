package edu.stanford.protege.issues.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IriTest {

    private JacksonTester<Iri> tester;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void shouldCreateIriViaConstructor() {
        var lexicalValue = "http://example.com/iri";
        var iri = new Iri(lexicalValue);
        assertThat(iri.lexicalValue()).isEqualTo(lexicalValue);
    }

    @Test
    public void shouldThrowNpeIfLexicalValueIsNull() {
        // Act and Assert
        assertThatThrownBy(() -> new Iri(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldCreateIriViaFactoryMethod() {
        var lexicalValue = "http://example.com/iri";
        var iri = Iri.valueOf(lexicalValue);
        assertThat(iri).isNotNull();
        assertThat(iri.lexicalValue()).isEqualTo(lexicalValue);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    public void shouldThrowNpeIfNullIsPassedToFactoryMethod() {
        // Act and Assert
        assertThatThrownBy(() -> Iri.valueOf(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldDeserializeJson() throws IOException {
        var read = tester.readObject(new StringReader("""
                                  "https://example.org/A"
                                  """));

        assertThat(read.lexicalValue()).isEqualTo("https://example.org/A");
    }

    @Test
    void shouldSerializeJson() throws IOException {
        var content = tester.write(Iri.valueOf("https://example.org/A"));
        assertThat(content.getJson()).isEqualTo("\"https://example.org/A\"");
    }
}
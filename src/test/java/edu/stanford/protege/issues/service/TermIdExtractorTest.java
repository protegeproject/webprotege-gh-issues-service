package edu.stanford.protege.issues.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TermIdExtractorTest {

    private TermIdExtractor extractor;

    @Mock
    private IssueTextParserFactory parserFactory;

    @Mock
    private IssueTextParser parser;

    private final String oboId = "GO:1234567";

    private final String iri = "http://example.org/A";

    @BeforeEach
    void setUp() throws ParseException {
        when(parserFactory.createParser(anyString())).thenReturn(parser);
        doAnswer(inv -> {
            var handler = (IssueTextParserHandler) inv.getArgument(0);
            handler.handleOboId(oboId);
            handler.handleUrl(iri);
            return null;
        }).when(parser).parse(any());
        extractor = new TermIdExtractor(parserFactory);


    }

    @Test
    void shouldExtractOboId() {
        var extractedIds = extractor.extractTermIds("Any text");
        assertThat(extractedIds.extractedOboIds()).containsExactly(OboId.valueOf(oboId));
        assertThat(extractedIds.extractedIris()).containsExactly(Iri.valueOf(iri));
    }
}
package edu.stanford.webprotege.issues;

import edu.stanford.webprotege.issues.parser.IssueTextParserHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.StringReader;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-15
 */
@ExtendWith(SpringExtension.class)
public class IssueTextParserTest {

    @Mock
    private IssueTextParserHandler handler;

    @Test
    void shouldParseUrl() throws ParseException {
        var text = "https://www.bbc.com";
        parse(text);
        verify(handler, times(1)).handleUrl("https://www.bbc.com");
    }

    @Test
    void shouldParseUrlWithSlash() throws ParseException {
        var text = "https://www.bbc.com/";
        parse(text);
        verify(handler, times(1)).handleUrl("https://www.bbc.com/");
    }

    @Test
    void shouldParseUrlWithFragment() throws ParseException {
        var text = "https://www.bbc.com#ABC";
        parse(text);
        verify(handler, times(1)).handleUrl("https://www.bbc.com#ABC");
    }

    @Test
    void shouldParseUrlWithFragmentWithPerido() throws ParseException {
        var text = "https://www.bbc.com#ABC.";
        parse(text);
        verify(handler, times(1)).handleUrl("https://www.bbc.com#ABC");
    }

    @Test
    void shouldParseEmbeddedUrl() throws ParseException {
        var text = "See this https://www.bbc.com for details";
        parse(text);
        verify(handler, times(1)).handleUrl("https://www.bbc.com");
    }

    @Test
    void shouldParseEmbeddedUrlWithPeriod() throws ParseException {
        var text = "See this https://www.bbc.com.";
        parse(text);
        verify(handler, times(1)).handleUrl("https://www.bbc.com");
    }

    @Test
    void shouldParseEmbeddedUrlInBrackets() throws ParseException {
        var text = "See (https://www.bbc.com)";
        parse(text);
        verify(handler, times(1)).handleUrl("https://www.bbc.com");
    }

    @Test
    void shouldParseEmbeddedUrlInBracketsWithPeriod() throws ParseException {
        var text = "See (https://www.bbc.com).";
        parse(text);
        verify(handler, times(1)).handleUrl("https://www.bbc.com");
    }

    @Test
    void shouldParseEmbeddedUrlInSquareBrackets() throws ParseException {
        var text = "See [https://www.bbc.com]";
        parse(text);
        verify(handler, times(1)).handleUrl("https://www.bbc.com");
    }

    @Test
    void shouldParseOboId() throws ParseException {
        var text = "GO:000000123";
        parse(text);
        verify(handler, times(1)).handleOboId("GO:000000123");
    }


    @Test
    void shouldParseEmbeededOboId() throws ParseException {
        var text = "See GO:000000123 for details";
        parse(text);
        verify(handler, times(1)).handleOboId("GO:000000123");
    }

    @Test
    void shouldParseEmbeededOboIdWithPeriod() throws ParseException {
        var text = "See GO:000000123.";
        parse(text);
        verify(handler, times(1)).handleOboId("GO:000000123");
    }


    private String tt = """
            Here is an example:
            https://www.ebi.ac.uk/QuickGO/annotations?geneProductId=Q9NWS0
            (I will list the indirct terms when I get chance, and challenge the original annotations and the PAINT transfer)
                        
            This is unsurprising since the GO Complex term
                        
            GO:0097255 R2TP complex
            Definition
            A highly conserved protein complex comprised of two ATP-dependent DNA helicases (Rvb1p and Rvb2p in yeast, Pontin52 and Reptin52 in humans), Pih1p in yeast or PIH1D1 in humans, and Tah1 in yeast or RPAP3 in humans. The complex associates with Hsp90 and is thought to have a role in assembly of large protein or protein/nucleic acid complexes. In this role it is involved in multiple processes such as box C/D snoRNP biogenesis, phosphatidylinositol-3 kinase-related protein kinase (PIKK) signaling, RNA polymerase II assembly, and others. PMID:15766533 PMID:21925213
                        
            Implies that it is OK to annotate to these downstream processes:
            phosphatidylinositol-3 kinase-related protein kinase (PIKK) signaling
            Even though TTT is a co-translational chaperone.
                        
            Nice summary here:
            https://www.cell.com/cell-reports/fulltext/S2211-1247(21)01334-6?_returnURL=https%3A%2F%2Flinkinghub.elsevier.com%2Fretrieve%2Fpii%2FS2211124721013346%3Fshowall%3Dtrue
                        
            https://www.cell.com/cms/attachment/26c358d2-4bdb-4cde-8725-4a4eaf33908e/gr7.jpg
            and in figure 7 (which shows the various complexes that TTT chaperones)
                        
            Also, need to consider that (Rvb1p and Rvb2p in yeast, Pontin52 and Reptin52 in humans)
            have an additional role in some complexes because they are part of the final assemlby. However this isn't the case for the "phosphatidylinositol-3 kinase-related protein kinase", so any annotations to this (or TOR signalling) are indirect)""";


    @Test
    protected void doParse() throws ParseException {
        new IssueTextParser(new StringReader(tt)).parse(new IssueTextParserHandler() {
            @Override
            public void handleUrl(String url) {
                System.out.println(url);
            }

            @Override
            public void handleOboId(String oboId) {
                System.out.println(oboId);
            }
        });
    }

    private void parse(String text) throws ParseException {
        var parser = new IssueTextParser(new StringReader(text));
        parser.parse(handler);
    }
}

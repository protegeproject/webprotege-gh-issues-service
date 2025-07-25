package edu.stanford.webprotege.issues;

import static org.junit.jupiter.api.Assertions.*;

import edu.stanford.webprotege.issues.entity.OboId;
import edu.stanford.webprotege.issues.entity.OboIdUtilities;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Aug 2017
 */
public class OboIdUtilitiesTest {

    @Test
    public void shouldRecogniseAsOboId() {
        MatcherAssert.assertThat(OboIdUtilities.isOboId("GO:0001234"), is(true));
    }

    @Test
    public void shouldRecogniseWithUnderscoreAsOboId() {
        assertThat(OboIdUtilities.isOboId("OTHER_THING:0001234"), is(true));
    }

    /**
     * Test for https://github.com/protegeproject/protege/issues/765
     */
    @Test
    public void shouldProcessLongName() {
        assertThat(OboIdUtilities.isOboId("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"), is(false));
    }

    @Test
    public void shouldNotRecogniseAsOboId() {
        assertThat(OboIdUtilities.isOboId("owl:Thing"), is(false));
    }

    @Test
    public void shouldRecogniseOboLibraryIri() {
        assertThat(OboIdUtilities.isOboIri(IRI.create("http://purl.obolibrary.org/obo/GO_0001234")), is(true));
    }

    @Test
    public void shouldRecogniseOboIri() {
        assertThat(OboIdUtilities.isOboIri(IRI.create("http://other.place.org/obo/MY_0001234")), is(true));
    }

    @Test
    public void shouldNotRecogniseOboIri() {
        assertThat(OboIdUtilities.isOboIri(OWLRDFVocabulary.RDFS_LABEL.getIRI()), is(false));
    }

    @Test
    public void shouldParseOboId() {
        IRI iri = OboIdUtilities.getOboLibraryIriFromOboId("GO:0001234");
        assertThat(iri.toString(), is("http://purl.obolibrary.org/obo/GO_0001234"));
    }

    @Test
    public void shouldParseOboIdWithUnderscore() {
        IRI iri = OboIdUtilities.getOboLibraryIriFromOboId("OTHER_THING:0001234");
        assertThat(iri.toString(), is("http://purl.obolibrary.org/obo/OTHER_THING_0001234"));
    }

    @Test
    public void shouldThrowRuntimeException() {
        assertThrows(RuntimeException.class, () -> {
            OboIdUtilities.getOboLibraryIriFromOboId("owl:Thing");
        });
    }

    @Test
    public void shouldGetOboIdFromIri() {
        Optional<OboId> id = OboIdUtilities.getOboIdFromIri(IRI.create("http://purl.obolibrary.org/obo/GO_0001234"));
        assertThat(id, is(Optional.of(OboId.valueOf("GO:0001234"))));
    }

    @Test
    public void shouldGetOboIdFromIriWithUnderscoreLocalPart() {
        Optional<OboId> id = OboIdUtilities.getOboIdFromIri(IRI.create("http://purl.obolibrary.org/obo/OTHER_THING_0001234"));
        assertThat(id, is(Optional.of(OboId.valueOf("OTHER_THING:0001234"))));
    }

    @Test
    public void shouldNotGetOboIdFromInvalidOboIri() {
        Optional<OboId> id = OboIdUtilities.getOboIdFromIri(IRI.create("http://purl.obolibrary.org/obo/otherthing"));
        assertThat(id.isPresent(), is(false));
    }
}
package edu.stanford.protege.issues.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OboIdResolverImplTest {

    private OboIdResolverImpl resolver;

    @BeforeEach
    void setUp() {
        resolver = new OboIdResolverImpl();
    }

    @Test
    void shouldResolveOboId() {
        var iri = resolver.resolveOboId("GO:000123");
        assertThat(iri).contains(IRI.create("http://purl.obolibrary.org/obo/GO_000123"));
    }

    @Test
    void shouldNotResolveOboId() {
        var iri = resolver.resolveOboId("Something Else");
        assertThat(iri).isEmpty();
    }

    @Test
    void shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
           resolver.resolveOboId(null);
        });
    }
}
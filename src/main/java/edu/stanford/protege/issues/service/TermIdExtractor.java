package edu.stanford.protege.issues.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-21
 */
@Component
public class TermIdExtractor {

    private static final Logger logger = LoggerFactory.getLogger(TermIdExtractor.class);

    private final IssueTextParserFactory parserFactory;


    public TermIdExtractor(IssueTextParserFactory parserFactory) {
        this.parserFactory = Objects.requireNonNull(parserFactory);
    }


    /**
     * Extracts Term Ids from the provided text.
     * <p>
     * This method parses the input text and identifies term IDs (OBO IDs and IRIs).
     * It populates sets of OBO IDs and IRIs based on
     * the identified terms and returns an {@link ExtractedTermIds} object containing these sets.
     * </p>
     *
     * @param text The text from which term IDs need to be extracted. It should not be {@code null}.
     * @return An {@link ExtractedTermIds} object containing the extracted OBO IDs and IRIs.
     *
     * @throws NullPointerException if {@code text} is {@code null}.
     */
    @Nonnull
    public ExtractedTermIds extractTermIds(@Nonnull String text) {
        Objects.requireNonNull(text);
        var oboIds = new HashSet<OboId>();
        var iris = new HashSet<Iri>();
        var parser = parserFactory.createParser(text);
        try {
            parser.parse(new IssueTextParserHandler() {
                @Override
                public void handleUrl(String url) {
                    iris.add(Iri.valueOf(url));
                }

                @Override
                public void handleOboId(String oboId) {
                    oboIds.add(OboId.valueOf(oboId));
                }
            });
        } catch (ParseException e) {
            logger.debug("Error parsing issue", e);
        }
        return new ExtractedTermIds(oboIds, iris);
    }


    public record ExtractedTermIds(@Nonnull Set<OboId> extractedOboIds,
                                   @Nonnull Set<Iri> extractedIris) {

        public ExtractedTermIds(Set<OboId> extractedOboIds, Set<Iri> extractedIris) {
            this.extractedOboIds = Set.copyOf(extractedOboIds);
            this.extractedIris = Set.copyOf(extractedIris);
        }
    }
}

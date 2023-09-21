package edu.stanford.protege.issues.service;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-15
 */
@Component
public class MentionedEntitiesExtractor {

    private static final Logger logger = LoggerFactory.getLogger(MentionedEntitiesExtractor.class);

    private final IssueTextParserFactory parserFactory;

    private final EntityResolver entityResolver;

    private final OboIdResolver oboIdResolver;

    public MentionedEntitiesExtractor(IssueTextParserFactory parserFactory, EntityResolver entityResolver,
                                      OboIdResolver oboIdResolver) {
        this.parserFactory = parserFactory;
        this.entityResolver = entityResolver;
        this.oboIdResolver = oboIdResolver;
    }

    @Nonnull
    public Set<OWLEntity> getMentionedEntities(@Nonnull String text) {
        var parser = parserFactory.createParser(text);
        try {
            var handler = new EntityMentionParserHandler();
            parser.parse(handler);
            return handler.getEntities();
        } catch (ParseException e) {
            logger.info("Error when parsing mentioned entities", e);
            return Set.of();
        }
    }



    private class EntityMentionParserHandler implements IssueTextParserHandler {

        private final Set<OWLEntity> entities = new HashSet<>();

        @Override
        public void handleUrl(String url) {
            var iri = IRI.create(url);
            entities.addAll(entityResolver.getEntities(iri));
        }

        @Override
        public void handleOboId(String oboId) {
            oboIdResolver.resolveOboId(oboId)
                    .map(entityResolver::getEntities)
                    .ifPresent(entities::addAll);
        }

        public Set<OWLEntity> getEntities() {
            return entities;
        }
    }

}

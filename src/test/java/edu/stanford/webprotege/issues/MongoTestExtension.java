package edu.stanford.webprotege.issues;

import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-26
 */
public class MongoTestExtension  implements BeforeEachCallback, AfterEachCallback {

    private final Logger logger = LoggerFactory.getLogger(MongoTestExtension.class);

    protected static final int MONGODB_PORT = 27017;

    private MongoDBContainer container;

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        var imageName = DockerImageName.parse("mongo:4.0.10");
        container = new MongoDBContainer(imageName)
                .withExposedPorts(MONGODB_PORT);
        container.start();
        logger.info("Started Mongo on port {}", container.getMappedPort(MONGODB_PORT));
        System.setProperty("spring.data.mongodb.port", Integer.toString(container.getMappedPort(MONGODB_PORT)));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
    }
}

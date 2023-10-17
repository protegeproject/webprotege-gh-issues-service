package edu.stanford.protege.issues.service;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-26
 */
public class MongoTestExtension  implements BeforeAllCallback, AfterAllCallback {

    protected static final int MONGODB_PORT = 27017;

    private MongoDBContainer container;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        var imageName = DockerImageName.parse("mongo:4.0.10");
        container = new MongoDBContainer(imageName)
                .withExposedPorts(MONGODB_PORT);
        container.start();
    }

    @DynamicPropertySource
    public void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", () -> "localhost");
        registry.add("spring.data.mongodb.port", () -> container.getMappedPort(MONGODB_PORT));
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        container.stop();
    }
}

package edu.stanford.protege.issues.service;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-26
 */
public class MongoTestExtension  implements BeforeAllCallback, AfterAllCallback {

    private MongoDBContainer container;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        var imageName = DockerImageName.parse("mongo:4.0.10");
        container = new MongoDBContainer(imageName)
                .withExposedPorts(27017);
        container.start();

        var mappedPort = container.getMappedPort(27017);
        // spring.data.mongodb.host=localhost
        //spring.data.mongodb.port=27017
        System.setProperty("spring.data.mongodb.host", "localhost");
        System.setProperty("spring.data.mongodb.port", Integer.toString(mappedPort));
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        container.stop();
    }
}

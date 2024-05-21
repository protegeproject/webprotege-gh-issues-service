package edu.stanford.protege.issues.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
class WebprotegeGhIssuesServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}

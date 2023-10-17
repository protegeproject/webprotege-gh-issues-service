package edu.stanford.protege.issues.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-16
 */
@RestController
public class GitHubWebhookController {

    @PostMapping(path = "/", consumes = "application/json")
    public HttpStatus handleWebhook(@RequestBody GitHubWebhookIssueEventPayload payload) {
        // TODO: Handle incremental updates
        return HttpStatus.ACCEPTED;
    }

}

package edu.stanford.protege.issues.service;

import org.apache.pulsar.client.api.url.URL;
import org.kohsuke.github.GHEvent;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-16
 */
@Component
public class GitHubWebHookConfigurationManager {

    private final Logger logger = LoggerFactory.getLogger(GitHubWebHookConfigurationManager.class);

    private final GitHub gitHub;

    private final GitHubWebhookRecordRepository webHookRepo;

    private final String webhookUrl;

    public GitHubWebHookConfigurationManager(GitHub gitHub, GitHubWebhookRecordRepository webHookRepo,
                                             @Value("${webprotege.github.webhook.url:}") String webhookUrl) {
        this.gitHub = gitHub;
        this.webHookRepo = webHookRepo;
        this.webhookUrl = Objects.requireNonNull(webhookUrl);
    }

    public void installWebHook(GitHubRepositoryCoordinates repoCoords) throws IOException {
        try {
            if(webhookUrl.isEmpty()) {
                logger.info("No GitHub webhook URL provided.  Skipping installation.");
                return;
            }
            var repository = gitHub.getRepository(repoCoords.getFullName());
            var webhook = repository
                    .createWebHook(URL.createURL(webhookUrl), Set.of(GHEvent.ISSUES));
            var webhookRecord = new GitHubWebhookRecord(webhook.getNodeId(), webhook.getId(), webhook.getName());
            webHookRepo.save(webhookRecord);
            logger.info("Installed webhook on {}", repoCoords);

        }
        catch (HttpException e) {
            System.err.println(e.getMessage());
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

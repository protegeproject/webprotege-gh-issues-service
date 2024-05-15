package edu.stanford.protege.issues.service;

import edu.stanford.protege.github.issues.server.GetGitHubIssuesRequest;
import edu.stanford.protege.github.issues.server.GetGitHubIssuesResponse;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Resource;
import edu.stanford.protege.webprotege.ipc.AuthorizedCommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-14
 */
@WebProtegeHandler
public class GetGitHubIssuesRequestHandler implements AuthorizedCommandHandler<GetGitHubIssuesRequest, GetGitHubIssuesResponse> {

    public static final ActionId ACTION_ID = ActionId.valueOf("GetGitHubIssues");

    private final GitHubIssuesManager issuesManager;

    public GetGitHubIssuesRequestHandler(GitHubIssuesManager issuesManager) {
        this.issuesManager = issuesManager;
    }

    @Nonnull
    @Override
    public Resource getTargetResource(GetGitHubIssuesRequest request) {
        return ProjectResource.forProject(request.projectId());
    }

    @Nonnull
    @Override
    public Collection<ActionId> getRequiredCapabilities() {
        return List.of(ACTION_ID);
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetGitHubIssuesRequest.CHANNEL;
    }

    @Override
    public Class<GetGitHubIssuesRequest> getRequestClass() {
        return GetGitHubIssuesRequest.class;
    }

    @Override
    public Mono<GetGitHubIssuesResponse> handleRequest(GetGitHubIssuesRequest request,
                                                       ExecutionContext executionContext) {

        var projectId = request.projectId();
        var entity = request.entity();
        var issues = issuesManager.getIssues(projectId, entity);

        var response = new GetGitHubIssuesResponse(projectId, entity, issues);
        return Mono.just(response);
    }
}

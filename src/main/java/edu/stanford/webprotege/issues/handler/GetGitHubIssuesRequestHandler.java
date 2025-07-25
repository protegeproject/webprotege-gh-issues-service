package edu.stanford.webprotege.issues.handler;

import edu.stanford.protege.github.issues.GetGitHubIssuesRequest;
import edu.stanford.protege.github.issues.GetGitHubIssuesResponse;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.webprotege.issues.service.GitHubIssuesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-14
 */
@WebProtegeHandler
public class GetGitHubIssuesRequestHandler implements CommandHandler<GetGitHubIssuesRequest, GetGitHubIssuesResponse> {

    private static final Logger logger = LoggerFactory.getLogger(GetGitHubIssuesRequestHandler.class);

    public static final ActionId ACTION_ID = ActionId.valueOf("GetGitHubIssues");

    private final GitHubIssuesService issuesManager;

    public GetGitHubIssuesRequestHandler(GitHubIssuesService issuesManager) {
        this.issuesManager = issuesManager;
    }
//
//    @Nonnull
//    @Override
//    public Resource getTargetResource(GetGitHubIssuesRequest request) {
//        return ProjectResource.forProject(request.projectId());
//    }
//
//    @Nonnull
//    @Override
//    public Collection<ActionId> getRequiredCapabilities() {
//        return List.of(ACTION_ID);
//    }

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
        logger.info("Retrieved issues for {}.  There are {} issues", request, issues.size());
        var response = new GetGitHubIssuesResponse(projectId, entity, issues);
        return Mono.just(response);
    }
}

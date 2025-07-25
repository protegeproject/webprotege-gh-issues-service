package edu.stanford.webprotege.issues;

import edu.stanford.protege.github.issues.GetGitHubIssuesRequest;
import edu.stanford.protege.github.issues.GitHubIssue;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.webprotege.issues.handler.GetGitHubIssuesRequestHandler;
import edu.stanford.webprotege.issues.service.GitHubIssuesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@ExtendWith(MongoTestExtension.class)
class GetGitHubIssuesRequestHandlerTest {

    private GetGitHubIssuesRequestHandler handler;

    @Mock
    private GitHubIssuesService issuesManager;

    @Mock
    private ProjectId projectId;

    @Mock
    private OWLEntity entity;

    private final List<GitHubIssue> issues = List.of(mock(GitHubIssue.class));

    @BeforeEach
    void setUp() {
        when(issuesManager.getIssues(projectId, entity)).thenReturn(issues);
        handler = new GetGitHubIssuesRequestHandler(issuesManager);
    }

    @Test
    void shouldHandleRequest() {
        var responseMono = handler.handleRequest(new GetGitHubIssuesRequest(projectId, entity), new ExecutionContext());
        var response = responseMono.block();
        assertThat(response).isNotNull();
        assertThat(response.projectId()).isEqualTo(projectId);
        assertThat(response.entity()).isEqualTo(entity);
        assertThat(response.issues()).isEqualTo(issues);
    }

    @Test
    public void shouldHandleCorrectChannel() {
        assertThat(handler.getChannelName()).isEqualTo("webprotege.issues.GetGitHubIssues");
    }

//    @Test
//    public void shouldHaveCorrectActionId() {
//        assertThat(handler.getRequiredCapabilities())
//                .contains(ActionId.valueOf("GetGitHubIssues"));
//    }

    @Test
    public void shouldHaveCorrectRequestClass() {
        assertThat(handler.getRequestClass())
                .isEqualTo(GetGitHubIssuesRequest.class);
    }

    @Test
    public void shouldBeWebProtegeHandler() {
        assertThat(GetGitHubIssuesRequestHandler.class).hasAnnotation(WebProtegeHandler.class);
    }
}
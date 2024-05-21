package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.TestObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-07-13
 */
public class GitHubCommentTest {

    protected static final long ID = 1L;

    protected static final String NODE_ID = "MDEyOklzc3VlQ29tbWVudDE=";

    protected static final String URL = "https://api.github.com/repos/octocat/Hello-World/issues/comments/1";

    protected static final String HTML_URL = "https://github.com/octocat/Hello-World/issues/1347#issuecomment-1";

    protected static final String BODY = "Me too";

    protected static final Instant CREATED_AT = Instant.parse("2011-04-14T16:00:49Z");

    protected static final Instant UPDATED_AT = Instant.parse("2011-04-14T16:00:49Z");

    protected static final String ISSUE_URL = "https://api.github.com/repos/octocat/Hello-World/issues/1347";

    protected static final GitHubAuthorAssociation AUTHOR_ASSOCIATION = GitHubAuthorAssociation.COLLABORATOR;

    protected JacksonTester<GitHubComment> tester;


    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = TestObjectMapper.getObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void testGitHubCommentProperties() {
        GitHubUser user = GitHubTestUser.getTestUser();

        GitHubComment comment = getComment(user);

        assertThat(comment.id()).isEqualTo(ID);
        assertThat(comment.nodeId()).isEqualTo(NODE_ID);
        assertThat(comment.url()).isEqualTo(URL);
        assertThat(comment.htmlUrl()).isEqualTo(HTML_URL);
        assertThat(comment.body()).isEqualTo(BODY);
        assertThat(comment.user()).isEqualTo(user);
        assertThat(comment.createdAt()).isEqualTo(CREATED_AT);
        assertThat(comment.updatedAt()).isEqualTo(UPDATED_AT);
        assertThat(comment.issueUrl()).isEqualTo(ISSUE_URL);
        assertThat(comment.authorAssociation()).isEqualTo(AUTHOR_ASSOCIATION);
    }

    protected static GitHubComment getComment(GitHubUser user) {
        return GitHubComment.get(ID,
                                 NODE_ID,
                                 URL,
                                 HTML_URL,
                                 BODY,
                                 user,
                                 CREATED_AT,
                                 UPDATED_AT,
                                 ISSUE_URL,
                                 AUTHOR_ASSOCIATION);
    }

    @Test
    public void testGitHubCommentWithNullValues() {
        GitHubComment comment = GitHubComment.get(2L, null, null, null, null, null, null, null, null, null);

        assertThat(comment.nodeId()).isEmpty();
        assertThat(comment.url()).isEmpty();
        assertThat(comment.htmlUrl()).isEmpty();
        assertThat(comment.body()).isEmpty();
        assertThat(comment.user()).isEqualTo(GitHubUser.empty());
        assertThat(comment.createdAt()).isEqualTo(Instant.EPOCH);
        assertThat(comment.updatedAt()).isEqualTo(Instant.EPOCH);
        assertThat(comment.issueUrl()).isEmpty();
        assertThat(comment.authorAssociation()).isEqualTo(GitHubAuthorAssociation.NONE);
    }

    @Test
    public void testGitHubCommentEmptyConstructor() {
        GitHubComment comment = GitHubComment.empty();

        assertThat(comment.id()).isEqualTo(0L);
        assertThat(comment.nodeId()).isEmpty();
        assertThat(comment.url()).isEmpty();
        assertThat(comment.htmlUrl()).isEmpty();
        assertThat(comment.body()).isEmpty();
        assertThat(comment.user()).isEqualTo(GitHubUser.empty());
        assertThat(comment.createdAt()).isEqualTo(Instant.EPOCH);
        assertThat(comment.updatedAt()).isEqualTo(Instant.EPOCH);
        assertThat(comment.issueUrl()).isEmpty();
        assertThat(comment.authorAssociation()).isEqualTo(GitHubAuthorAssociation.NONE);
    }

    @Test
    public void shouldParseJson() throws IOException {
        GitHubComment comment = tester.readObject(GitHubCommentTest.class.getResourceAsStream("/comment.json"));

        assertThat(comment.id()).isEqualTo(ID);
        assertThat(comment.nodeId()).isEqualTo(NODE_ID);
        assertThat(comment.url()).isEqualTo(URL);
        assertThat(comment.htmlUrl()).isEqualTo(HTML_URL);
        assertThat(comment.body()).isEqualTo(BODY);
        assertThat(comment.user()).isEqualTo(GitHubTestUser.getTestUser());
        assertThat(comment.createdAt()).isEqualTo(CREATED_AT);
        assertThat(comment.updatedAt()).isEqualTo(UPDATED_AT);
        assertThat(comment.issueUrl()).isEqualTo(ISSUE_URL);
        assertThat(comment.authorAssociation()).isEqualTo(AUTHOR_ASSOCIATION);
    }

    @Test
    public void shouldWriteJson() throws IOException {
        GitHubComment comment = getComment(GitHubUser.empty());
        org.springframework.boot.test.json.JsonContent<GitHubComment> content = tester.write(comment);
        assertThat(content).hasJsonPathNumberValue("id", ID);
        assertThat(content).hasJsonPathStringValue("node_id", NODE_ID);
        assertThat(content).hasJsonPathStringValue("url", URL);
        assertThat(content).hasJsonPathStringValue("html_url", HTML_URL);
        assertThat(content).hasJsonPathStringValue("body", BODY);
        assertThat(content).hasJsonPathValue("user");
        assertThat(content).hasJsonPathStringValue("created_at", CREATED_AT.toString());
        assertThat(content).hasJsonPathStringValue("updated_at", UPDATED_AT.toString());
        assertThat(content).hasJsonPathStringValue("issue_url", ISSUE_URL);
        assertThat(content).hasJsonPathStringValue("author_association", AUTHOR_ASSOCIATION.toString());
    }


    @Test
    public void shouldBeGwtSerializable() {
        assertThat(GitHubComment.class.getInterfaces()).contains(IsSerializable.class);

        GwtCompatible anno = GitHubComment.class.getAnnotation(GwtCompatible.class);
        assertThat(anno).isNotNull();
    }

}

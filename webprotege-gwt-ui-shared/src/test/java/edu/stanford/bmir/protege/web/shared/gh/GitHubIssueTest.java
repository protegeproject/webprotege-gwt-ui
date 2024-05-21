package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.TestObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class GitHubIssueTest {

    private static final long ID = 1;

    private static final String NODE_ID = "MDU6SXNzdWUx";

    private static final String URL = "https://api.github.com/repos/octocat/Hello-World/issues/1347";

    private static final int NUMBER = 1347;

    private static final GitHubState STATE = GitHubState.OPEN;

    private static final String TITLE = "Found a bug";

    private static final String BODY = "I'm having a problem with this.";

    private static final boolean LOCKED = true;

    private static final String ACTIVE_LOCK_REASON = "too heated";

    private static final int COMMENTS = 0;

    private static final GitHubTimeStamp CLOSED_AT = GitHubTimeStamp.epoch();

    private static final GitHubTimeStamp CREATED_AT = GitHubTimeStamp.valueOf("2011-04-22T13:33:48Z");

    private static final GitHubTimeStamp UPDATED_AT = GitHubTimeStamp.valueOf("2011-04-22T13:33:48Z");

    private static final GitHubUser CLOSED_BY = GitHubTestUser.getTestUser();

    private static final GitHubAuthorAssociation AUTHOR_ASSOCIATION = GitHubAuthorAssociation.COLLABORATOR;

    private static final GitHubStateReason STATE_REASON = GitHubStateReason.COMPLETED;

    protected static final String HTML_URL = "https://example.org/html/issue";



    private JacksonTester<GitHubIssue> tester;


    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = TestObjectMapper.getObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void shouldParseJson() throws IOException {
        InputStream inputStream = GitHubIssueTest.class.getResourceAsStream("/issue.json");
        GitHubIssue issue = tester.readObject(inputStream);
        assertThat(issue.id()).isEqualTo(ID);
        assertThat(issue.nodeId()).isEqualTo(NODE_ID);
        assertThat(issue.url()).isEqualTo(URL);
        assertThat(issue.number()).isEqualTo(NUMBER);
        assertThat(issue.state()).isEqualTo(STATE);
        assertThat(issue.title()).isEqualTo(TITLE);
        assertThat(issue.body()).isEqualTo(BODY);
        assertThat(issue.locked()).isEqualTo(LOCKED);
        assertThat(issue.activeLockReason()).isEqualTo(ACTIVE_LOCK_REASON);
        assertThat(issue.comments()).isEqualTo(COMMENTS);
        assertThat(issue.createdAt()).isEqualTo(CREATED_AT);
        assertThat(issue.updatedAt()).isEqualTo(UPDATED_AT);
        assertThat(issue.closedAt()).isEqualTo(CLOSED_AT);
        assertThat(issue.closedBy()).isEqualTo(CLOSED_BY);
        assertThat(issue.authorAssociation()).isEqualTo(AUTHOR_ASSOCIATION);
        assertThat(issue.stateReason()).isEqualTo(STATE_REASON);
    }

    @Test
    public void shouldWriteJson() throws IOException {
        JsonContent<GitHubIssue> content = tester.write(GitHubIssue.get(
                URL,
                ID,
                NODE_ID,
                NUMBER,
                TITLE,
                GitHubTestUser.getTestUser(),
                Collections.emptyList(), HTML_URL,
                STATE,
                LOCKED,
                GitHubTestUser.getTestUser(),
                Arrays.asList(GitHubTestUser.getTestUser()),
                GitHubMilestoneTest.getMilestone(),
                COMMENTS,
                CREATED_AT,
                UPDATED_AT,
                CLOSED_AT,
                GitHubTestUser.getTestUser(),
                AUTHOR_ASSOCIATION,
                ACTIVE_LOCK_REASON,
                BODY,
                GitHubReactionsTest.getReactions(),
                STATE_REASON
        ));
        assertThat(content).hasJsonPathStringValue("url", URL);
        assertThat(content).hasJsonPathNumberValue("id", ID);
        assertThat(content).hasJsonPathStringValue("node_id", NODE_ID);
        assertThat(content).hasJsonPathNumberValue("number", NUMBER);
        assertThat(content).hasJsonPathStringValue("title", TITLE);
        assertThat(content).hasJsonPathValue("assignee");
        assertThat(content).hasJsonPathArrayValue("assignees");
        assertThat(content).hasJsonPathStringValue("html_url", HTML_URL);


    }
}
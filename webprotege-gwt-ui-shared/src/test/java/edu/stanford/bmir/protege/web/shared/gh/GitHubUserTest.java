package edu.stanford.bmir.protege.web.shared.gh;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-19
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gwt.user.client.rpc.IsSerializable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class GitHubUserTest {

    protected static final String NODE_ID = "MDQ6VXNlcjE=";

    protected static final String LOGIN = "octocat";

    protected static final String AVATAR_URL = "https://github.com/images/error/octocat_happy.gif";

    protected static final String URL = "https://api.github.com/users/octocat";

    protected static final String HTML_URL = "https://github.com/octocat";

    private JacksonTester<GitHubUser> tester;


    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void shouldParseUser() throws IOException {
        InputStream jsonInputStream = GitHubUserTest.class.getResourceAsStream("/user.json");
        ObjectContent<GitHubUser> content = tester.read(jsonInputStream);
        GitHubUser user = content.getObject();

        assertThat(user.login()).isEqualTo(LOGIN);
        assertThat(user.id()).isEqualTo(1);
        assertThat(user.nodeId()).isEqualTo(NODE_ID);
        assertThat(user.avatarUrl()).isEqualTo(AVATAR_URL);
        assertThat(user.htmlUrl()).isEqualTo(HTML_URL);
        assertThat(user.type()).isEqualTo(GitHubUserType.USER);
        assertThat(user.siteAdmin()).isFalse();
    }

    @Test
    void shouldWriteUser() throws IOException {
        GitHubUser user = getUser();
        JsonContent<GitHubUser> content = tester.write(user);
        assertThat(content).hasJsonPathValue("login", LOGIN);
        assertThat(content).hasJsonPathValue("id", 1);
        assertThat(content).hasJsonPathValue("node_id", NODE_ID);
        assertThat(content).hasJsonPathValue("avatar_url", AVATAR_URL);
        assertThat(content).hasJsonPathValue("url", URL);
        assertThat(content).hasJsonPathValue("html_url", HTML_URL);
        assertThat(content).hasJsonPathValue("type", "User");
        assertThat(content).hasJsonPathValue("site_admin", false);
    }

    protected static GitHubUser getUser() {
        return GitHubUser.get(LOGIN, 1, NODE_ID, AVATAR_URL, URL, HTML_URL, GitHubUserType.USER, false);
    }

    @Test
    public void testGitHubUserProperties() {
        GitHubUser user = getUser();
        assertThat(user.login()).isEqualTo(LOGIN);
        assertThat(user.id()).isEqualTo(1);
        assertThat(user.nodeId()).isEqualTo(NODE_ID);
        assertThat(user.avatarUrl()).isEqualTo(AVATAR_URL);
        assertThat(user.htmlUrl()).isEqualTo(HTML_URL);
        assertThat(user.type()).isEqualTo(GitHubUserType.USER);
        assertThat(user.siteAdmin()).isFalse();
    }

    @Test
    public void testGitHubUserWithNullValues() {
        GitHubUser user = GitHubUser.get(null, 1, null, null, null, null, null, false);

        assertThat(user.login()).isEmpty();
        assertThat(user.id()).isEqualTo(1);
        assertThat(user.nodeId()).isEmpty();
        assertThat(user.avatarUrl()).isEmpty();
        assertThat(user.htmlUrl()).isEmpty();
        assertThat(user.type()).isEqualTo(GitHubUserType.USER);
        assertThat(user.siteAdmin()).isFalse();
    }

    @Test
    public void shouldGetEmptyUser() {
        GitHubUser user = GitHubUser.empty();
        assertThat(user.login()).isEmpty();
        assertThat(user.id()).isEqualTo(0);
        assertThat(user.nodeId()).isEmpty();
        assertThat(user.avatarUrl()).isEmpty();
        assertThat(user.htmlUrl()).isEmpty();
        assertThat(user.type()).isEqualTo(GitHubUserType.USER);
        assertThat(user.siteAdmin()).isFalse();
    }

}

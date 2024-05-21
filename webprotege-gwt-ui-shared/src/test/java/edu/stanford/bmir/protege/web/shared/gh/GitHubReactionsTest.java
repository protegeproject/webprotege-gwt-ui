package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-07-14
 */
public class GitHubReactionsTest {

    protected static final String URL = "https://example.org/reactions";

    protected static final int TOTAL_COUNT = 36;

    protected static final int PLUS_1_COUNT = 1;

    protected static final int MINUS_1_COUNT = 2;

    protected static final int LAUGH_COUNT = 3;

    protected static final int HOORAY_COUNT = 4;

    protected static final int CONFUSED_COUNT = 5;

    protected static final int HEART_COUNT = 6;

    protected static final int ROCKET_COUNT = 7;

    protected static final int EYES_COUNT = 8;

    private JacksonTester<GitHubReactions> tester;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void shouldParseJson() throws IOException {
        GitHubReactions reactions = tester.readObject(GitHubReactionsTest.class.getResourceAsStream("/reactions.json"));
        assertThat(reactions.url()).isEqualTo(URL);
        assertThat(reactions.totalCount()).isEqualTo(TOTAL_COUNT);
        assertThat(reactions.plus1()).isEqualTo(PLUS_1_COUNT);
        assertThat(reactions.minus1()).isEqualTo(MINUS_1_COUNT);
        assertThat(reactions.laugh()).isEqualTo(LAUGH_COUNT);
        assertThat(reactions.hooray()).isEqualTo(HOORAY_COUNT);
        assertThat(reactions.confused()).isEqualTo(CONFUSED_COUNT);
        assertThat(reactions.heart()).isEqualTo(HEART_COUNT);
        assertThat(reactions.rocket()).isEqualTo(ROCKET_COUNT);
        assertThat(reactions.eyes()).isEqualTo(EYES_COUNT);
    }

    @Test
    public void shouldWriteJson() throws IOException {
        GitHubReactions reactions = getReactions();
        JsonContent<GitHubReactions> content = tester.write(reactions);
        assertThat(content).hasJsonPathStringValue("url", URL);
        assertThat(content).hasJsonPathNumberValue("total_count", TOTAL_COUNT);
        assertThat(content).hasJsonPathNumberValue("+1", PLUS_1_COUNT);
        assertThat(content).hasJsonPathNumberValue("-1", MINUS_1_COUNT);
        assertThat(content).hasJsonPathNumberValue("laugh", LAUGH_COUNT);
        assertThat(content).hasJsonPathNumberValue("hooray", HOORAY_COUNT);
        assertThat(content).hasJsonPathNumberValue("confused", CONFUSED_COUNT);
        assertThat(content).hasJsonPathNumberValue("heart", HEART_COUNT);
        assertThat(content).hasJsonPathNumberValue("rocket", ROCKET_COUNT);
        assertThat(content).hasJsonPathNumberValue("eyes", EYES_COUNT);
    }

    public static GitHubReactions getReactions() {
        return GitHubReactions.get(URL,
                                   TOTAL_COUNT,
                                   PLUS_1_COUNT,
                                   MINUS_1_COUNT,
                                   LAUGH_COUNT,
                                   HOORAY_COUNT,
                                   CONFUSED_COUNT,
                                   HEART_COUNT,
                                   ROCKET_COUNT,
                                   EYES_COUNT);
    }


    @Test
    public void testGitHubReactionsProperties() {
        GitHubReactions reactions = GitHubReactions.get(
                "https://example.com/reactions",
                10,
                3,
                2,
                1,
                4,
                0,
                5,
                2,
                2
        );

        assertThat(reactions.url()).isEqualTo("https://example.com/reactions");
        assertThat(reactions.totalCount()).isEqualTo(10);
        assertThat(reactions.plus1()).isEqualTo(3);
        assertThat(reactions.minus1()).isEqualTo(2);
        assertThat(reactions.laugh()).isEqualTo(1);
        assertThat(reactions.hooray()).isEqualTo(4);
        assertThat(reactions.confused()).isEqualTo(0);
        assertThat(reactions.heart()).isEqualTo(5);
        assertThat(reactions.rocket()).isEqualTo(2);
        assertThat(reactions.eyes()).isEqualTo(2);
    }

    @Test
    public void testGitHubReactionsWithZeroValues() {
        GitHubReactions reactions = GitHubReactions.get(
                "https://example.com/reactions",
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );

        assertThat(reactions.url()).isEqualTo("https://example.com/reactions");
        assertThat(reactions.totalCount()).isEqualTo(0);
        assertThat(reactions.plus1()).isEqualTo(0);
        assertThat(reactions.minus1()).isEqualTo(0);
        assertThat(reactions.laugh()).isEqualTo(0);
        assertThat(reactions.hooray()).isEqualTo(0);
        assertThat(reactions.confused()).isEqualTo(0);
        assertThat(reactions.heart()).isEqualTo(0);
        assertThat(reactions.rocket()).isEqualTo(0);
        assertThat(reactions.eyes()).isEqualTo(0);
    }

    @Test
    public void testGitHubReactionsDefaultConstructor() {
        GitHubReactions reactions = GitHubReactions.get(
                null,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );

        assertThat(reactions.url()).isEmpty();
        assertThat(reactions.totalCount()).isEqualTo(0);
        assertThat(reactions.plus1()).isEqualTo(0);
        assertThat(reactions.minus1()).isEqualTo(0);
        assertThat(reactions.laugh()).isEqualTo(0);
        assertThat(reactions.hooray()).isEqualTo(0);
        assertThat(reactions.confused()).isEqualTo(0);
        assertThat(reactions.heart()).isEqualTo(0);
        assertThat(reactions.rocket()).isEqualTo(0);
        assertThat(reactions.eyes()).isEqualTo(0);
    }

}

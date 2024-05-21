package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GitHubRepositoryCoordinatesTest {

    protected static final String TEST_OWNER_NAME = "TestOwnerName";

    protected static final String TEST_REPO_NAME = "TestRepoName";

    private GitHubRepositoryCoordinates coords;

    private JacksonTester<GitHubRepositoryCoordinates> jacksonTester;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        coords = GitHubRepositoryCoordinates.of(TEST_OWNER_NAME, TEST_REPO_NAME);
    }

    @Test
    void shouldStoreOwnerName() {
        assertThat(coords.ownerName()).isEqualTo(TEST_OWNER_NAME);
    }

    @Test
    void shouldStoreRepoName() {
        assertThat(coords.repositoryName()).isEqualTo(TEST_REPO_NAME);
    }

    @Test
    void shouldProvideFullName() {
        assertThat(coords.getFullName()).isEqualTo(TEST_OWNER_NAME + "/" + TEST_REPO_NAME);
    }

    @Test
    void shouldConstructFromFullName() {
        GitHubRepositoryCoordinates coords = GitHubRepositoryCoordinates.fromFullName("ACME/R1");
        assertThat(coords.ownerName()).isEqualTo("ACME");
        assertThat(coords.repositoryName()).isEqualTo("R1");
    }

    @Test
    void shouldThrowIaeForInvalidFullName() {
        assertThrows(IllegalArgumentException.class, () -> {
            GitHubRepositoryCoordinates.fromFullName("Incorrect full name");
        });
    }

    @Test
    void shouldThrowNpeIfOwnerNameIsNull() {
        assertThrows(NullPointerException.class, () -> {
            GitHubRepositoryCoordinates.of(null, TEST_REPO_NAME);
        });
    }

    @Test
    void shouldThrowNpeIfRepoNameIsNull() {
        assertThrows(NullPointerException.class, () -> {
            GitHubRepositoryCoordinates.of(TEST_OWNER_NAME, null);
        });
    }

    @Test
    void shouldSerializeRepositoryCoordinates() throws IOException {
        JsonContent<GitHubRepositoryCoordinates> json = jacksonTester.write(coords);
        assertThat(json).hasJsonPathStringValue("ownerName", TEST_OWNER_NAME);
        assertThat(json).hasJsonPathStringValue("repositoryName", TEST_REPO_NAME);
    }

    @Test
    void shouldDeserializeRepositoryCoodinates() throws IOException {
        String json = " { \"ownerName\" : \"TestOwnerName\", \"repositoryName\" : \"TestRepoName\"}";
        ObjectContent<GitHubRepositoryCoordinates> read = jacksonTester.read(new StringReader(json));
        GitHubRepositoryCoordinates readCoords = read.getObject();
        assertThat(readCoords.ownerName()).isEqualTo(TEST_OWNER_NAME);
        assertThat(readCoords.repositoryName()).isEqualTo(TEST_REPO_NAME);
    }
}
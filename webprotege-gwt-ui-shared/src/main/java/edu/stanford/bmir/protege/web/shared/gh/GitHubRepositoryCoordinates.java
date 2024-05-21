package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-09-21
 * <p>
 * Constructs a new GitHubRepositoryCoordinates object with the specified owner and repository names.
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GitHubRepositoryCoordinates {

    public static final RegExp NAME_PATTERN = RegExp.compile("[A-Za-z0-9_.-]+");

    public static final RegExp FULL_NAME_PATTERN = RegExp.compile("^(" + NAME_PATTERN.getSource() + ")/(" + NAME_PATTERN.getSource() + ")$");


    @JsonCreator
    @Nonnull
    public static GitHubRepositoryCoordinates of(@JsonProperty("ownerName") @Nonnull String organizationName,
                                                 @JsonProperty("repositoryName") @Nonnull String repoName) {
        return new AutoValue_GitHubRepositoryCoordinates(organizationName, repoName);
    }

    public static GitHubRepositoryCoordinates fromFullName(@Nonnull String fullName) {
        Objects.requireNonNull(fullName);
        MatchResult fullNameMatcher = FULL_NAME_PATTERN.exec(fullName);
        if (fullNameMatcher == null) {
            throw new IllegalArgumentException("Invalid full name");
        }
        return GitHubRepositoryCoordinates.of(fullNameMatcher.getGroup(1), fullNameMatcher.getGroup(2));
    }

    /**
     * Gets the full name of the GitHub repository in the format "ownerName/repositoryName."
     *
     * @return The full name of the GitHub repository.
     */
    @JsonIgnore
    public String getFullName() {
        return ownerName() + "/" + repositoryName();
    }

    @JsonProperty("ownerName")
    @Nonnull
    public abstract String ownerName();

    @JsonProperty("repositoryName")
    @Nonnull
    public abstract String repositoryName();
}
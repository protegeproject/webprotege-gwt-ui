package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-07-12
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class GitHubMilestone implements IsSerializable {

    public abstract @JsonProperty("url") String url();

    public abstract @JsonProperty("id") long id();

    public abstract @JsonProperty("node_id") String nodeId();

    public abstract @JsonProperty("number") int number();

    public abstract @JsonProperty("title") String title();

    public abstract @JsonProperty("description") String description();

    public abstract @JsonProperty("creator") GitHubUser creator();

    public abstract @JsonProperty("open_issues") int openIssues();

    public abstract @JsonProperty("closed_issues") int closedIssues();

    public abstract @JsonProperty("state") GitHubState state();

    @Nullable
    public abstract @JsonProperty("created_at") GitHubTimeStamp createdAt();

    @JsonIgnore
    @Nonnull
    public Optional<GitHubTimeStamp> getCreatedAt() {
        return Optional.ofNullable(createdAt());
    }

    @Nullable
    public abstract @JsonProperty("updated_at") GitHubTimeStamp updatedAt();

    @JsonIgnore
    @Nonnull
    public Optional<GitHubTimeStamp> getUpdatedAt() {
        return Optional.ofNullable(updatedAt());
    }

    @Nullable
    public abstract @JsonProperty("due_on") GitHubTimeStamp dueOn();

    @JsonIgnore
    @Nonnull
    public Optional<GitHubTimeStamp> getDueOn() {
        return Optional.ofNullable(dueOn());
    }

    @Nullable
    public abstract @JsonProperty("closed_at") GitHubTimeStamp closedAt();

    @JsonIgnore
    @Nonnull
    public Optional<GitHubTimeStamp> getClosedAt() {
        return Optional.ofNullable(closedAt());
    }

    @JsonCreator
    @Nonnull
    public static GitHubMilestone get(@JsonProperty("url") @Nullable String url,
                               @JsonProperty("id") long id,
                               @JsonProperty("node_id") @Nullable String nodeId,
                               @JsonProperty("number") int number,
                               @JsonProperty("title") @Nullable String title,
                               @JsonProperty("description") @Nullable String description,
                               @JsonProperty("creator") @Nullable GitHubUser creator,
                               @JsonProperty("open_issues") int openIssues,
                               @JsonProperty("closed_issues") int closedIssues,
                               @JsonProperty("state") @Nullable GitHubState state,
                               @JsonProperty("created_at") @Nullable GitHubTimeStamp createdAt,
                               @JsonProperty("updated_at") @Nullable GitHubTimeStamp updatedAt,
                               @JsonProperty("due_on") @Nullable GitHubTimeStamp dueOn,
                               @JsonProperty("closed_at") @Nullable GitHubTimeStamp closedAt) {
        return new AutoValue_GitHubMilestone(
                Helper.requireNonNullElse(url, ""),
                id,
                Helper.requireNonNullElse(nodeId, ""),
                number,
                Helper.requireNonNullElse(title, ""),
                Helper.requireNonNullElse(description, ""),
                Helper.requireNonNullElse(creator, GitHubUser.empty()),
                Helper.requireNonNullElse(openIssues, 0),
                closedIssues,
                Helper.requireNonNullElse(state, GitHubState.OPEN),
                createdAt,
                updatedAt,
                dueOn,
                closedAt
        );
    }
}

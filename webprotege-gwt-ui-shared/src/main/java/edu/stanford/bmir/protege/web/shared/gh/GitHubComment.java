package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-07-13
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class GitHubComment implements IsSerializable {

    @JsonProperty("id")
    public abstract long id();

    @JsonProperty("node_id")
    public abstract String nodeId();

    @JsonProperty("url")
    public abstract String url();

    @JsonProperty("html_url")
    public abstract String htmlUrl();

    @JsonProperty("body")
    public abstract String body();

    @JsonProperty("user")
    public abstract GitHubUser user();

    @JsonProperty("created_at")
    public abstract GitHubTimeStamp createdAt();

    @JsonProperty("updated_at")
    public abstract GitHubTimeStamp updatedAt();

    @JsonProperty("issue_url")
    public abstract String issueUrl();

    @JsonProperty("author_association")
    public abstract GitHubAuthorAssociation authorAssociation();

    @Nonnull
    @JsonCreator
    public static GitHubComment get(@JsonProperty("id") long id,
                                    @JsonProperty("node_id") String nodeId,
                                    @JsonProperty("url") String url,
                                    @JsonProperty("html_url") String htmlUrl,
                                    @JsonProperty("body") String body,
                                    @JsonProperty("user") GitHubUser user,
                                    @JsonProperty("created_at") GitHubTimeStamp createdAt,
                                    @JsonProperty("updated_at") GitHubTimeStamp updatedAt,
                                    @JsonProperty("issue_url") String issueUrl,
                                    @JsonProperty("author_association") GitHubAuthorAssociation authorAssociation) {
        return new AutoValue_GitHubComment(
                id,
                Helper.requireNonNullElse(nodeId, ""),
                Helper.requireNonNullElse(url, ""),
                Helper.requireNonNullElse(htmlUrl, ""),
                Helper.requireNonNullElse(body, ""),
                Helper.requireNonNullElse(user, GitHubUser.empty()),
                Helper.requireNonNullElse(createdAt, GitHubTimeStamp.epoch()),
                Helper.requireNonNullElse(updatedAt, GitHubTimeStamp.epoch()),
                Helper.requireNonNullElse(issueUrl, ""),
                Helper.requireNonNullElse(authorAssociation, GitHubAuthorAssociation.NONE)
        );
    }

    @Nonnull
    public static GitHubComment empty() {
        return get(0, null, null, null, null, null, null, null, null, null);
    }


}

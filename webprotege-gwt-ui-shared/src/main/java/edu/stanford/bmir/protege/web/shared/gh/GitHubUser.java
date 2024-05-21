package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-07-12
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class GitHubUser implements IsSerializable {

    /**
     * Returns the login name of the GitHub user.
     *
     * @return The login name as a string.
     */
    public abstract @JsonProperty("login") String login();

    /**
     * Returns the unique identifier of the GitHub user.
     *
     * @return The user's ID as an integer.
     */
    public abstract @JsonProperty("id") long id();

    /**
     * Returns the node ID of the GitHub user.
     *
     * @return The node ID as a string.
     */
    public abstract @JsonProperty("node_id") String nodeId();

    /**
     * Returns the URL of the user's avatar image.
     *
     * @return The avatar URL as a string.
     */
    public abstract @JsonProperty("avatar_url") String avatarUrl();

    /**
     * The API URL for this user
     * @return The API URL as a string
     */
    public abstract @JsonProperty("url") String url();

    /**
     * Returns the URL of the GitHub user's profile.
     *
     * @return The profile URL as a string.
     */
    public abstract @JsonProperty("html_url") String htmlUrl();

    /**
     * Returns the type of the GitHub user (e.g., "User" or "Organization").
     *
     * @return The user type as a string.
     */
    public abstract @JsonProperty("type") GitHubUserType type();

    /**
     * Returns a boolean indicating whether the GitHub user is a site administrator.
     *
     * @return {@code true} if the user is a site admin, {@code false} otherwise.
     */
    public abstract @JsonProperty("site_admin") boolean siteAdmin();


    @JsonCreator
    public static GitHubUser get(@JsonProperty("login") @Nullable String login,
                                 @JsonProperty("id") long id,
                                 @JsonProperty("node_id") @Nullable String nodeId,
                                 @JsonProperty("avatar_url") @Nullable String avatarUrl,
                                 @JsonProperty("url") @Nullable String url,
                                 @JsonProperty("html_url") @Nullable String htmlUrl,
                                 @JsonProperty("type") @Nullable GitHubUserType type,
                                 @JsonProperty("site_admin") boolean siteAdmin) {
        return new AutoValue_GitHubUser(Helper.requireNonNullElse(login, ""),
                                        id,
                                        Helper.requireNonNullElse(nodeId, ""),
                                        Helper.requireNonNullElse(avatarUrl, ""),
                                        Helper.requireNonNullElse(url, ""),
                                        Helper.requireNonNullElse(htmlUrl, ""),
                                        Helper.requireNonNullElse(type, GitHubUserType.USER),
                                        siteAdmin);
    }

    public static GitHubUser empty() {
        return get("", 0, "", "", "", "", GitHubUserType.USER, false);
    }
}


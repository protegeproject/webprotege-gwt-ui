package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

/**
 * A summary representation of a GitHub repository
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GitHubRepository implements GitHubObject {

    @JsonCreator
    public static GitHubRepository get(@JsonProperty("id") long id,
                            @JsonProperty("node_id") String nodeId,
                            @JsonProperty("name") String name,
                            @JsonProperty("full_name") String fullName,
                            @JsonProperty("description") String description) {
        return new AutoValue_GitHubRepository(id,
                                              Helper.requireNonNullElse(nodeId, ""),
                                              Helper.requireNonNullElse(name, ""),
                                              Helper.requireNonNullElse(fullName, ""),
                                              Helper.requireNonNullElse(description, ""));
    }

    @JsonIgnore
    public GitHubRepositoryCoordinates getCoordinates() {
        return GitHubRepositoryCoordinates.fromFullName(fullName());
    }

    @Override
    @JsonProperty("id")
    public abstract long id();

    @Override
    @JsonProperty("node_id")
    public abstract String nodeId();

    @JsonProperty("name")
    public abstract String name();

    @JsonProperty("full_name")
    public abstract String fullName();

    @JsonProperty("description")
    public abstract String description();
}

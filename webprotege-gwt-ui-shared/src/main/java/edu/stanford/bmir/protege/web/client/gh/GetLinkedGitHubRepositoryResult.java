package edu.stanford.bmir.protege.web.client.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.gh.GitHubRepositoryCoordinates;
import edu.stanford.bmir.protege.web.shared.gh.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.github.GetLinkedGitHubRepository")
public abstract class GetLinkedGitHubRepositoryResult implements Result {

    @JsonCreator
    public static GetLinkedGitHubRepositoryResult create(@JsonProperty("projectId") ProjectId projectId,
                                                         @Nullable @JsonProperty("repositoryCoordinates") GitHubRepositoryCoordinates repositoryCoordinates) {
        return new AutoValue_GetLinkedGitHubRepositoryResult(projectId, repositoryCoordinates);
    }

    @Nonnull
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @Nullable
    @JsonProperty("repositoryCoordinates")
    public abstract GitHubRepositoryCoordinates getRepositoryCoordinatesInternal();

    @JsonIgnore
    Optional<GitHubRepositoryCoordinates> getRepositoryCoordinates() {
        return Optional.ofNullable(getRepositoryCoordinatesInternal());
    }
}

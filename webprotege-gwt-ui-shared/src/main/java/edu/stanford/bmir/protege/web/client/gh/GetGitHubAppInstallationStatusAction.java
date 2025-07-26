package edu.stanford.bmir.protege.web.client.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.gh.GitHubRepositoryCoordinates;
import edu.stanford.bmir.protege.web.shared.gh.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.github.GetGitHubAppInstallationStatus")
public abstract class GetGitHubAppInstallationStatusAction implements ProjectAction<GetGitHubAppInstallationStatusResult> {

    @JsonCreator
    public static GetGitHubAppInstallationStatusAction create(@JsonProperty("projectId") ProjectId projectId,
                                                              @JsonProperty("repositoryCoordinates") GitHubRepositoryCoordinates repositoryCoordinates) {
        return new AutoValue_GetGitHubAppInstallationStatusAction(projectId, repositoryCoordinates);
    }

    @Nonnull
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @Nonnull
    @JsonProperty("repositoryCoordinates")
    public abstract GitHubRepositoryCoordinates getRepositoryCoordinates();
}

package edu.stanford.bmir.protege.web.client.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.gh.GitHubAppInstallationStatus;
import edu.stanford.bmir.protege.web.shared.gh.GitHubRepositoryCoordinates;
import edu.stanford.bmir.protege.web.shared.gh.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.github.GetGitHubAppInstallationStatus")
public abstract class GetGitHubAppInstallationStatusResult implements Result {

    @JsonCreator
    public static GetGitHubAppInstallationStatusResult create(@JsonProperty("projectId") ProjectId projectId,
                                                              @JsonProperty("repositoryCoordinates") GitHubRepositoryCoordinates repositoryCoordinates,
                                                              @JsonProperty("installationStatus")GitHubAppInstallationStatus installationStatus) {
        return new AutoValue_GetGitHubAppInstallationStatusResult(projectId, repositoryCoordinates, installationStatus);
    }

    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("repositoryCoordinates")
    public abstract GitHubRepositoryCoordinates getRepositoryCoordinates();

    @JsonProperty("installationStatus")
    public abstract GitHubAppInstallationStatus getInstallationStatus();

}

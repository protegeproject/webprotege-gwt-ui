package edu.stanford.bmir.protege.web.client.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.gh.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.github.GetLinkedGitHubRepository")
public abstract class GetLinkedGitHubRepositoryAction implements ProjectAction<GetLinkedGitHubRepositoryResult> {

    @JsonCreator
    public static GetLinkedGitHubRepositoryAction create(@JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_GetLinkedGitHubRepositoryAction(projectId);
    }

    @Nonnull
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();
}

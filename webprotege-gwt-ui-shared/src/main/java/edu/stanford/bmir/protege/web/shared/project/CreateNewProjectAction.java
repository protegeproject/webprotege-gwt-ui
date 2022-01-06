package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@JsonTypeName("webprotege.projects.CreateNewProject")
public class CreateNewProjectAction implements Action<CreateNewProjectResult> {

    private ProjectId newProjectId;

    private NewProjectSettings newProjectSettings;

    /**
     * For serialization purposes only
     */
    private CreateNewProjectAction() {
    }

    @JsonCreator
    public CreateNewProjectAction(@JsonProperty("projectId") ProjectId newProjectId,
                                  @JsonProperty("newProjectSettings") NewProjectSettings newProjectSettings) {
        this.newProjectId = newProjectId;
        this.newProjectSettings = checkNotNull(newProjectSettings);
    }

    public ProjectId getNewProjectId() {
        return newProjectId;
    }

    public NewProjectSettings getNewProjectSettings() {
        return newProjectSettings;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(newProjectId, newProjectSettings);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CreateNewProjectAction)) {
            return false;
        }
        CreateNewProjectAction other = (CreateNewProjectAction) obj;
        return this.newProjectId.equals(other.newProjectId) && this.newProjectSettings.equals(other.newProjectSettings);
    }

    @Override
    public String toString() {
        return toStringHelper("CreateNewProjectAction")
                .addValue(newProjectId)
                .addValue(newProjectSettings)
                .toString();
    }
}

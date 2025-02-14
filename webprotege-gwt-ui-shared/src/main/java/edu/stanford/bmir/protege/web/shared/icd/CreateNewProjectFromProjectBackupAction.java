package edu.stanford.bmir.protege.web.shared.icd;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.*;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

@JsonTypeName("webprotege.projects.CreateNewProjectFromProjectBackup")
public class CreateNewProjectFromProjectBackupAction implements Action<CreateNewProjectFromProjectBackupResult> {
    private ProjectId newProjectId;

    private NewProjectSettings newProjectSettings;
    private String branchName;

    /**
     * For serialization purposes only
     */
    private CreateNewProjectFromProjectBackupAction() {
    }

    @JsonCreator
    public CreateNewProjectFromProjectBackupAction(@JsonProperty("newProjectId") ProjectId newProjectId,
                                                   @JsonProperty("newProjectSettings") NewProjectSettings newProjectSettings,
                                                   @JsonProperty("branchName") String branchName) {
        this.newProjectId = newProjectId;
        this.newProjectSettings = checkNotNull(newProjectSettings);
        this.branchName = branchName;
    }

    public ProjectId getNewProjectId() {
        return newProjectId;
    }

    public NewProjectSettings getNewProjectSettings() {
        return newProjectSettings;
    }

    public String getBranchName() {
        return branchName;
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
        if (!(obj instanceof CreateNewProjectFromProjectBackupAction)) {
            return false;
        }
        CreateNewProjectFromProjectBackupAction other = (CreateNewProjectFromProjectBackupAction) obj;
        return this.newProjectId.equals(other.newProjectId) && this.newProjectSettings.equals(other.newProjectSettings);
    }

    @Override
    public String toString() {
        return toStringHelper("CreateNewProjectFromProjectBackupAction")
                .addValue(newProjectId)
                .addValue(newProjectSettings)
                .addValue(branchName)
                .toString();
    }
}

package edu.stanford.bmir.protege.web.shared.icd;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.*;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

@JsonTypeName("webprotege.projects.CreateNewProjectFromProjectBackup")
public class CreateNewProjectFromProjectBackupResult implements Result {

    private ProjectDetails projectDetails;

    /**
     * For serialization purposes only
     */
    private CreateNewProjectFromProjectBackupResult() {
    }

    public CreateNewProjectFromProjectBackupResult(ProjectDetails projectDetails) {
        this.projectDetails = checkNotNull(projectDetails);
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectDetails);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CreateNewProjectFromProjectBackupResult)) {
            return false;
        }
        CreateNewProjectFromProjectBackupResult other = (CreateNewProjectFromProjectBackupResult) obj;
        return this.projectDetails.equals(other.projectDetails);
    }


    @Override
    public String toString() {
        return toStringHelper("CreateNewProjectFromProjectBackupResult")
                .addValue(projectDetails)
                .toString();
    }
}

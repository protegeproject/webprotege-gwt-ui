package edu.stanford.bmir.protege.web.shared.sharing;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
@JsonTypeName("webprotege.projects.GetProjectSharingSettings")
public class GetProjectSharingSettingsResult implements Result {

    private ProjectSharingSettings projectSharingSettings;

    // For serialization
    private GetProjectSharingSettingsResult() {
    }

    private GetProjectSharingSettingsResult(ProjectSharingSettings projectSharingSettings) {
        this.projectSharingSettings = checkNotNull(projectSharingSettings);
    }

    @JsonCreator
    public static GetProjectSharingSettingsResult create(@JsonProperty("settings") ProjectSharingSettings projectSharingSettings) {
        return new GetProjectSharingSettingsResult(projectSharingSettings);
    }

    public ProjectSharingSettings getProjectSharingSettings() {
        return projectSharingSettings;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectSharingSettings);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectSharingSettingsResult)) {
            return false;
        }
        GetProjectSharingSettingsResult other = (GetProjectSharingSettingsResult) obj;
        return this.projectSharingSettings.equals(other.projectSharingSettings);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectSharingSettingsResult")
                .addValue(projectSharingSettings)
                .toString();
    }
}

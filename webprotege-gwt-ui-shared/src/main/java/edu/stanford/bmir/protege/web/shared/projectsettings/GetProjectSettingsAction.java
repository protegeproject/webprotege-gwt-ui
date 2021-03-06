package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@JsonTypeName("webprotege.projects.GetProjectSettings")
public class GetProjectSettingsAction extends AbstractHasProjectAction<GetProjectSettingsResult> {


    /**
     * For serialization purposes only
     */
    public GetProjectSettingsAction() {
    }

    private GetProjectSettingsAction(ProjectId projectId) {
        super(projectId);
    }

    public static GetProjectSettingsAction create(ProjectId projectId) {
        return new GetProjectSettingsAction(projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectSettingsAction)) {
            return false;
        }
        GetProjectSettingsAction other = (GetProjectSettingsAction) obj;
        return this.getProjectId().equals(other.getProjectId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId());
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetProjectSettingsAction")
                          .addValue(getProjectId())
                          .toString();
    }
}

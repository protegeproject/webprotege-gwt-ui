package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@JsonTypeName("webprotege.projects.SetProjectSettings")
public class SetProjectSettingsAction extends AbstractHasProjectAction<SetProjectSettingsResult> {

    private ProjectSettings projectSettings;

    /**
     * For serialization purposes only
     */
    private SetProjectSettingsAction() {
    }

    private SetProjectSettingsAction(ProjectSettings projectSettings) {
        super(projectSettings.getProjectId());
        this.projectSettings = projectSettings;
    }

    public static SetProjectSettingsAction create(ProjectSettings projectSettings) {
        return new SetProjectSettingsAction(projectSettings);
    }

    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectSettings);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetProjectSettingsAction)) {
            return false;
        }
        SetProjectSettingsAction other = (SetProjectSettingsAction) obj;
        return this.projectSettings.equals(other.projectSettings);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("SetProjectSettingsAction")
                          .addValue(projectSettings)
                          .toString();
    }
}

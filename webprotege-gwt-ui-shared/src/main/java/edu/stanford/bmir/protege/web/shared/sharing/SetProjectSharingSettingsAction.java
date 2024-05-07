package edu.stanford.bmir.protege.web.shared.sharing;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
@JsonTypeName("webprotege.sharing.SetProjectSharingSettings")
public class SetProjectSharingSettingsAction implements ProjectAction<SetProjectSharingSettingsResult> {

    private ProjectSharingSettings settings;

    private ChangeRequestId changeRequestId;

    private ProjectId projectId;

    private SetProjectSharingSettingsAction() {
    }

    private SetProjectSharingSettingsAction(ProjectSharingSettings projectSharingSettings, ChangeRequestId changeRequestId, ProjectId projectId) {
        this.settings = checkNotNull(projectSharingSettings);
        this.projectId = projectId;
        this.changeRequestId = changeRequestId;
    }

    public static SetProjectSharingSettingsAction create(ProjectSharingSettings projectSharingSettings,  ChangeRequestId changeRequestId, ProjectId projectId) {
        return new SetProjectSharingSettingsAction(projectSharingSettings, changeRequestId, projectId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return settings.getProjectId();
    }

    public ProjectSharingSettings getSettings() {
        return settings;
    }

    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(settings);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetProjectSharingSettingsAction)) {
            return false;
        }
        SetProjectSharingSettingsAction other = (SetProjectSharingSettingsAction) obj;
        return this.settings.equals(other.settings);
    }


    @Override
    public String toString() {
        return toStringHelper("SetProjectSharingSettingsAction")
                .addValue(settings)
                .toString();
    }
}

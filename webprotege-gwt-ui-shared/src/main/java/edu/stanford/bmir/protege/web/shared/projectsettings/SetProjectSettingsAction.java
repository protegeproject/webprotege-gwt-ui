package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@JsonTypeName("webprotege.projects.SetProjectSettings")
public class SetProjectSettingsAction extends AbstractHasProjectAction<SetProjectSettingsResult> {

    private ChangeRequestId changeRequestId;

    private ProjectSettings projectSettings;

    /**
     * For serialization purposes only
     */
    private SetProjectSettingsAction() {
    }

    private SetProjectSettingsAction(ChangeRequestId changeRequestId,
                                     ProjectId projectId,
                                     ProjectSettings projectSettings) {
        super(projectId);
        this.changeRequestId = checkNotNull(changeRequestId);
        this.projectSettings = checkNotNull(projectSettings);
    }

    @GwtIncompatible
    public static SetProjectSettingsAction create(ProjectSettings projectSettings) {
        return create(ChangeRequestId.get(UUID.randomUUID().toString()),
                      projectSettings.getProjectId(),
                      projectSettings);
    }

    @JsonCreator
    public static SetProjectSettingsAction create(@JsonProperty("changeRequestId") ChangeRequestId changeRequestId,
                                                  @JsonProperty("projectId") ProjectId projectId,
                                                  @JsonProperty("settings") ProjectSettings projectSettings) {
        return new SetProjectSettingsAction(changeRequestId, projectId, projectSettings);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Nonnull
    @Override
    @JsonProperty("projectId")
    public ProjectId getProjectId() {
        return super.getProjectId();
    }

    @JsonProperty("settings")
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

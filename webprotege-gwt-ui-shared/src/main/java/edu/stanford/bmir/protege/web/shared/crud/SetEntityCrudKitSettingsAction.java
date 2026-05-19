package edu.stanford.bmir.protege.web.shared.crud;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
@JsonTypeName("webprotege.projects.SetEntityCrudKitSettings")
public class SetEntityCrudKitSettingsAction implements ProjectAction<SetEntityCrudKitSettingsResult> {

    private ChangeRequestId changeRequestId;

    private ProjectId projectId;

    private EntityCrudKitSettings fromSettings;

    private EntityCrudKitSettings toSettings;

    private IRIPrefixUpdateStrategy prefixUpdateStrategy;

    /**
     * For serialization purposes only
     */
    private SetEntityCrudKitSettingsAction() {
    }

    @JsonCreator
    public SetEntityCrudKitSettingsAction(@JsonProperty("changeRequestId") ChangeRequestId changeRequestId,
                                          @JsonProperty("projectId") ProjectId projectId,
                                          @JsonProperty("fromSettings") EntityCrudKitSettings fromSettings,
                                          @JsonProperty("toSettings") EntityCrudKitSettings toSettings,
                                          @JsonProperty("prefixUpdateStrategy") IRIPrefixUpdateStrategy prefixUpdateStrategy) {
        this.changeRequestId = changeRequestId;
        this.projectId = projectId;
        this.toSettings = toSettings;
        this.fromSettings = fromSettings;
        this.prefixUpdateStrategy = prefixUpdateStrategy;
    }


    @Nonnull
    @JsonProperty("changeRequestId")
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public IRIPrefixUpdateStrategy getPrefixUpdateStrategy() {
        return prefixUpdateStrategy;
    }

    public EntityCrudKitSettings getToSettings() {
        return toSettings;
    }

    public EntityCrudKitSettings getFromSettings() {
        return fromSettings;
    }
}

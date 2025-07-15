package edu.stanford.bmir.protege.web.shared.crud;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
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

    private ProjectId projectId;

    private EntityCrudKitSettings fromSettings;

    private EntityCrudKitSettings toSettings;

    private IRIPrefixUpdateStrategy prefixUpdateStrategy;

    /**
     * For serialization purposes only
     */
    private SetEntityCrudKitSettingsAction() {
    }

    public SetEntityCrudKitSettingsAction(ProjectId projectId, EntityCrudKitSettings fromSettings, EntityCrudKitSettings toSettings, IRIPrefixUpdateStrategy prefixUpdateStrategy) {
        this.projectId = projectId;
        this.toSettings = toSettings;
        this.fromSettings = fromSettings;
        this.prefixUpdateStrategy = prefixUpdateStrategy;
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

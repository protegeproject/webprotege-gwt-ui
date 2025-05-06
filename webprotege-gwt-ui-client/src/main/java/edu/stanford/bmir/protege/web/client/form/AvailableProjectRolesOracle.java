package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectRoleDefinitionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@ProjectSingleton
public class AvailableProjectRolesOracle {

    private final ProjectId projectId;

    private final DispatchServiceManager dispatch;

    @Inject
    public AvailableProjectRolesOracle(ProjectId projectId, DispatchServiceManager dispatch) {
        this.projectId = projectId;
        this.dispatch = dispatch;
    }


    public void getAvailableProjectRoles(Consumer<List<RoleId>> consumer) {
        dispatch.execute(GetProjectRoleDefinitionsAction.get(projectId), result -> {
            List<RoleDefinition> roleDefinitions = result.getRoleDefinitions();
            List<RoleId> roleIds = roleDefinitions.stream()
                            .map(RoleDefinition::getRoleId)
                    .collect(Collectors.toList());
            consumer.accept(roleIds);
        });
    }

}

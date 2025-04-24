package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.ObjectListPresenter;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectRoleDefinitionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.logging.Logger;

@ProjectSingleton
public class ProjectRolesPresenter implements Presenter {

    private static final Logger logger = Logger.getLogger(ProjectRolesPresenter.class.getName());

    private final ProjectId projectId;

    private final ProjectRolesView view;

    private final DispatchServiceManager dispatch;

    private final RoleDefinitionsObjectListPresenter roleDefinitionsObjectListPresenter;

    @Inject
    public ProjectRolesPresenter(ProjectId projectId, ProjectRolesView view,
                                 DispatchServiceManager dispatch,
                                 RoleDefinitionsObjectListPresenter roleDefinitionsObjectListPresenter) {
        this.projectId = projectId;
        this.view = view;
        this.dispatch = dispatch;
        this.roleDefinitionsObjectListPresenter = roleDefinitionsObjectListPresenter;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        container.setWidget(view);
        view.setApplySettingsHandler(() -> {
            logger.info(roleDefinitionsObjectListPresenter.getValues().toString());
        });
        roleDefinitionsObjectListPresenter.start(view.getRoleDefinitionsContainer(), eventBus);
        dispatch.execute(GetProjectRoleDefinitionsAction.get(projectId),
                result -> {
                    roleDefinitionsObjectListPresenter.setValues(result.getRoleDefinitions());
                });
    }
}

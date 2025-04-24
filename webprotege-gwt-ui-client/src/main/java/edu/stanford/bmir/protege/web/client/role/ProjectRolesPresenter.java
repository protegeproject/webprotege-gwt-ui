package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.CapabilityScreener;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.ObjectListPresenter;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.projectsettings.ProjectSettingsPresenter;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.access.BasicCapability;
import edu.stanford.bmir.protege.web.shared.access.BuiltInCapability;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectRoleDefinitionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Logger;

@ProjectSingleton
public class ProjectRolesPresenter implements Presenter {

    private static final Logger logger = Logger.getLogger(ProjectRolesPresenter.class.getName());

    private final ProjectId projectId;

    private final ProjectRolesView view;

    @Nonnull
    private final BusyView busyView;

    private final DispatchServiceManager dispatch;

    private final RoleDefinitionsObjectListPresenter roleDefinitionsObjectListPresenter;

    private final CapabilityScreener capabilityScreener;

    private final SettingsPresenter settingsPresenter;

    @Inject
    public ProjectRolesPresenter(ProjectId projectId,
                                 ProjectRolesView view, @Nonnull BusyView busyView,
                                 DispatchServiceManager dispatch,
                                 RoleDefinitionsObjectListPresenter roleDefinitionsObjectListPresenter,
                                 CapabilityScreener capabilityScreener,
                                 SettingsPresenter settingsPresenter) {
        this.projectId = projectId;
        this.view = view;
        this.busyView = busyView;
        this.dispatch = dispatch;
        this.roleDefinitionsObjectListPresenter = roleDefinitionsObjectListPresenter;
        this.capabilityScreener = capabilityScreener;
        this.settingsPresenter = settingsPresenter;
    }

    public void setNextPlace(Optional<Place> nextPlace) {
        settingsPresenter.setNextPlace(nextPlace);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        container.setWidget(busyView);
        capabilityScreener.checkCapability(BuiltInCapability.EDIT_PROJECT_SETTINGS.getCapability(),
                container,
                () -> displayProjectRoles(container, eventBus));
    }

    private void displayProjectRoles(AcceptsOneWidget container, EventBus eventBus) {
        settingsPresenter.setApplySettingsHandler(() -> {
            logger.info(roleDefinitionsObjectListPresenter.getValues().toString());
            settingsPresenter.goToNextPlace();
        });
        dispatch.execute(GetProjectRoleDefinitionsAction.get(projectId),
                result -> {
                    settingsPresenter.start(container);
                    settingsPresenter.setSettingsTitle("Role Definitions");
                    AcceptsOneWidget roleDefinitions = settingsPresenter.addSection("Role Definitions" );
                    roleDefinitions.setWidget(view);
                    roleDefinitionsObjectListPresenter.start(view.getRoleDefinitionsContainer(), eventBus);
                    roleDefinitionsObjectListPresenter.setValues(result.getRoleDefinitions());
                });
    }
}

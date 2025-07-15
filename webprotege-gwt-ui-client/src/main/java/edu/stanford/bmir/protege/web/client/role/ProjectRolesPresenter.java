package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.CapabilityScreener;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.ObjectListPresenter;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.projectsettings.ProjectSettingsPresenter;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.access.BasicCapability;
import edu.stanford.bmir.protege.web.shared.access.BuiltInCapability;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectRoleDefinitionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.ResetProjectRoleDefinitionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;
import edu.stanford.bmir.protege.web.shared.permissions.SetProjectRoleDefinitionsAction;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    private final MessageBox messageBox;

    private final List<RoleId> roleIds = new ArrayList<>();

    @Inject
    public ProjectRolesPresenter(ProjectId projectId,
                                 ProjectRolesView view, @Nonnull BusyView busyView,
                                 DispatchServiceManager dispatch,
                                 RoleDefinitionsObjectListPresenter roleDefinitionsObjectListPresenter,
                                 CapabilityScreener capabilityScreener,
                                 SettingsPresenter settingsPresenter,
                                 MessageBox messageBox) {
        this.projectId = projectId;
        this.view = view;
        this.busyView = busyView;
        this.dispatch = dispatch;
        this.roleDefinitionsObjectListPresenter = roleDefinitionsObjectListPresenter;
        this.capabilityScreener = capabilityScreener;
        this.settingsPresenter = settingsPresenter;
        this.messageBox = messageBox;
    }

    public void setNextPlace(Optional<Place> nextPlace) {
        settingsPresenter.setNextPlace(nextPlace);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        container.setWidget(busyView);
        view.setResetProjectRolesHandler(this::handleResetProjectRoles);
        capabilityScreener.checkCapability(BuiltInCapability.EDIT_PROJECT_SETTINGS.getCapability(),
                container,
                () -> displayProjectRoles(container, eventBus));
    }

    private void handleResetProjectRoles() {
        DialogButton resetButton = DialogButton.get("Reset" );
        messageBox.showConfirmBox("Reset project roles?",
                "Are you sure that you want to reset the project roles?<br>This will clear any customization of roles, including form access restrictions.", DialogButton.CANCEL, resetButton, this::performProjectRolesReset, DialogButton.CANCEL);

    }

    private void performProjectRolesReset() {
        dispatch.execute(ResetProjectRoleDefinitionsAction.get(projectId),
                result -> {
                    roleDefinitionsObjectListPresenter.setValues(result.getRoleDefinitions());
                    messageBox.showMessage("Project roles reset", "The project roles have been reset to the default values.  Note, you may have to reconfigure access restrictions to forms and similar resources.");
                });
    }

    private void displayProjectRoles(AcceptsOneWidget container, EventBus eventBus) {
        settingsPresenter.setApplySettingsHandler(() -> {
            logger.info(roleDefinitionsObjectListPresenter.getValues().toString());
            dispatch.execute(SetProjectRoleDefinitionsAction.get(projectId, roleDefinitionsObjectListPresenter.getValues()),
                    result -> {
                    });
            settingsPresenter.goToNextPlace();
        });
        dispatch.execute(GetProjectRoleDefinitionsAction.get(projectId),
                result -> {
                    settingsPresenter.start(container);
                    settingsPresenter.setSettingsTitle("Role Definitions");
                    AcceptsOneWidget roleDefinitions = settingsPresenter.addSection("Role Definitions" );
                    roleDefinitions.setWidget(view);
                    roleDefinitionsObjectListPresenter.start(view.getRoleDefinitionsContainer(), eventBus);
                    List<RoleDefinition> defs = result.getRoleDefinitions();
                    this.roleIds.clear();
                    this.roleIds.addAll(defs.stream().map(RoleDefinition::getRoleId).collect(Collectors.toList()));
                    roleDefinitionsObjectListPresenter.setValues(defs);
                    roleDefinitionsObjectListPresenter.setParentRoleIdSupplier(getParentRoleIdSupplier());
                    roleDefinitionsObjectListPresenter.setRoleIdCycleChecker(getRoleIdCycleFinder());
        });
    }

    private RoleIdCycleFinder getRoleIdCycleFinder() {
        return roleId -> new RoleDefinitionCycleFinder()
                .findCycle(roleDefinitionsObjectListPresenter.getValues(), roleId);
    }

    public ParentRoleIdSupplier getParentRoleIdSupplier() {
        return this::getAvailableRoleIds;
    }

    private Set<RoleId> getAvailableRoleIds() {
        Set<RoleId> setIds = new LinkedHashSet<>(roleIds);
        roleDefinitionsObjectListPresenter.getValues()
                .stream()
                .map(RoleDefinition::getRoleId)
                .forEach(setIds::add);
        return setIds;
    }
}

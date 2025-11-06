package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.permissions.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectRoleAssignmentsPresenter {

    private final ProjectId projectId;

    private final ProjectRoleAssignmentsView view;

    private final SettingsPresenter settingsPresenter;

    private final DispatchServiceManager dispatchServiceManager;

    private final MessageBox messageBox;

    private final Messages messages;

    @Inject
    public ProjectRoleAssignmentsPresenter(ProjectId projectId, ProjectRoleAssignmentsView view, SettingsPresenter settingsPresenter, DispatchServiceManager dispatchServiceManager, MessageBox messageBox, Messages messages) {
        this.projectId = projectId;
        this.view = view;
        this.settingsPresenter = settingsPresenter;
        this.dispatchServiceManager = dispatchServiceManager;
        this.messageBox = messageBox;
        this.messages = messages;
    }

    public void start(AcceptsOneWidget container) {
        settingsPresenter.start(container);
        settingsPresenter.setBusy(true);
        settingsPresenter.setApplySettingsHandler(this::handleApply);
        dispatchServiceManager.execute(GetProjectRoleAssignmentsAction.create(projectId), result -> {
            settingsPresenter.setBusy(false);
            AcceptsOneWidget roleAssignments = settingsPresenter.addSection(messages.settings_projectRoleAssignments());
            roleAssignments.setWidget(view);
            List<ProjectUserRoleAssignment> userAssignments = result.getAssignments()
                    .getUserAssignments()
                    .stream()
                    .sorted(Comparator.comparing(ra -> ra.getUserId().getUserName()))
                    .collect(Collectors.toList());
            view.setRoleAssignments(userAssignments);
        });
        dispatchServiceManager.execute(GetProjectRoleDefinitionsAction.get(projectId), result -> {
            view.setAvailableRoles(result.getRoleDefinitions());
        });
    }

    private void handleApply() {
        ProjectRoleAssignments assignments = ProjectRoleAssignments.get(view.getRoleAssignments());
        dispatchServiceManager.execute(SetProjectRoleAssignmentsAction.create(projectId, assignments),
                result -> {
                    messageBox.showMessage("Role assignments updated", "The role assignments for this project have been updated.");
                    settingsPresenter.goToNextPlace();
                });
    }

    public void setNextPlace(Optional<Place> nextPlace) {
        settingsPresenter.setNextPlace(nextPlace);
    }
}

package edu.stanford.bmir.protege.web.client.permissions;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectRoleDefinitionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectRoleSelectorPresenter implements ValueEditor<RoleId> {

    private final ProjectId projectId;

    private final DispatchServiceManager dispatchServiceManager;

    private final ProjectRoleSelectorView view;

    @Inject
    public ProjectRoleSelectorPresenter(ProjectId projectId, DispatchServiceManager dispatchServiceManager, ProjectRoleSelectorView view) {
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        this.view = view;
    }

    public void start() {
        dispatchServiceManager.execute(GetProjectRoleDefinitionsAction.get(projectId),
                result -> view.setAvailableRoles(result.getRoleDefinitions().stream().map(RoleDefinition::getRoleId).collect(Collectors.toList())));
    }

    @Override
    public void setValue(RoleId object) {
        view.setValue(object);
    }

    @Override
    public void clearValue() {
        view.clearValue();
    }

    @Override
    public Optional<RoleId> getValue() {
        return view.getValue();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<RoleId>> handler) {
        return view.addValueChangeHandler(handler);
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    @Override
    public boolean isDirty() {
        return view.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return view.addDirtyChangedHandler(handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        view.fireEvent(event);
    }

    @Override
    public boolean isWellFormed() {
        return view.isWellFormed();
    }
}

package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.user.UserIdSuggestOracle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.permissions.ProjectUserRoleAssignment;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ProjectRoleAssignmentsValueEditor extends Composite implements ValueEditor<ProjectUserRoleAssignment> {

    interface ProjectRoleAssignmentsValueEditorUiBinder extends UiBinder<HTMLPanel, ProjectRoleAssignmentsValueEditor> {

    }

    private static ProjectRoleAssignmentsValueEditorUiBinder ourUiBinder = GWT.create(ProjectRoleAssignmentsValueEditorUiBinder.class);

    @UiField(provided = true)
    SuggestBox personIdBox;

    @UiField
    ListBox roleIdListBox;

    @Inject
    public ProjectRoleAssignmentsValueEditor(DispatchServiceManager dispatchServiceManager) {
        UserIdSuggestOracle oracle = new UserIdSuggestOracle(dispatchServiceManager);
        personIdBox = new SuggestBox(oracle);
        personIdBox.setEnabled(true);
        initWidget(ourUiBinder.createAndBindUi(this));
        roleIdListBox.addItem("");
    }

    public void setAvailableRoles(List<RoleDefinition> availableRoles) {
        String selValue = roleIdListBox.getSelectedValue();
        roleIdListBox.clear();
        roleIdListBox.addItem("");
        availableRoles.stream()
                .filter(r -> !r.getLabel().trim().isEmpty())
                .sorted(Comparator.comparing(RoleDefinition::getLabel))
                .forEach(r -> roleIdListBox.addItem(r.getLabel(), r.getRoleId().getId()));
        displayRoleInListBox(selValue);

    }

    @Override
    public void setValue(ProjectUserRoleAssignment object) {
        personIdBox.setValue(object.getUserId().getUserName());
        roleIdListBox.setSelectedIndex(0);
        String roleId = object.getRoleId().getId();
        displayRoleInListBox(roleId);

    }

    private void displayRoleInListBox(String roleId) {
        for(int i = 0; i < roleIdListBox.getItemCount(); i++) {
            if(roleIdListBox.getValue(i).equals(roleId)) {
                roleIdListBox.setSelectedIndex(i);
                break;
            }
        }
        if(roleIdListBox.getSelectedIndex() == 0) {
            roleIdListBox.addItem(roleId);
            roleIdListBox.setSelectedIndex(roleIdListBox.getItemCount() - 1);
        }
    }

    @Override
    public void clearValue() {
        personIdBox.setText("");
        roleIdListBox.setSelectedIndex(0);
    }

    @Override
    public Optional<ProjectUserRoleAssignment> getValue() {
        if(personIdBox.getText().trim().isEmpty()) {
            return Optional.empty();
        }
        if(roleIdListBox.getSelectedIndex() <= 0) {
            return Optional.empty();
        }
        UserId userId = UserId.valueOf(personIdBox.getValue().trim());
        RoleId roleId = RoleId.valueOf(roleIdListBox.getSelectedValue());
        return Optional.of(ProjectUserRoleAssignment.create(userId, roleId));
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<ProjectUserRoleAssignment>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public boolean isWellFormed() {
        return getValue().isPresent();
    }
}
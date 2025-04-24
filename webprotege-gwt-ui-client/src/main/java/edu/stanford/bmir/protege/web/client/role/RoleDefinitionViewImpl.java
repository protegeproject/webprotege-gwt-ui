package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoleDefinitionViewImpl extends Composite implements RoleDefinitionView {

    interface RoleDefinitionViewImplUiBinder extends UiBinder<HTMLPanel, RoleDefinitionViewImpl> {

    }

    private static RoleDefinitionViewImplUiBinder ourUiBinder = GWT.create(RoleDefinitionViewImplUiBinder.class);

    @UiField
    TextBox roleIdField;

    @UiField
    TextArea descriptionField;

    @UiField(provided = true)
    ValueListFlexEditorImpl<RoleId> parentRolesEditor;

    @UiField(provided = true)
    ValueListFlexEditorImpl<Capability> capabilitiesEditor;

    @Inject
    public RoleDefinitionViewImpl(ValueListFlexEditorImpl<RoleId> parentRolesEditor,
                                  ValueListFlexEditorImpl<Capability> capabilitiesEditor) {
        this.parentRolesEditor = parentRolesEditor;
        parentRolesEditor.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        parentRolesEditor.setEnabled(true);
        this.capabilitiesEditor = capabilitiesEditor;
        capabilitiesEditor.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        capabilitiesEditor.setEnabled(true);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setRoleId(RoleId roleId) {
        roleIdField.setText(roleId.getId());
    }

    @Override
    public Optional<RoleId> getRoleId() {
        return Optional.of(new RoleId(roleIdField.getText()));
    }

    @Override
    public void setAvailableRoles(List<RoleDefinition> availableRoles) {

    }

    @Override
    public void setParentRoles(List<RoleId> parentRoles) {
        parentRolesEditor.setValue(parentRoles);
    }

    @Override
    public List<RoleId> getParentRoles() {
        return parentRolesEditor.getValue().orElse(Collections.emptyList());
    }

    @Override
    public void setDescription(String description) {
        descriptionField.setValue(description);
    }

    @Override
    public String getDescription() {
        return descriptionField.getValue().trim();
    }

    @Override
    public void setCapabilities(List<Capability> capabilities) {
        capabilitiesEditor.setValue(capabilities);
    }

    @Override
    public List<Capability> getCapabilities() {
        return capabilitiesEditor.getValue().orElse(Collections.emptyList());
    }
}
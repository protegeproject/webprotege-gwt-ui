package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;

import javax.inject.Inject;
import java.util.ArrayList;
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
    ValueListEditor<Capability> capabilitiesEditor;

    @Inject
    public RoleDefinitionViewImpl(ValueListEditor<Capability> capabilitiesEditor) {
        this.capabilitiesEditor = capabilitiesEditor;
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

    }

    @Override
    public List<RoleId> getParentRoles() {
        return new ArrayList<>();
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
    public ValueListEditor<Capability> getCapabilitiesEditor() {
        return capabilitiesEditor;
    }
}
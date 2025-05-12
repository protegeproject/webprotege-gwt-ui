package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.RoleId;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RoleDefinitionViewImpl extends Composite implements RoleDefinitionView {

    private static final Logger logger = Logger.getLogger(RoleDefinitionViewImpl.class.getName());

    private Consumer<String> roleIdChangedHandler = id -> {};

    private ParentRoleIdsChangedHandler parentRoleIdsChangedHandler = () -> {};

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

    @UiField
    Label cycleMessageField;

    @UiField
    TextBox labelField;

    private ParentRoleIdSupplier parentRoleIdSupplier = Collections::emptySet;

    @Inject
    public RoleDefinitionViewImpl(ValueListFlexEditorImpl<Capability> capabilitiesEditor,
                                  Provider<ParentRoleSelectorPresenter> roleSelectorPresenterProvider) {
        this.parentRolesEditor = new ValueListFlexEditorImpl<>(() -> {
            ParentRoleSelectorPresenter p = roleSelectorPresenterProvider.get();
            p.setParentRoleIdSupplier(parentRoleIdSupplier);
            return p;
        });
        this.parentRolesEditor.setEnabled(true);
        this.parentRolesEditor.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        this.parentRolesEditor.addValueChangeHandler(evt -> {
            logger.info("Parent roles changed");
            parentRoleIdsChangedHandler.handleParentRoleIdsChanged();
        });
        this.capabilitiesEditor = capabilitiesEditor;
        capabilitiesEditor.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        capabilitiesEditor.setEnabled(true);
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @UiHandler("roleIdField" )
    public void handleChange(ChangeEvent event) {
        roleIdChangedHandler.accept(roleIdField.getText().trim());
    }

    @UiHandler("roleIdField" )
    public void handleKeyDown(KeyDownEvent event) {
        roleIdChangedHandler.accept(roleIdField.getText().trim());
    }

    @UiHandler("roleIdField" )
    public void handleKeyUp(KeyUpEvent event) {
        roleIdChangedHandler.accept(roleIdField.getText().trim());
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
    public void setParentRoles(List<RoleId> parentRoles) {
        parentRolesEditor.setValue(parentRoles);
    }

    @Override
    public List<RoleId> getParentRoles() {
        return parentRolesEditor.getValue().orElse(Collections.emptyList());
    }

    @Override
    public String getLabel() {
        return labelField.getText().trim();
    }

    @Override
    public void setLabel(String label) {
        labelField.setText(label);
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

    @Override
    public void setParentRoleIdSupplier(ParentRoleIdSupplier parentRoleIdSupplier) {
        this.parentRoleIdSupplier = parentRoleIdSupplier;
        parentRolesEditor.forEachEditor(e -> {
            ((ParentRoleSelectorPresenter) e).setParentRoleIdSupplier(parentRoleIdSupplier);
        });
    }

    @Override
    public void setRoleIdChangedHandler(Consumer<String> handler) {
        this.roleIdChangedHandler = handler;
    }

    @Override
    public void setParentRoleIdsChangedHandler(ParentRoleIdsChangedHandler handler) {
        this.parentRoleIdsChangedHandler = handler;
    }

    @Override
    public void clearCycle() {
        cycleMessageField.setText("");
    }

    @Override
    public void displayCycle(List<RoleId> roleIds) {
        String cycleList = roleIds.stream()
                .map(RoleId::getId)
                .collect(Collectors.joining(", "));
        cycleMessageField.setText("Cycle in role hierarchy: " + cycleList);
    }
}
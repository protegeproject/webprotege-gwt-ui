package edu.stanford.bmir.protege.web.client.permissions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.access.RoleId;

import javax.inject.Inject;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectRoleSelectorViewImpl extends Composite implements ProjectRoleSelectorView {

    private ArrayList<RoleId> availableRoles;

    interface ProjectRoleSelectorViewImplUiBinder extends UiBinder<HTMLPanel, ProjectRoleSelectorViewImpl> {

    }

    private static ProjectRoleSelectorViewImplUiBinder ourUiBinder = GWT.create(ProjectRoleSelectorViewImplUiBinder.class);

    @UiField
    ListBox availableRolesListBox;

    private Optional<RoleId> setSelection = Optional.empty();

    @Inject
    public ProjectRoleSelectorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setAvailableRoles(List<RoleId> availableRoles) {
        this.availableRoles = new ArrayList<>(availableRoles);
        availableRolesListBox.clear();
        availableRolesListBox.addItem("");
        availableRoles.stream()
                .map(RoleId::getId)
                .forEach(availableRolesListBox::addItem);
        availableRolesListBox.addChangeHandler(this::fireEvent);
        setSelection.ifPresent(this::setSelectedRole);
    }

    @Override
    public Optional<RoleId> getSelectedRole() {
        String selValue = availableRolesListBox.getSelectedValue();
        if(selValue == null) {
            return Optional.empty();
        }
        else if(selValue.isEmpty()) {
            return Optional.empty();
        }
        else {
            return Optional.of(new RoleId(selValue));
        }
    }

    private void setSelectedRole(RoleId selectedRole) {
        availableRolesListBox.setSelectedIndex(-1);
        for(int i = 0; i < availableRolesListBox.getItemCount(); i++) {
            if(availableRolesListBox.getItemText(i).equals(selectedRole.getId())) {
                availableRolesListBox.setSelectedIndex(i);
            }
        }
    }

    private void clearSelectedRole() {
        availableRolesListBox.setSelectedIndex(0);
    }

    @Override
    public void setValue(RoleId object) {
        this.setSelection = Optional.of(object);
        setSelectedRole(object);
    }

    @Override
    public void clearValue() {
        this.setSelection = Optional.empty();
        clearSelectedRole();
    }

    @Override
    public Optional<RoleId> getValue() {
        return getSelectedRole();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<RoleId>> handler) {
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
        return true;
    }
}
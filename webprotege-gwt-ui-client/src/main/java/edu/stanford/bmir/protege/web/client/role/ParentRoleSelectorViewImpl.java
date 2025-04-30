package edu.stanford.bmir.protege.web.client.role;

import com.google.auto.factory.AutoFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import elemental.html.Console;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@AutoFactory
public class ParentRoleSelectorViewImpl extends Composite implements ParentRoleSelectorView {

    private ParentRoleIdSupplier parentRoleIdSupplier = () -> Collections.emptySet();

    private Logger logger = Logger.getLogger(ParentRoleSelectorViewImpl.class.getName());

    interface ParentRoleSelectorViewImplUiBinder extends UiBinder<HTMLPanel, ParentRoleSelectorViewImpl> {

    }

    private static ParentRoleSelectorViewImplUiBinder ourUiBinder = GWT.create(ParentRoleSelectorViewImplUiBinder.class);

    @UiField
    ListBox listBox;

    private Optional<RoleId> selectedRole = Optional.empty();

    @Inject
    public ParentRoleSelectorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        listBox.addChangeHandler(evt -> {
            selectedRole = getSelectedRole();
            fireEvent(evt);
            ValueChangeEvent.fire(this, selectedRole);
        });
        listBox.addAttachHandler(event -> refillRolesList());
        listBox.addClickHandler(event -> refillRolesList());
        listBox.addFocusHandler(event -> refillRolesList());
    }

    private void refillRolesList() {
        logger.info("Refilling roles list");
        Set<RoleId> parentRoles = parentRoleIdSupplier.get();
        listBox.clear();
        parentRoles.stream().map(RoleId::getId).sorted().forEach(listBox::addItem);
        listBox.insertItem("", 0);
        transmitSelectedRoleToListBox();
    }

    @Override
    public void setParentRoleIdSupplier(ParentRoleIdSupplier parentRoleIdSupplier) {
        this.parentRoleIdSupplier = parentRoleIdSupplier;
        refillRolesList();
    }

    @Override
    public Optional<RoleId> getSelectedRole() {
        String selValue = listBox.getSelectedValue();
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
        this.selectedRole = Optional.of(selectedRole);
        transmitSelectedRoleToListBox();
        // Add the role if it is not found in the list
//        listBox.addItem(selectedRole.getId());
//        listBox.setSelectedIndex(0);
    }

    private void transmitSelectedRoleToListBox() {
        selectedRole.ifPresent(selRole -> {
            listBox.setSelectedIndex(-1);
            for(int i = 0; i < listBox.getItemCount(); i++) {
                if(listBox.getItemText(i).equals(selRole.getId())) {
                    listBox.setSelectedIndex(i);
                    return;
                }
            }
        });
    }

    private void clearSelectedRole() {
        this.selectedRole = Optional.empty();
        listBox.setSelectedIndex(0);
    }

    @Override
    public void setValue(RoleId object) {
        this.selectedRole = Optional.of(object);
        setSelectedRole(object);
    }

    @Override
    public void clearValue() {
        this.selectedRole = Optional.empty();
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
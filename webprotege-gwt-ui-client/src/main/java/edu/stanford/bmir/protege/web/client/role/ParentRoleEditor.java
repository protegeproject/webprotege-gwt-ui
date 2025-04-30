package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.access.RoleId;

import java.util.Optional;

public class ParentRoleEditor extends Composite implements ValueEditor<RoleId> {

    interface ParentRoleEditorUiBinder extends UiBinder<HTMLPanel, ParentRoleEditor> {

    }

    private static ParentRoleEditorUiBinder ourUiBinder = GWT.create(ParentRoleEditorUiBinder.class);

    @UiField
    SimplePanel container;

    public ParentRoleEditor() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setValue(RoleId object) {

    }

    @Override
    public void clearValue() {

    }

    @Override
    public Optional<RoleId> getValue() {
        return Optional.empty();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<RoleId>> handler) {
        return null;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return null;
    }

    @Override
    public boolean isWellFormed() {
        return false;
    }
}
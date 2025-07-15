package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.access.CapabilityId;
import edu.stanford.bmir.protege.web.shared.form.FormRegionAccessRestriction;

import javax.inject.Inject;
import java.util.Optional;

public class FormRegionRoleCriteriaValueEditor extends Composite implements ValueEditor<RoleCriteriaBinding> {

    interface FormRegionRoleCriteriaValueEditorUiBinder extends UiBinder<HTMLPanel, FormRegionRoleCriteriaValueEditor> {

    }

    private static FormRegionRoleCriteriaValueEditorUiBinder ourUiBinder = GWT.create(FormRegionRoleCriteriaValueEditorUiBinder.class);

    @UiField
    SimplePanel container;

    private final FormRegionRoleCriteriaPresenter presenter;

    @Inject
    public FormRegionRoleCriteriaValueEditor(FormRegionRoleCriteriaPresenter presenter) {
        this.presenter = presenter;
        initWidget(ourUiBinder.createAndBindUi(this));
        presenter.start(container);
    }

    @Override
    public void setValue(RoleCriteriaBinding object) {
        presenter.setValue(object);
    }

    @Override
    public void clearValue() {
    }

    @Override
    public Optional<RoleCriteriaBinding> getValue() {
        return presenter.getValue();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<RoleCriteriaBinding>> handler) {
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
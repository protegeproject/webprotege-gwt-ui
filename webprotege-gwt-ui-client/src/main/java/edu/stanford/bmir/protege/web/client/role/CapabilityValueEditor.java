package edu.stanford.bmir.protege.web.client.role;

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
import edu.stanford.bmir.protege.web.shared.access.Capability;

import javax.inject.Inject;
import java.util.Optional;

public class CapabilityValueEditor extends Composite implements ValueEditor<Capability> {

    interface CapabilityValueEditorUiBinder extends UiBinder<HTMLPanel, CapabilityValueEditor> {

    }

    private static CapabilityValueEditorUiBinder ourUiBinder = GWT.create(CapabilityValueEditorUiBinder.class);

    private final CapabilityPresenterSelector presenter;

    @UiField
    SimplePanel container;

    @Inject
    public CapabilityValueEditor(CapabilityPresenterSelector presenter) {
        this.presenter = presenter;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        presenter.start(container);
    }

    @Override
    public void setValue(Capability object) {
        presenter.setValue(object);
    }

    @Override
    public void clearValue() {
        presenter.clearValue();
    }

    @Override
    public Optional<Capability> getValue() {
        return presenter.getValue();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<Capability>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isDirty() {
        return presenter.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public boolean isWellFormed() {
        return presenter.isWellFormed();
    }
}
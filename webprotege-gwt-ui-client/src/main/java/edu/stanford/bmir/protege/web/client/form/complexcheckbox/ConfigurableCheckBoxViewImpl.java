package edu.stanford.bmir.protege.web.client.form.complexcheckbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import javax.inject.Inject;

import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConfigurableCheckBoxViewImpl extends Composite implements ConfigurableCheckBoxView {
    Logger logger = java.util.logging.Logger.getLogger("ConfigurableCheckBoxViewImpl");

    private boolean enabled = true;

    private boolean selected = false;

    private CheckboxValue checkboxValue;

    private boolean readOnly = false;

    interface CheckBoxViewImplUiBinder extends UiBinder<HTMLPanel, ConfigurableCheckBoxViewImpl> {
    }

    private static CheckBoxViewImplUiBinder ourUiBinder = GWT.create(CheckBoxViewImplUiBinder.class);

    @UiField
    HTMLPanel input;

    @UiField
    Label label;

    @UiField
    ConfigurableCheckBoxResourceBundle cb;

    @Inject
    public ConfigurableCheckBoxViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));

        sinkEvents(Event.ONKEYUP);
        sinkEvents(Event.ONMOUSEDOWN);
        sinkEvents(Event.ONMOUSEUP);

        cb.style().ensureInjected();
        updateState();
    }

    public void setFocusable(boolean focusable) {
    }

    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return addHandler(handler, KeyUpEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addHandler(handler, MouseDownEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return addHandler(handler, MouseUpEvent.getType());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<CheckboxValue> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        updateState();
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        updateState();
    }

    @Override
    public CheckboxValue getValue() {
        return checkboxValue;
    }

    @Override
    public void setValue(CheckboxValue value) {
        setValue(value, true);
    }

    @Override
    public void setValue(CheckboxValue value, boolean fireEvents) {
        if(this.checkboxValue == null || !this.checkboxValue.equals(value)) {
        logger.info("ALEX in configurableView schimb " + this.checkboxValue + " cu " + value.getValue() );
            this.checkboxValue = value;
            updateState();
            if (fireEvents) {
                ValueChangeEvent.fire(this, this.checkboxValue);
            }
        }
    }

    private void updateState() {
        if (checkboxValue != null) {
            input.getElement().setInnerHTML(checkboxValue.getSvgImage());
        }
        setFocusable(enabled);
    }

    @Override
    public String getText() {
        return label.getText();
    }

    @Override
    public void setText(String text) {
        label.setText(checkNotNull(text));
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        logger.info("ALEX setez readonly din view " + readOnly);

        this.readOnly = readOnly;
        updateState();
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setFocus(boolean focus) {
        if (focus) {
            getElement().focus();
        }
        else {
            getElement().blur();
        }
    }
}
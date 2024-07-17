package edu.stanford.bmir.protege.web.client.form.complexcheckbox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.HasReadOnly;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

public class ThreeStateCheckbox implements IsWidget, HasValue<CheckboxValue>, HasText, HasEnabled, HasReadOnly, ClickHandler {
    Logger logger = java.util.logging.Logger.getLogger("ThreeStateCheckbox");

    @Nonnull
    private final SimplePanel container = new SimplePanel();

    @Nonnull
    private final ConfigurableCheckBoxPresenter presenter;

    private final CheckBoxConfig checkBoxConfig;

    public ThreeStateCheckbox(CheckBoxConfig checkBoxConfig, CheckboxValue initialValue) {
        presenter = new ConfigurableCheckBoxPresenter(new ConfigurableCheckBoxViewImpl());
        presenter.start(container);
        this.checkBoxConfig = checkBoxConfig;

        presenter.setEnabled(checkBoxConfig.isEnabled());
        presenter.setReadOnly(checkBoxConfig.isReadOnly());
        logger.info("ALEX presenter dupa setare " + presenter.isEnabled() + " " + presenter.isReadOnly());
        this.setValue(initialValue);
        presenter.setConfig(this.checkBoxConfig);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public CheckboxValue getValue() {
        return presenter.getValue();
    }

    @Override
    public void setValue(CheckboxValue value) {
        presenter.setValue(value);
    }

    @Override
    public void setValue(CheckboxValue value, boolean fireEvents) {
        presenter.setValue(value, fireEvents);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<CheckboxValue> handler) {
        return presenter.addValueChangeHandler(handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        presenter.fireEvent(event);
    }

    @Override
    public String getText() {
        return presenter.getText();
    }

    @Override
    public void setText(String text) {
        presenter.setText(text);
    }

    @Override
    public boolean isEnabled() {
        return presenter.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        presenter.setEnabled(enabled);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        presenter.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return presenter.isReadOnly();
    }

    public void setFocus(boolean focus) {
        presenter.setFocus(focus);
    }

    @Override
    public void onClick(ClickEvent event) {
        CheckboxValue nextValue = checkBoxConfig.getNextValue(getValue());
        logger.info("ALEX pe threeStateCheckbox trec la " + nextValue);
        setValue(nextValue);
    }
}

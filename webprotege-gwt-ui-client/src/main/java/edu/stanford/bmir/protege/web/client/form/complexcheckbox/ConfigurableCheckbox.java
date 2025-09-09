package edu.stanford.bmir.protege.web.client.form.complexcheckbox;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.HasReadOnly;

import javax.annotation.Nonnull;

public class ConfigurableCheckbox implements IsWidget, HasValue<CheckboxValue>, HasText, HasEnabled, HasReadOnly {
    @Nonnull
    private final SimplePanel container = new SimplePanel();

    @Nonnull
    private final ConfigurableCheckBoxPresenter presenter;


    private final CheckBoxConfig checkBoxConfig;

    public ConfigurableCheckbox(CheckBoxConfig checkBoxConfig, String initialValue) {
        ConfigurableCheckBoxResourceBundle.CheckBoxCss style = ConfigurableCheckBoxResourceBundle.INSTANCE.style();
        style.ensureInjected();
        container.addStyleName(style.getConfigurableCheckboxContainer());
        presenter = new ConfigurableCheckBoxPresenter(new ConfigurableCheckBoxViewImpl());
        presenter.start(container);

        this.checkBoxConfig = checkBoxConfig;

        presenter.setEnabled(checkBoxConfig.isEnabled());
        presenter.setReadOnly(checkBoxConfig.isReadOnly());
        presenter.setConfig(checkBoxConfig);
        CheckboxValue checkboxValue = checkBoxConfig.findValue(initialValue);
        this.setValue(checkboxValue);
        presenter.setTitle(checkboxValue.getTooltip());
    }

    public boolean isTouched() {
        return this.presenter.isTouched();
    }

    public void setTouched(boolean touched) {
        this.presenter.setTouched(touched);
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
        presenter.setTitle(value.getTooltip());
    }

    public void setValue(String value) {
        CheckboxValue checkboxValue = this.checkBoxConfig.findValue(value);
        this.setValue(checkboxValue);
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

    public void setTitle(String title) {
        presenter.setTitle(title);
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
}

package edu.stanford.bmir.protege.web.client.form.complexcheckbox;

import java.util.List;

public abstract class CheckBoxConfig {
    private final CheckboxValue defaultValue;
    private final List<CheckboxValue> scrollableValues;

    private final boolean enabled;

    private final boolean readOnly;


    protected CheckBoxConfig(CheckboxValue defaultValue, List<CheckboxValue> scrollableValues) {
        this.defaultValue = defaultValue;
        this.scrollableValues = scrollableValues;
        this.enabled = false;
        this.readOnly = true;
    }

    public CheckboxValue getNextValue(CheckboxValue checkboxValue) {
        int pos = scrollableValues.indexOf(checkboxValue);
        if(pos >= 0) {
            if(pos == scrollableValues.size() - 1) {
                pos = -1;
            }
            return scrollableValues.get(pos + 1);
        } else {
            throw new RuntimeException("Given value " + checkboxValue + " is not in scrollable values ");
        }
    }


    public CheckboxValue findValue(String inputValue) {
        if(inputValue == null || inputValue.isEmpty()) {
            return defaultValue;
        }
        return this.scrollableValues.stream().filter(value -> value.getValue().equalsIgnoreCase(inputValue))
                .findFirst()
                .orElse(defaultValue);
    }

    public boolean isReadOnly(){
        return readOnly;
    }

    public boolean isEnabled() {
        return enabled;
    }
}

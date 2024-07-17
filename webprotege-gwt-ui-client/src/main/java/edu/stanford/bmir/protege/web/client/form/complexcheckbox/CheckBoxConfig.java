package edu.stanford.bmir.protege.web.client.form.complexcheckbox;

import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

public abstract class CheckBoxConfig {
    Logger logger = java.util.logging.Logger.getLogger("CheckBoxConfig");

    private final Map<String, CheckboxValue> availableValues;
    private final LinkedList<CheckboxValue> scrollableValues;

    private final boolean enabled;

    private final boolean readOnly;


    protected CheckBoxConfig(Map<String, CheckboxValue> availableValues, LinkedList<CheckboxValue> scrollableValues) {
        this.availableValues = availableValues;
        this.scrollableValues = scrollableValues;
        this.enabled = true;
        this.readOnly = false;
    }

    CheckboxValue getNextValue(CheckboxValue checkboxValue) {
        logger.info("ALEX din config incerc sa iau urmatoarea valoare raportat la " + checkboxValue  + " din " + this.scrollableValues) ;
        int pos = scrollableValues.indexOf(checkboxValue);
        if(pos > 0) {
            if(pos == scrollableValues.size() - 1) {
                pos = -1;
            }
            return scrollableValues.get(pos + 1);
        } else {
            throw new RuntimeException("Given value " + checkboxValue + " is not in scrollable values ");
        }
    }


    public boolean isReadOnly(){
        return readOnly;
    }

    public boolean isEnabled() {
        return enabled;
    }
}

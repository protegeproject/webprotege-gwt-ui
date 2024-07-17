package edu.stanford.bmir.protege.web.client.linearization;

import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckBoxConfig;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckboxValue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LinearizationCheckboxConfig extends CheckBoxConfig {

    protected LinearizationCheckboxConfig(LinkedList<CheckboxValue> scrollableValues) {
        super(new HashMap<>(), scrollableValues);
    }
}

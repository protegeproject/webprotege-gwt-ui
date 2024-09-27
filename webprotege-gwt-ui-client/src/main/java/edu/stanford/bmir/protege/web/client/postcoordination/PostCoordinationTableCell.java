package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckboxValue;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.ConfigurableCheckbox;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;

import java.util.logging.Logger;

public class PostCoordinationTableCell {
        private ConfigurableCheckbox configurableCheckbox;
        private LinearizationDefinition linearizationDefinition;
        private PostCoordinationCheckboxConfig checkboxConfig;
        private PostCoordinationTableAxisLabel axisLabel;
        private PostCoordinationTableRow rowWrapper;
        private PostCoordinationTableCell parentCell;

    Logger logger = java.util.logging.Logger.getLogger("PostCoordinationTableCell");

    public PostCoordinationTableCell(LinearizationDefinition linearizationDefinition, PostCoordinationTableAxisLabel axisLabel, PostCoordinationTableRow parentRow) {
        this.checkboxConfig = new PostCoordinationCheckboxConfig();
        configurableCheckbox = new ConfigurableCheckbox(checkboxConfig, "NOT_ALLOWED");
        configurableCheckbox.setReadOnly(false);
        configurableCheckbox.setEnabled(true);

        this.linearizationDefinition = linearizationDefinition;
        this.axisLabel = axisLabel;
        this.rowWrapper = parentRow;
    }

    public Widget asWidget(){
        return configurableCheckbox.asWidget();
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<CheckboxValue> handler) {
        return this.configurableCheckbox.addValueChangeHandler(handler);
    }

    public void setSetValueAsDefaultParent(){
        if(parentCell != null) {
            this.setValue("DEFAULT_" + parentCell.getValue());
        } else {
            this.setValue("DEFAULT_NOT_ALLOWED");
        }
    }
    public void setState(boolean readOnly) {
        configurableCheckbox.setReadOnly(readOnly);
        configurableCheckbox.setEnabled(!readOnly);
    }

    public void setValue(String value) {
       this.configurableCheckbox.setValue(value);
    }

    public  boolean isTouched(){
        return configurableCheckbox.isTouched();
    }

    public void setParentCell(PostCoordinationTableCell parentCell) {
        this.parentCell = parentCell;
    }

    public String getValue() {
        return this.configurableCheckbox.getValue().getValue();
    }

    public CheckboxValue getAsCheckboxValue(){
        return this.configurableCheckbox.getValue();
    }

    public LinearizationDefinition getLinearizationDefinition() {
        return linearizationDefinition;
    }

    public PostCoordinationTableAxisLabel getAxisLabel() {
        return axisLabel;
    }

    public void setParentValue(CheckboxValue parentValue) {
        this.checkboxConfig.setParentValue(parentValue);
    }
}

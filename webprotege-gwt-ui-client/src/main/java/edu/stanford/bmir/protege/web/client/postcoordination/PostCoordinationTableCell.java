package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckboxValue;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.ConfigurableCheckbox;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PostCoordinationTableCell {
        private ConfigurableCheckbox configurableCheckbox;
        private LinearizationDefinition linearizationDefinition;
        private PostCoordinationCheckboxConfig checkboxConfig;
        private PostCoordinationTableAxisLabel axisLabel;
        private List<PostCoordinationTableCell> childCells = new ArrayList<>();
    Logger logger = java.util.logging.Logger.getLogger("PostCoordinationTableCell");

    public PostCoordinationTableCell(LinearizationDefinition linearizationDefinition, PostCoordinationTableAxisLabel axisLabel, PostCoordinationTableRow parentRow) {
        this.checkboxConfig = new PostCoordinationCheckboxConfig();


        this.linearizationDefinition = linearizationDefinition;

        this.axisLabel = axisLabel;
        String initialValue = "NOT_ALLOWED";
        if(linearizationDefinition.getCoreLinId() != null && !linearizationDefinition.getCoreLinId().isEmpty()) {
            initialValue = "DEFAULT_NOT_ALLOWED";
        }
        configurableCheckbox = new ConfigurableCheckbox(checkboxConfig, initialValue);
        configurableCheckbox.setReadOnly(false);
        configurableCheckbox.setEnabled(true);
    }

    public Widget asWidget(){
        return configurableCheckbox.asWidget();
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<CheckboxValue> handler) {
        return this.configurableCheckbox.addValueChangeHandler(handler);
    }
    public void setState(boolean readOnly) {
        configurableCheckbox.setReadOnly(readOnly);
        configurableCheckbox.setEnabled(!readOnly);
    }

    public void setValue(String value) {
       this.configurableCheckbox.setValue(value);
       for(PostCoordinationTableCell childCell: this.childCells) {
           if(childCell.getValue().startsWith("DEFAULT")) {
               childCell.setValue("DEFAULT_"+value);
           }
       }
    }

    public void addToChildCells(PostCoordinationTableCell childCell) {
        this.childCells.add(childCell);
    }

    public  boolean isTouched(){
        return configurableCheckbox.isTouched();
    }

    public void initializeCallback(){
        this.configurableCheckbox.addValueChangeHandler((checkboxValue -> {
            this.updateChildCells(checkboxValue.getValue().getValue());
        }));
    }

    public void reset(){
        this.childCells = new ArrayList<>();
        this.configurableCheckbox.setTouched(false);
        if(this.checkboxConfig.isDerived()) {
            this.setValue("DEFAULT_NOT_ALLOWED");
        } else {
            this.setValue("NOT_ALLOWED");
        }
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

    public void setIsDerived(){
        this.checkboxConfig.setIsDerived(true);
    }

    public void updateChildCells(String checkboxValue) {
        if(!checkboxConfig.isDerived()) {
            for(PostCoordinationTableCell childCell: this.childCells) {
                if(childCell.getValue().startsWith("DEFAULT")) {
                    childCell.setValue("DEFAULT_"+checkboxValue);
                }
                childCell.setParentValue(this.getAsCheckboxValue());
            }
        }
    }

    public void updateChildren() {
        updateChildCells(this.getValue());
    }

    public void setParentValue(CheckboxValue parentValue) {
        this.checkboxConfig.setParentValue(parentValue);
    }
}

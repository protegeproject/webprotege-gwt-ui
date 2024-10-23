package edu.stanford.bmir.protege.web.client.logicaldefinition;

import edu.stanford.bmir.protege.web.shared.frame.PropertyClassValue;

public class LogicalDefinitionTableRow {

    private String postCoordinationAxis;
    private String postCoordinationAxisLabel;
    private String postCoordinationValue;

    private String postCoordinationValueLabel;

    public LogicalDefinitionTableRow(){

    }
    public LogicalDefinitionTableRow(PropertyClassValue propertyClassValue) {
        this.setPostCoordinationAxis(propertyClassValue.getProperty().getEntity().getIRI().toString());
        this.setPostCoordinationAxisLabel(propertyClassValue.getProperty().getBrowserText());
        this.setPostCoordinationValue(propertyClassValue.getValue().getIri().toString());
        this.setPostCoordinationValueLabel(propertyClassValue.getValue().getBrowserText());
    }

    public String getPostCoordinationAxis() {
        return postCoordinationAxis;
    }

    public void setPostCoordinationAxis(String postCoordinationAxis) {
        this.postCoordinationAxis = postCoordinationAxis;
    }

    public String getPostCoordinationAxisLabel() {
        return postCoordinationAxisLabel;
    }

    public void setPostCoordinationAxisLabel(String postCoordinationAxisLabel) {
        this.postCoordinationAxisLabel = postCoordinationAxisLabel;
    }

    public String getPostCoordinationValue() {
        return postCoordinationValue;
    }

    public void setPostCoordinationValue(String postCoordinationValue) {
        this.postCoordinationValue = postCoordinationValue;
    }

    public String getPostCoordinationValueLabel() {
        return postCoordinationValueLabel;
    }

    public void setPostCoordinationValueLabel(String postCoordinationValueLabel) {
        this.postCoordinationValueLabel = postCoordinationValueLabel;
    }

    @Override
    public String toString() {
        return "LogicalDefinitionTableRow{" +
                "postCoordinationAxis='" + postCoordinationAxis + '\'' +
                ", postCoordinationAxisLabel='" + postCoordinationAxisLabel + '\'' +
                ", postCoordinationValue='" + postCoordinationValue + '\'' +
                ", postCoordinationValueLabel='" + postCoordinationValueLabel + '\'' +
                '}';
    }
}

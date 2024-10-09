package edu.stanford.bmir.protege.web.client.logicaldefinition;

public class LogicalDefinitionTableConfig {
    private final String axisLabel;
    private final String valueLabel;

    private final ValueChangeHandler changeHandler;

    public LogicalDefinitionTableConfig(String axisLabel, String valueLabel, ValueChangeHandler changeHandler) {
        this.axisLabel = axisLabel;
        this.valueLabel = valueLabel;
        this.changeHandler = changeHandler;
    }

    public String getAxisLabel() {
        return axisLabel;
    }

    public String getValueLabel() {
        return valueLabel;
    }

    public ValueChangeHandler getChangeHandler() {
        return changeHandler;
    }

    public interface ValueChangeHandler {
        public void handleChange(String postCoordinationAxis, LogicalDefinitionTable table);
    }
}

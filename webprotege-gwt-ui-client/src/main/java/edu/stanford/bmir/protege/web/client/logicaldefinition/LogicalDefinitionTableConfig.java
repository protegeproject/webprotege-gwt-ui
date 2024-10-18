package edu.stanford.bmir.protege.web.client.logicaldefinition;

import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyPopupPresenter;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyPopupPresenterFactory;

public class LogicalDefinitionTableConfig {
    private final String axisLabel;
    private final String valueLabel;
    private final ValueChangeHandler changeHandler;

    private final HierarchyPopupPresenterFactory hierarchyPopupPresenterFactory;

    public LogicalDefinitionTableConfig(String axisLabel, String valueLabel, ValueChangeHandler changeHandler, HierarchyPopupPresenterFactory hierarchyPopupPresenterFactory) {
        this.axisLabel = axisLabel;
        this.hierarchyPopupPresenterFactory = hierarchyPopupPresenterFactory;
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


    public HierarchyPopupPresenterFactory getHierarchyPopupPresenterFactory() {
        return hierarchyPopupPresenterFactory;
    }

    public interface ValueChangeHandler {
        void handleChange(String postCoordinationAxis, LogicalDefinitionTable table);
    }

}

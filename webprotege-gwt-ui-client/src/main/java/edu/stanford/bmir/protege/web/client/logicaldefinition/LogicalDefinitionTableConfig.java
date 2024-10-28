package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyPopupPresenterFactory;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.postcoordination.WhoficCustomScalesValues;

public class LogicalDefinitionTableConfig {
    private final String axisLabel;
    private final String valueLabel;
    private final ValueChangeHandler changeHandler;

    private final AddAxisValueHandler addAxisValueHandler;


    public LogicalDefinitionTableConfig(String axisLabel, String valueLabel, ValueChangeHandler changeHandler, AddAxisValueHandler addAxisValueHandler) {
        this.axisLabel = axisLabel;
        this.valueLabel = valueLabel;
        this.changeHandler = changeHandler;
        this.addAxisValueHandler = addAxisValueHandler;
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

    public AddAxisValueHandler getAddAxisValueHandler() {
        return addAxisValueHandler;
    }

    public interface ValueChangeHandler {
        void handleChange(String postCoordinationAxis, LogicalDefinitionTable table);

    }

    public interface AddAxisValueHandler {
        void handleAddAxisValue(String postCoordinationAxis, LogicalDefinitionTable table, WhoficCustomScalesValues superclassScalesValue);
    }

    public interface SelectedAxisValueHandler {
        void handleSelectAxisValue(EntityNode selectedEntity);
    }
}

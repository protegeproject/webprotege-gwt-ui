package edu.stanford.bmir.protege.web.shared.logicaldefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.frame.PropertyClassValue;

import java.util.List;

import static edu.stanford.bmir.protege.web.shared.logicaldefinition.SaveLogicalDefinitionAction.CHANNEL;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class SaveLogicalDefinitionAction implements Action<SaveLogicalDefinitionResult> {

    public static final String CHANNEL = "icatx.logicalDefinitions.UpdateLogicalDefinition";


    @JsonCreator
    public static SaveLogicalDefinitionAction create(@JsonProperty("logicalDefinitions") List<LogicalDefinition> logicalDefinitions,
                                                     @JsonProperty("necessaryConditions") List<PropertyClassValue> necessaryConditions) {
        return new AutoValue_SaveLogicalDefinitionAction(logicalDefinitions, necessaryConditions);

    }
    @JsonProperty("logicalDefinitions")
    public abstract List<LogicalDefinition> getLogicalDefinitions();
    @JsonProperty("necessaryConditions")
    public abstract List<PropertyClassValue> getNecessaryConditions();


}

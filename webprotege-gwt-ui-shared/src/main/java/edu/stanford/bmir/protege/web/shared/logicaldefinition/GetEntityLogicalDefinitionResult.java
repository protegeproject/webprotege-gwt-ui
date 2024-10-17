package edu.stanford.bmir.protege.web.shared.logicaldefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.frame.PropertyClassValue;

import java.util.List;

import static edu.stanford.bmir.protege.web.shared.logicaldefinition.GetEntityLogicalDefinitionAction.CHANNEL;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class GetEntityLogicalDefinitionResult implements Result {



    @JsonCreator
    public static GetEntityLogicalDefinitionResult create(@JsonProperty("logicalDefinitions") List<LogicalDefinition> logicalDefinitions,
                                                          @JsonProperty("necessaryConditions") List<PropertyClassValue> necessaryConditions) {
        return new AutoValue_GetEntityLogicalDefinitionResult(logicalDefinitions, necessaryConditions);
    }



    @JsonProperty("logicalDefinitions")
    public abstract List<LogicalDefinition> getLogicalDefinitions();

    @JsonProperty("necessaryConditions")
    public abstract List<PropertyClassValue> getNecessaryConditions();

}

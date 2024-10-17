package edu.stanford.bmir.protege.web.shared.logicaldefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.frame.PropertyClassValue;

import java.io.Serializable;
import java.util.List;


@AutoValue
@GwtCompatible(serializable = true)
public abstract class LogicalConditions implements Serializable, IsSerializable {


    @JsonCreator
    public static LogicalConditions create(@JsonProperty("logicalDefinitions") List<LogicalDefinition> logicalDefinitions,
                                                       @JsonProperty("necessaryConditions") List<PropertyClassValue> necessaryConditions) {
        return new AutoValue_LogicalConditions(logicalDefinitions, necessaryConditions);
    }
    @JsonProperty("logicalDefinitions")
    public abstract List<LogicalDefinition> getLogicalDefinitions();
    @JsonProperty("necessaryConditions")
    public abstract List<PropertyClassValue> getNecessaryConditions();

}

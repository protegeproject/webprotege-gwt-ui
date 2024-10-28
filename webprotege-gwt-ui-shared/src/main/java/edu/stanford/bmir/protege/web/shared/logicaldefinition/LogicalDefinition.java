package edu.stanford.bmir.protege.web.shared.logicaldefinition;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyClassValue;

import java.io.Serializable;
import java.util.List;


@AutoValue
@GwtCompatible(serializable = true)
public abstract class LogicalDefinition implements Serializable, IsSerializable {

    @JsonCreator
    public static LogicalDefinition create(@JsonProperty("logicalDefinitionParent") OWLClassData logicalDefinitonParent,
                                           @JsonProperty("axis2filler") List<PropertyClassValue> axis2filler) {
        return new AutoValue_LogicalDefinition(logicalDefinitonParent, axis2filler);
    }

    @JsonProperty("logicalDefinitionParent")
    public abstract OWLClassData getLogicalDefinitionParent();

    @JsonProperty("axis2filler")
    public abstract List<PropertyClassValue> getAxis2filler();

}

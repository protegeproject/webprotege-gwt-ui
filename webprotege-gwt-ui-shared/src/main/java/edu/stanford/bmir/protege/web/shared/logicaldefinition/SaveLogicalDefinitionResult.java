package edu.stanford.bmir.protege.web.shared.logicaldefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static edu.stanford.bmir.protege.web.shared.logicaldefinition.SaveLogicalDefinitionAction.CHANNEL;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class SaveLogicalDefinitionResult implements Result  {


    @JsonCreator
    public static SaveLogicalDefinitionResult create(){
        return new AutoValue_SaveLogicalDefinitionResult();
    }
}

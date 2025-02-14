package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static edu.stanford.bmir.protege.web.shared.postcoordination.SaveEntityCustomScaleAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class SaveEntityCustomScaleResult implements Result {

    @JsonCreator
    public static SaveEntityCustomScaleResult create(){
        return new AutoValue_SaveEntityCustomScaleResult();
    }
}

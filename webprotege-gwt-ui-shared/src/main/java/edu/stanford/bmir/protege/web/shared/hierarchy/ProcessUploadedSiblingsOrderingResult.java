package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(ProcessUploadedSiblingsOrderingAction.CHANNEL)
public abstract class ProcessUploadedSiblingsOrderingResult implements Result {

    @JsonCreator
    public static ProcessUploadedSiblingsOrderingResult create() {
        return new AutoValue_ProcessUploadedSiblingsOrderingResult();
    }
}

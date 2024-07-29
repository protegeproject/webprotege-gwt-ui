package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static edu.stanford.bmir.protege.web.shared.linearization.ProcessUploadedLinearizationAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class ProcessUploadedLinearizationResult implements Result {

    @JsonCreator
    public static ProcessUploadedLinearizationResult create() {
        return new AutoValue_ProcessUploadedLinearizationResult();
    }
}

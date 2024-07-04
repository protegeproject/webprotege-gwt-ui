package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.projects.ProcessUploadedLinearization")
public abstract class ProcessUploadedLinearizationResult implements Result {

    @JsonCreator
    public static ProcessUploadedLinearizationResult create() {
        return new AutoValue_ProcessUploadedLinearizationResult();
    }
}

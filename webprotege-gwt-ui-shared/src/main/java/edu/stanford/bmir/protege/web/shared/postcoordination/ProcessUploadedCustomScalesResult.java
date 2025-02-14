package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static edu.stanford.bmir.protege.web.shared.postcoordination.ProcessUploadedCustomScalesAction.CHANNEL;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class ProcessUploadedCustomScalesResult implements Result {


    @JsonCreator
    public static ProcessUploadedCustomScalesResult create() {
        return new AutoValue_ProcessUploadedCustomScalesResult();
    }
}

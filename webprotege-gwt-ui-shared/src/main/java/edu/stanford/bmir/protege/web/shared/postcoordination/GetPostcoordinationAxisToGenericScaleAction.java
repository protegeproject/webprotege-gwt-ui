package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import static edu.stanford.bmir.protege.web.shared.postcoordination.GetPostcoordinationAxisToGenericScaleAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public class GetPostcoordinationAxisToGenericScaleAction implements Action<GetPostcoordinationAxisToGenericScaleResult> {

    public static final String CHANNEL = "webprotege.postcoordination.GetPostcoordinationAxisToGenericScale";

    public static GetPostcoordinationAxisToGenericScaleAction create() {
        return new AutoValue_GetPostcoordinationAxisToGenericScaleAction();
    }
}

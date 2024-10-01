package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.postcoordination.GetPostCoordinationAxisToGenericScale")
public class GetPostCoordinationAxisToGenericScaleAction implements Action<GetPostCoordinationAxisToGenericScaleResult> {

    public static GetPostCoordinationAxisToGenericScaleAction create() {
        return new AutoValue_GetPostCoordinationAxisToGenericScaleAction();
    }
}

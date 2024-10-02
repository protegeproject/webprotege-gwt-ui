package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.List;

import static edu.stanford.bmir.protege.web.shared.postcoordination.GetPostcoordinationAxisToGenericScaleAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class GetPostcoordinationAxisToGenericScaleResult implements Result {

    @JsonCreator
    public static GetPostcoordinationAxisToGenericScaleResult create(@JsonProperty("postcoordinationAxisToGenericScales") List<PostcoordinationAxisToGenericScale> postcoordinationAxisToGenericScales) {
        return new AutoValue_GetPostcoordinationAxisToGenericScaleResult(postcoordinationAxisToGenericScales);
    }

    @JsonProperty("postcoordinationAxisToGenericScales")
    public abstract List<PostcoordinationAxisToGenericScale> getPostcoordinationAxisToGenericScales();
}

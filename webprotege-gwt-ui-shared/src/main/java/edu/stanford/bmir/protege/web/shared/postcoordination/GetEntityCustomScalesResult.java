package edu.stanford.bmir.protege.web.shared.postcoordination;


import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.postcoordination.GetEntityScaleValues")
public abstract class GetEntityCustomScalesResult implements Result {

    @JsonProperty("whoficCustomScaleValues")
    public abstract WhoficCustomScalesValues getWhoficCustomScaleValues();

    @JsonCreator
    public static GetEntityCustomScalesResult create(@JsonProperty("whoficCustomScaleValues") WhoficCustomScalesValues whoficCustomScaleValues) {
        return new AutoValue_GetEntityCustomScalesResult(whoficCustomScaleValues);
    }
}

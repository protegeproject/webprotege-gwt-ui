package edu.stanford.bmir.protege.web.shared.postcoordination;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.postcoordination.GetEntityScaleValues")
public abstract class GetEntityCustomScalesResult implements Result {
    @JsonProperty("entityIri")
    public abstract String getEntityIri();

    @JsonProperty("postCoordinationScaleValues")
    public abstract WhoficCustomScalesValues getPostCoordinationScaleValues();

    @JsonCreator
    public static GetEntityCustomScalesResult create(@JsonProperty("entityIri") String entityIri,
                                                     @JsonProperty("postCoordinationScaleValues") WhoficCustomScalesValues postCoordinationScaleValues) {
        return new AutoValue_GetEntityCustomScalesResult(entityIri, postCoordinationScaleValues);
    }
}

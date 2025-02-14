package edu.stanford.bmir.protege.web.shared.postcoordination;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.postcoordination.GetEntityPostCoordinations")
public abstract class GetEntityPostCoordinationResult implements Result {

    @JsonProperty("entityIri")
    public abstract String getEntityIri();

    @JsonProperty("postCoordinationSpecification")
    public abstract WhoficEntityPostCoordinationSpecification getPostCoordinationSpecification();

    @JsonCreator
    public static GetEntityPostCoordinationResult create(@JsonProperty("entityIri") String entityIri,
                                                         @JsonProperty("postCoordinationSpecification") WhoficEntityPostCoordinationSpecification postCoordinationSpecification) {
        return new AutoValue_GetEntityPostCoordinationResult(entityIri, postCoordinationSpecification);
    }
}

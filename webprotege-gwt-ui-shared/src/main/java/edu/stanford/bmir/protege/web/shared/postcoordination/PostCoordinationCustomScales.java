package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class PostCoordinationCustomScales {

    @JsonCreator
    public static PostCoordinationCustomScales create(@JsonProperty("postcoordinationScaleValues") List<String> postcoordinationScaleValues,
                                                      @JsonProperty("postcoordinationAxis") String postcoordinationAxis) {
        return new AutoValue_PostCoordinationCustomScales(postcoordinationScaleValues, postcoordinationAxis);
    }

    @JsonProperty("postcoordinationScaleValues")
    public abstract List<String> getPostcoordinationScaleValues();

    @JsonProperty("postcoordinationAxis")
    public abstract String getPostcoordinationAxis();
}

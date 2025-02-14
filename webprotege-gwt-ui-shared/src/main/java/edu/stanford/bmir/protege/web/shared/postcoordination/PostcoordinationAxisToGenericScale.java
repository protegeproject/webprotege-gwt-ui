package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class PostcoordinationAxisToGenericScale {


    @JsonCreator
    public static PostcoordinationAxisToGenericScale create(@JsonProperty("postcoordinationAxis") String postcoordinationAxis,
                                                            @JsonProperty("genericPostcoordinationScaleTopClass") String genericPostcoordinationScaleTopClass,
                                                            @JsonProperty("allowMultiValue") String allowMultiValue) {
        return new AutoValue_PostcoordinationAxisToGenericScale(postcoordinationAxis, genericPostcoordinationScaleTopClass, allowMultiValue);
    }

    @JsonProperty("postcoordinationAxis")
    public abstract String getPostcoordinationAxis();

    @JsonProperty("genericPostcoordinationScaleTopClass")
    public abstract String getGenericPostcoordinationScaleTopClass();

    @JsonProperty("allowMultiValue")
    public abstract String getAllowMultiValue();
}

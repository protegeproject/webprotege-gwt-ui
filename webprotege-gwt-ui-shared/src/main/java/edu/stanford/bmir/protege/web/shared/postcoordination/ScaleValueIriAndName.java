package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class ScaleValueIriAndName {

    @JsonCreator
    public static ScaleValueIriAndName create(@JsonProperty("scaleValueIri")
                                              String scaleValueIri,
                                              @JsonProperty("scaleValueName")
                                              String scaleValueName) {
        return new AutoValue_ScaleValueIriAndName(scaleValueIri, scaleValueName);
    }

    public static ScaleValueIriAndName create(String scaleValueIri) {
        return ScaleValueIriAndName.create(scaleValueIri, "");
    }


    @Nonnull
    @JsonProperty("scaleValueIri")
    public abstract String getScaleValueIri();

    @JsonProperty("scaleValueName")
    public abstract String getScaleValueName();
}

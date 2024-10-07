package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class WhoficCustomScalesValues implements IsSerializable, Serializable {

    @JsonCreator
    public static WhoficCustomScalesValues create(@JsonProperty("whoficEntityIri") String whoficEntityIri,
                                                  @JsonProperty("scaleCustomizations") List<PostCoordinationCustomScales> scaleCustomizations) {
        return new AutoValue_WhoficCustomScalesValues(whoficEntityIri, scaleCustomizations);
    }

    @JsonProperty("whoficEntityIri")
    public abstract String getWhoficEntityIri();

    @JsonProperty("scaleCustomizations")
    public abstract List<PostCoordinationCustomScales> getScaleCustomizations();
}

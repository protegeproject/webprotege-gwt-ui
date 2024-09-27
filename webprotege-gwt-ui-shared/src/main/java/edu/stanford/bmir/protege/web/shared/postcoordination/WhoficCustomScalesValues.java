package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;
import java.util.List;

@GwtCompatible(serializable = true)
public class WhoficCustomScalesValues implements IsSerializable, Serializable {

    private String whoficEntityIri;
    private List<PostCoordinationCustomScales> scaleCustomizations;


    @JsonCreator
    public WhoficCustomScalesValues( @JsonProperty("whoficEntityIri") String whoficEntityIri,
                                     @JsonProperty("scaleCustomizations") List<PostCoordinationCustomScales> scaleCustomizations) {
        this.whoficEntityIri = whoficEntityIri;
        this.scaleCustomizations = scaleCustomizations;
    }

    @GwtSerializationConstructor
    private WhoficCustomScalesValues() {
    }

    @JsonProperty("whoficEntityIri")
    public String getWhoficEntityIri() {
        return whoficEntityIri;
    }

    @JsonProperty("scaleCustomizations")
    public List<PostCoordinationCustomScales> getScaleCustomizations() {
        return scaleCustomizations;
    }
}

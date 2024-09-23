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

    @JsonProperty("whoficEntityIri")
    private String whoficEntityIri;
    @JsonProperty("scaleCustomizations")
    private List<PostCoordinationCustomScalesRequest> scaleCustomizations;


    @JsonCreator
    public WhoficCustomScalesValues( @JsonProperty("whoficEntityIri") String whoficEntityIri,
                                     @JsonProperty("scaleCustomizations") List<PostCoordinationCustomScalesRequest> scaleCustomizations) {
        this.whoficEntityIri = whoficEntityIri;
        this.scaleCustomizations = scaleCustomizations;
    }

    @GwtSerializationConstructor
    private WhoficCustomScalesValues() {
    }

    public String getWhoficEntityIri() {
        return whoficEntityIri;
    }

    public List<PostCoordinationCustomScalesRequest> getScaleCustomizations() {
        return scaleCustomizations;
    }
}

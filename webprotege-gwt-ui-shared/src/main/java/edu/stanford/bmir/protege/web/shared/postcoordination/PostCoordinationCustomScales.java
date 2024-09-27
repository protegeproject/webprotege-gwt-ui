package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;
import java.util.List;

@GwtCompatible(serializable = true)
public class PostCoordinationCustomScales implements IsSerializable, Serializable {

    private List<String> postcoordinationScaleValues;
    private String postcoordinationAxis;


    @GwtSerializationConstructor
    public PostCoordinationCustomScales(){

    }

    @JsonCreator
    public PostCoordinationCustomScales(@JsonProperty("postcoordinationScaleValues") List<String> postcoordinationScaleValues,
                                        @JsonProperty("postcoordinationAxis") String postcoordinationAxis) {
        this.postcoordinationScaleValues = postcoordinationScaleValues;
        this.postcoordinationAxis = postcoordinationAxis;
    }

    @JsonProperty("postcoordinationScaleValues")
    public List<String> getPostcoordinationScaleValues() {
        return postcoordinationScaleValues;
    }

    @JsonProperty("postcoordinationAxis")
    public String getPostcoordinationAxis() {
        return postcoordinationAxis;
    }
}

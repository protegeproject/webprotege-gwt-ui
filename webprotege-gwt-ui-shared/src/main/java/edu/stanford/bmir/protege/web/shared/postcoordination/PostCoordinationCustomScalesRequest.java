package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;
import java.util.List;

@GwtCompatible(serializable = true)
public class PostCoordinationCustomScalesRequest  implements IsSerializable, Serializable {

    private List<String> postCoordinationScalesValues;
    private String postCoordinationAxis;


    @JsonCreator
    public PostCoordinationCustomScalesRequest(@JsonProperty("postcoordinationScaleValues") List<String> postCoordinationScalesValues,
                                               @JsonProperty("postcoordinationAxis") String postCoordinationAxis) {
        this.postCoordinationScalesValues = postCoordinationScalesValues;
        this.postCoordinationAxis = postCoordinationAxis;
    }

    @GwtSerializationConstructor
    private PostCoordinationCustomScalesRequest() {
    }

    @JsonProperty("postcoordinationScaleValues")
    public List<String> getPostCoordinationScalesValues() {
        return postCoordinationScalesValues;
    }

    @JsonProperty("postcoordinationAxis")
    public String getPostCoordinationAxis() {
        return postCoordinationAxis;
    }
}

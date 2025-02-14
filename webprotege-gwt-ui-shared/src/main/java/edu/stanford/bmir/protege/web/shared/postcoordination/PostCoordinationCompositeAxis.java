package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;
import java.util.List;

@GwtCompatible(serializable = true)
public class PostCoordinationCompositeAxis implements IsSerializable, Serializable {

    private String postCoordinationAxis;

    private List<String> subAxis;
    @GwtSerializationConstructor
    private PostCoordinationCompositeAxis() {
    }

    @JsonCreator
    public PostCoordinationCompositeAxis(@JsonProperty("postcoordinationAxis") String postCoordinationAxis, @JsonProperty("replacedBySubaxes") List<String> subAxis) {
        this.postCoordinationAxis = postCoordinationAxis;
        this.subAxis = subAxis;
    }

    @JsonProperty("postcoordinationAxis")
    public String getPostCoordinationAxis() {
        return postCoordinationAxis;
    }

    @JsonProperty("replacedBySubaxes")
    public List<String> getSubAxis() {
        return subAxis;
    }
}

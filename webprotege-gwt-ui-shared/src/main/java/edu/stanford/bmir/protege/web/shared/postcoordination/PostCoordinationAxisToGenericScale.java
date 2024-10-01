package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.*;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;

@GwtCompatible(serializable = true)
public class PostCoordinationAxisToGenericScale implements IsSerializable, Serializable {

    private String postcoordinationAxis;
    private String genericPostcoordinationScaleTopClass;
    private String allowMultiValue;

    @GwtSerializationConstructor
    private PostCoordinationAxisToGenericScale() {
    }

    public PostCoordinationAxisToGenericScale(String postcoordinationAxis,
                                              String genericPostcoordinationScaleTopClass,
                                              String allowMultiValue) {
        this.postcoordinationAxis = postcoordinationAxis;
        this.genericPostcoordinationScaleTopClass = genericPostcoordinationScaleTopClass;
        this.allowMultiValue = allowMultiValue;
    }

    @JsonCreator
    public PostCoordinationAxisToGenericScale create(@JsonProperty("postcoordinationAxis") String postcoordinationAxis,
                                                     @JsonProperty("genericPostcoordinationScaleTopClass") String genericPostcoordinationScaleTopClass,
                                                     @JsonProperty("allowMultiValue") String allowMultiValue) {
        return new PostCoordinationAxisToGenericScale(postcoordinationAxis, genericPostcoordinationScaleTopClass, allowMultiValue);
    }

    @JsonProperty("postcoordinationAxis")
    public String getPostcoordinationAxis() {
        return postcoordinationAxis;
    }

    @JsonProperty("genericPostcoordinationScaleTopClass")
    public String getGenericPostcoordinationScaleTopClass() {
        return genericPostcoordinationScaleTopClass;
    }

    @JsonProperty("allowMultiValue")
    public String getAllowMultiValue() {
        return allowMultiValue;
    }
}

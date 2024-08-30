package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;


import java.io.Serializable;
import java.util.List;

@GwtCompatible(serializable = true)
public class PostCoordinationTableConfiguration implements IsSerializable, Serializable {

    private String entityType;
    private List<String> postCoordinationAxes;

    private List<PostCoordinationCompositeAxis> compositePostCoordinationAxes;

    @GwtSerializationConstructor
    private PostCoordinationTableConfiguration() {
    }


    @JsonCreator
    public PostCoordinationTableConfiguration(@JsonProperty("entityType") String entityType, @JsonProperty("postcoordinationAxes") List<String> postCoordinationAxes, @JsonProperty("compositePostcoordinationAxes") List<PostCoordinationCompositeAxis> compositePostCoordinationAxes) {
        this.entityType = entityType;
        this.postCoordinationAxes = postCoordinationAxes;
        this.compositePostCoordinationAxes = compositePostCoordinationAxes;
    }


    @JsonProperty("entityType")
    public String getEntityType() {
        return entityType;
    }

    @JsonProperty("postcoordinationAxes")
    public List<String> getPostCoordinationAxes() {
        return postCoordinationAxes;
    }

    @JsonProperty("compositePostcoordinationAxes")
    public List<PostCoordinationCompositeAxis> getCompositePostCoordinationAxes() {
        return compositePostCoordinationAxes;
    }
}

package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;
import java.util.List;

@GwtCompatible(serializable = true)
public class WhoficEntityPostCoordinationSpecification implements IsSerializable, Serializable {
    private String whoficEntityIri;
    private String entityType;
    private List<PostCoordinationSpecification> postCoordinationSpecifications;

    @JsonCreator
    public WhoficEntityPostCoordinationSpecification(@JsonProperty("whoficEntityIri") String whoficEntityIri,
                                                     @JsonProperty("entityType") String entityType,
                                                     @JsonProperty("postCoordinationSpecifications") List<PostCoordinationSpecification> postCoordinationSpecifications) {
        this.whoficEntityIri = whoficEntityIri;
        this.entityType = entityType;
        this.postCoordinationSpecifications = postCoordinationSpecifications;
    }

    @GwtSerializationConstructor
    private WhoficEntityPostCoordinationSpecification() {
    }

    @JsonProperty("whoficEntityIri")
    public String getWhoficEntityIri() {
        return whoficEntityIri;
    }

    @JsonProperty("postCoordinationSpecifications")
    public List<PostCoordinationSpecification> getPostCoordinationSpecifications() {
        return postCoordinationSpecifications;
    }

    @JsonProperty("entityType")
    public String getEntityType() {
        return entityType;
    }

}

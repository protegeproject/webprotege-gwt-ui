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
    private List<PostCoordinationSpecification> postcoordinationSpecifications;

    @JsonCreator
    public WhoficEntityPostCoordinationSpecification(@JsonProperty("whoficEntityIri") String whoficEntityIri,
                                                     @JsonProperty("entityType") String entityType,
                                                     @JsonProperty("postcoordinationSpecifications") List<PostCoordinationSpecification> postcoordinationSpecifications) {
        this.whoficEntityIri = whoficEntityIri;
        this.entityType = entityType;
        this.postcoordinationSpecifications = postcoordinationSpecifications;
    }

    @GwtSerializationConstructor
    private WhoficEntityPostCoordinationSpecification() {
    }

    @JsonProperty("whoficEntityIri")
    public String getWhoficEntityIri() {
        return whoficEntityIri;
    }

    @JsonProperty("postcoordinationSpecifications")
    public List<PostCoordinationSpecification> getPostCoordinationSpecifications() {
        return postcoordinationSpecifications;
    }

    @JsonProperty("entityType")
    public String getEntityType() {
        return entityType;
    }

    @Override
    public String toString() {
        return "WhoficEntityPostCoordinationSpecification{" +
                "whoficEntityIri='" + whoficEntityIri + '\'' +
                ", entityType='" + entityType + '\'' +
                ", postcoordinationSpecifications=" + postcoordinationSpecifications +
                '}';
    }
}

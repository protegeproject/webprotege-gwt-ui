package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import org.semanticweb.owlapi.model.IRI;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@GwtCompatible(serializable = true)
public class WhoficEntityLinearizationSpecification implements IsSerializable, Serializable {

    private IRI entityIRI;
    private LinearizationResiduals linearizationResiduals;
    private List<LinearizationSpecification> linearizationSpecifications;

    @JsonCreator
    public WhoficEntityLinearizationSpecification(@JsonProperty("whoficEntityIri") IRI entityIRI,
                                                  @JsonProperty("linearizationResiduals") LinearizationResiduals linearizationResiduals,
                                                  @JsonProperty("linearizationSpecifications") List<LinearizationSpecification> linearizationSpecifications) {
        this.entityIRI = entityIRI;
        this.linearizationResiduals = linearizationResiduals;
        this.linearizationSpecifications = linearizationSpecifications;
    }

    @GwtSerializationConstructor
    private WhoficEntityLinearizationSpecification() {
    }

    @JsonProperty("whoficEntityIri")
    public IRI getEntityIRI() {
        return entityIRI;
    }

    @JsonProperty("linearizationResiduals")
    public LinearizationResiduals getLinearizationResiduals() {
        return linearizationResiduals;
    }

    @JsonProperty("linearizationSpecifications")
    public List<LinearizationSpecification> getLinearizationSpecifications() {
        return linearizationSpecifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WhoficEntityLinearizationSpecification)) return false;
        WhoficEntityLinearizationSpecification that = (WhoficEntityLinearizationSpecification) o;
        return Objects.equals(entityIRI, that.entityIRI) &&
                Objects.equals(linearizationResiduals, that.linearizationResiduals) &&
                Objects.equals(linearizationSpecifications, that.linearizationSpecifications);
    }

    @Override
    public String toString() {
        return "WhoficEntityLinearizationSpecification{" +
                "entityIRI=" + entityIRI +
                ", linearizationResiduals=" + linearizationResiduals +
                ", linearizationSpecifications=" + linearizationSpecifications +
                '}';
    }
}


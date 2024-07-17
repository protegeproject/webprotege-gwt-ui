package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import org.semanticweb.owlapi.model.IRI;

import java.io.Serializable;
import java.util.List;


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
        this.linearizationSpecifications = linearizationSpecifications;
        this.linearizationResiduals = linearizationResiduals;
    }

    @GwtSerializationConstructor
    private WhoficEntityLinearizationSpecification() {
    }

    public IRI getEntityIRI() {
        return entityIRI;
    }

    public LinearizationResiduals getLinearizationResiduals() {
        return linearizationResiduals;
    }

    public List<LinearizationSpecification> getLinearizationSpecifications() {
        return linearizationSpecifications;
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

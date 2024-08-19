package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.semanticweb.owlapi.model.IRI;



@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.linearization.GetEntityLinearizations")
public abstract class GetEntityLinearizationResult implements Result {

    public abstract IRI getEntityIri();

    public abstract WhoficEntityLinearizationSpecification getWhoficEntityLinearizationSpecification();

    @JsonCreator
    public static GetEntityLinearizationResult create(@JsonProperty("entityIri")
                                                               IRI entityIri,
                                                           @JsonProperty("linearizationSpecification")
                                                               WhoficEntityLinearizationSpecification linearizationSpecification) {
        return new AutoValue_GetEntityLinearizationResult(entityIri, linearizationSpecification);
    }
}

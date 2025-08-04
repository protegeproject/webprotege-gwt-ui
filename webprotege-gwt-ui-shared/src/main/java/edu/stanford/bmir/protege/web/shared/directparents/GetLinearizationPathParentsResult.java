package edu.stanford.bmir.protege.web.shared.directparents;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.linearization.GetParentsThatAreLinearizationPathParents")
public abstract class GetLinearizationPathParentsResult implements Result  {


    @JsonCreator
    public static GetLinearizationPathParentsResult create(
            @JsonProperty("parentsThatAreLinearizationPathParents")
            Set<IRI> parentsThatAreLinearizationPathParents,
            @JsonProperty("existingLinearizationParents") Set<IRI> existingLinearizationParents
    ) {
        return new AutoValue_GetLinearizationPathParentsResult(parentsThatAreLinearizationPathParents, existingLinearizationParents);
    }

    public abstract Set<IRI> getParentsThatAreLinearizationPathParents();

    public abstract Set<IRI> getExistingLinearizationParents();
}

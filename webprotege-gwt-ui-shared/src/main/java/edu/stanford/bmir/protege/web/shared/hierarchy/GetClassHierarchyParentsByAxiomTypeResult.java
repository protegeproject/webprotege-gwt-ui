package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.*;

import javax.annotation.*;
import java.util.*;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.GetClassHierarchyParentsByAxiomType")
public abstract class GetClassHierarchyParentsByAxiomTypeResult implements Result {

    public static GetClassHierarchyParentsByAxiomTypeResult create() {
        return create(null, Collections.emptySet(), Collections.emptySet());
    }

    @JsonCreator
    public static GetClassHierarchyParentsByAxiomTypeResult create(@JsonProperty("owlClass") @Nullable OWLClass owlClass,
                                                                   @JsonProperty("parentsBySubclassOf") @Nonnull Set<OWLEntityData> parentsBySubclassOf,
                                                                   @JsonProperty("parentsByEquivalentClass") @Nonnull Set<OWLEntityData> parentsByEquivalentClass) {
        return new AutoValue_GetClassHierarchyParentsByAxiomTypeResult(owlClass, parentsBySubclassOf, parentsByEquivalentClass);
    }

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nullable
    public abstract Set<OWLEntityData> getParentsBySubclassOf();

    @Nullable
    public abstract Set<OWLEntityData> getParentsByEquivalentClass();
}

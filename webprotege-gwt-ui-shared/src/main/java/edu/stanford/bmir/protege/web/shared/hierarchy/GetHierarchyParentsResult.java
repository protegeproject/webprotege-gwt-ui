package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.GetHierarchyParents")
public abstract class GetHierarchyParentsResult implements Result {

    public static GetHierarchyParentsResult create() {
        return create(null, Collections.emptySet());
    }

    @JsonCreator
    public static GetHierarchyParentsResult create(@JsonProperty("entity") @Nullable OWLEntity entity,
                                                   @JsonProperty("parents") @Nonnull Set<OWLEntityData> parents) {
        return new AutoValue_GetHierarchyParentsResult(parents, entity);
    }

    @Nullable
    public abstract Set<OWLEntityData> getParents();

    @Nonnull
    public abstract OWLEntity getEntity();
}

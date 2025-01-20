package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.*;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.ChangeEntityParents")
public abstract class ChangeEntityParentsResult implements Result {


    @JsonCreator
    public static ChangeEntityParentsResult create(@JsonProperty("classesWithCycle") @Nonnull Set<OWLEntityData> classesWithCycle,
                                                   @JsonProperty("classesWithRetiredParents") @Nonnull Set<OWLEntityData> classesWithRetiredParents,
                                                   @JsonProperty("linearizationPathParents") @Nullable Set<OWLEntityData> linearizationPathParents) {
        return new AutoValue_ChangeEntityParentsResult(classesWithCycle, classesWithRetiredParents, linearizationPathParents);
    }

    @JsonProperty("classesWithCycle")
    @Nonnull
    public abstract Set<OWLEntityData> getClassesWithCycle();

    @JsonProperty("classesWithRetiredParents")
    @Nonnull
    public abstract Set<OWLEntityData> getClassesWithRetiredParents();

    @JsonProperty("linearizationPathParents")
    @Nonnull
    public abstract Set<OWLEntityData> getLinearizationPathParents();

    public boolean hasClassesWithCycle() {
        return !getClassesWithCycle().isEmpty();
    }

    public boolean hasClassesWithRetiredParents() {
        return !getClassesWithRetiredParents().isEmpty();
    }

    public boolean hasParentAsLinearizationPathParent() {
        return !getLinearizationPathParents().isEmpty();
    }

    public boolean isFailure() {
        return hasClassesWithRetiredParents() || hasClassesWithCycle() || hasParentAsLinearizationPathParent();
    }

    public boolean isSuccess() {
        return !isFailure();
    }
}

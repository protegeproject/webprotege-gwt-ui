package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.ChangeEntityParents")
public abstract class ChangeEntityParentsResult implements Result {


    @JsonCreator
    public static ChangeEntityParentsResult create(@JsonProperty("classesWithCycle") @Nonnull Set<OWLEntityData> classesWithCycle,
                                                   @JsonProperty("classesWithRetiredParents") @Nonnull Set<OWLEntityData> classesWithRetiredParents,
                                                   @JsonProperty("linearizationPathParent") Optional<OWLEntityData> linearizationPathParent ) {
        return new AutoValue_ChangeEntityParentsResult(classesWithCycle, classesWithRetiredParents, linearizationPathParent);
    }

    @JsonProperty("classesWithCycle")
    @Nonnull
    public abstract Set<OWLEntityData> getClassesWithCycle();

    @JsonProperty("classesWithRetiredParents")
    @Nonnull
    public abstract Set<OWLEntityData> getClassesWithRetiredParents();

    @JsonProperty("linearizationPathParent")
    @Nonnull
    public abstract Optional<OWLEntityData> getLinearizationPathParent();

    public boolean hasClassesWithCycle(){
        return !getClassesWithCycle().isEmpty();
    }

    public boolean hasClassesWithRetiredParents(){
        return !getClassesWithRetiredParents().isEmpty();
    }

    public boolean hasLinearizationPathParent(){
        return getLinearizationPathParent().isPresent();
    }

    public boolean isFailure(){
        return hasClassesWithRetiredParents()||hasClassesWithCycle()||hasLinearizationPathParent();
    }

    public boolean isSuccess(){
        return !isFailure();
    }
}

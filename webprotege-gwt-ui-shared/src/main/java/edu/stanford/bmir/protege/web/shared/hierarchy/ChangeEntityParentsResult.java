package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
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
                                                   @JsonProperty("classesWithRetiredParents") @Nonnull Set<OWLEntityData> classesWithRetiredParents) {
        return new AutoValue_ChangeEntityParentsResult(classesWithCycle, classesWithRetiredParents);
    }

    @JsonProperty("classesWithCycle")
    @Nonnull
    public abstract Set<OWLEntityData> getClassesWithCycle();

    @JsonProperty("classesWithRetiredParents")
    @Nonnull
    public abstract Set<OWLEntityData> getClassesWithRetiredParents();

}

package edu.stanford.bmir.protege.web.shared.bulkop;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.MoveEntitiesToParent")
public abstract class MoveEntitiesToParentResult implements Result {

    @JsonCreator
    public static MoveEntitiesToParentResult create(@JsonProperty("isDestinationRetiredClass") @Nonnull boolean isDestinationRetiredClass) {
        return new AutoValue_MoveEntitiesToParentResult(isDestinationRetiredClass);
    }

    @JsonProperty("isDestinationRetiredClass")
    @Nonnull
    public abstract boolean isDestinationRetiredClass();

}

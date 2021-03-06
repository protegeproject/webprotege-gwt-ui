package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.perspectives.GetPerspectives")
public abstract class GetPerspectivesResult implements Result {

    @JsonCreator
    @Nonnull
    public static GetPerspectivesResult create(@JsonProperty("perspectives") @Nonnull ImmutableList<PerspectiveDescriptor> perspectives,
                                               @JsonProperty("resettablePerspectives") @Nonnull ImmutableSet<PerspectiveId> resettablePerspectives) {
        return new AutoValue_GetPerspectivesResult(perspectives, resettablePerspectives);
    }

    @Nonnull
    public abstract ImmutableList<PerspectiveDescriptor> getPerspectives();

    @Nonnull
    public abstract ImmutableSet<PerspectiveId> getResettablePerspectives();
}

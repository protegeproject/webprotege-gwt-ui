package edu.stanford.bmir.protege.web.shared.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.RenderedOwlEntities")
public abstract class GetRenderedOwlEntitiesResult implements Result {
    @JsonCreator
    public static GetRenderedOwlEntitiesResult create(@JsonProperty("renderedEntities") @Nonnull List<EntityNode> renderedEntities) {
        return new AutoValue_GetRenderedOwlEntitiesResult(renderedEntities);
    }

    @Nonnull
    public abstract List<EntityNode> getRenderedEntities();
}

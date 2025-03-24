package edu.stanford.bmir.protege.web.shared.directparents;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.*;

import javax.annotation.Nonnull;
import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.GetEntityDirectParents")
public abstract class GetEntityDirectParentsResult implements Result {

    @JsonCreator
    public static GetEntityDirectParentsResult create(
            @JsonProperty("entity") @Nonnull OWLEntityData entity,
            @JsonProperty("directParents") List<EntityNode> directParents
    ) {
        return new AutoValue_GetEntityDirectParentsResult(entity, directParents);
    }

    public abstract OWLEntityData getEntity();

    public abstract List<EntityNode> getDirectParents();
}

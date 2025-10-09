package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class CreateEntitiesOperationId {

    @JsonCreator
    public static CreateEntitiesOperationId create(String id) {
        return new AutoValue_CreateEntitiesOperationId(id);
    }

    @JsonValue
    public abstract String getId();

}

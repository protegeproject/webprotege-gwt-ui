package edu.stanford.bmir.protege.web.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class ViewId {

    @JsonCreator
    public static ViewId create(String id) {
        return new AutoValue_ViewId(id);
    }

    @JsonValue
    public abstract String getId();
}

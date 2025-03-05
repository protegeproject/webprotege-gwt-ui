package edu.stanford.bmir.protege.web.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class ViewNodeId {

    @JsonCreator
    public static ViewNodeId get(String id) {
        return new AutoValue_ViewNodeId(id);
    }

    @JsonValue
    public abstract String getId();
}

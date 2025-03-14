package edu.stanford.bmir.protege.web.shared.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class CustomContentId {

    @JsonCreator
    public static CustomContentId valueOf(String id) {
        return new AutoValue_CustomContentId(id);
    }

    @JsonValue
    public abstract String getId();
}

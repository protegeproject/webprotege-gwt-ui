package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-22
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormRegionId {

    @JsonCreator
    @Nonnull
    public static FormRegionId get(@Nonnull String id) {
        checkFormat(id);
        return new AutoValue_FormRegionId(id);
    }

    private static void checkFormat(@Nonnull String id) {
        if(!UUIDUtil.isWellFormed(id)) {
            throw new IllegalArgumentException("Malformed FormRegionId: " + id);
        }
    }

    @JsonValue
    public abstract String getId();

}

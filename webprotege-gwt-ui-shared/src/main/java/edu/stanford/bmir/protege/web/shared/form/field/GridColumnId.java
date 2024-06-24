package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
@GwtCompatible(serializable = true)
@AutoValue
@JsonTypeName("GridColumnId")
public abstract class GridColumnId implements FormRegionId, Serializable, IsSerializable {

    @JsonCreator
    @Nonnull
    public static GridColumnId get(@JsonProperty("id") @Nonnull String id) {
        checkFormat(id);
        return new AutoValue_GridColumnId(id);
    }

    private static void checkFormat(@Nonnull String id) {
        if(!UUIDUtil.isWellFormed(id)) {
            throw new IllegalArgumentException("Malformed GridColumnId: " + id);
        }
    }

    public GridColumnId(){

    }

    @Override
    public abstract String getId();
}

package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;

import javax.annotation.Nonnull;
import java.io.Serializable;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class ChangeRequestId implements IsSerializable, Serializable {

    @JsonCreator
    @Nonnull
    public static ChangeRequestId get(@Nonnull String id) {
        if(!UUIDUtil.isWellFormed(id)) {
            throw new IllegalArgumentException("Malformed ChangeRequestId.  ChangeRequestIds must be UUIDs");
        }
        return new AutoValue_ChangeRequestId(id);
    }

    /**
     * Gets the identifier for this perspective.  This is a human readable name.
     */
    @JsonValue
    @Nonnull
    public abstract String getId();

}

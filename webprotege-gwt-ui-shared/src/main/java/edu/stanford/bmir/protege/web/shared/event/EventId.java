package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Identifier for an event sent over the wire. Mirrors the backend's
 * {@code edu.stanford.protege.webprotege.common.EventId} record. The JSON form
 * is just the raw UUID string (via {@code @JsonValue}), so a client
 * {@code EventId} is wire-compatible with both the backend {@code EventId}
 * and a plain {@code String}.
 */
@GwtCompatible(serializable = true)
public class EventId implements Serializable, IsSerializable {

    private String id;

    private EventId(@Nonnull String id) {
        this.id = Objects.requireNonNull(id);
    }

    @GwtSerializationConstructor
    private EventId() {
    }

    @JsonCreator
    @Nonnull
    public static EventId get(@Nonnull String id) {
        return new EventId(id);
    }

    @Nonnull
    public static EventId valueOf(@Nonnull String id) {
        return get(id);
    }

    /**
     * Mints a fresh {@link EventId} backed by a random UUID.
     *
     * <p>JVM-only helper, mirroring {@code ChangeRequestId.generate()} and
     * {@code PerspectiveId.generate()}. Client code mints {@code EventId} via
     * {@code EventId.get(uuidV4Provider.get())} instead.</p>
     */
    @GwtIncompatible
    @Nonnull
    public static EventId generate() {
        return get(UUID.randomUUID().toString());
    }

    @JsonValue
    @Nonnull
    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EventId)) {
            return false;
        }
        return Objects.equals(this.id, ((EventId) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EventId{" + id + "}";
    }
}

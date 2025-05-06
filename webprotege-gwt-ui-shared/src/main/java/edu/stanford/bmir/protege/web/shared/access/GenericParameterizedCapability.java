package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class GenericParameterizedCapability implements Capability {


    @Nonnull
    @Override
    @JsonProperty("id")
    public abstract String getId();

    @Nonnull
    @JsonProperty("@type")
    public abstract String getType();

    @JsonAnyGetter
    public abstract Map<String, Object> getOtherFields();


    /**
     * Constructs a new {@code GenericParameterizedCapability}.
     *
     * @param type        The type of the capability, must not be {@code null}.
     * @param id          The unique identifier for the capability, must not be {@code null}.
     * @param otherFields A map of additional fields; may be {@code null}, in which case an empty map is used.
     * @throws NullPointerException if {@code type} or {@code id} is {@code null}.
     */
    public static GenericParameterizedCapability get(@JsonProperty("@type") String type,
                                                     @JsonProperty("id") String id,
                                                     Map<String, Object> otherFields) {
        return new AutoValue_GenericParameterizedCapability(id,  type, otherFields);
    }

    /**
     * Constructs a new {@code GenericParameterizedCapability}.
     *
     * @param type        The type of the capability, must not be {@code null}.
     * @param id          The unique identifier for the capability, must not be {@code null}.
     * @throws NullPointerException if {@code type} or {@code id} is {@code null}.
     */
    @JsonCreator
    public static GenericParameterizedCapability fromJson(@JsonProperty("@type") String type,
                                                          @JsonProperty("id") String id) {
        return new AutoValue_GenericParameterizedCapability(id,  type, new HashMap<>());
    }

    /**
     * Adds an additional field to the capability.
     * <p>
     * This method is used during deserialization to capture fields that are not explicitly
     * modeled as part of this record. These fields are stored in the {@code otherFields} map.
     * </p>
     *
     * @param field The name of the field.
     * @param value The value of the field.
     */
    @JsonAnySetter
    private void set(String field, Object value) {
        this.getOtherFields().put(field, value);
    }
}

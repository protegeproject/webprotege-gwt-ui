package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;
import java.io.Serializable;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("EntityStatus")
public abstract class EntityStatus implements Serializable, Comparable<EntityStatus> {

    public static final String STATUS = "status";

    @JsonCreator
    public static EntityStatus get(@Nonnull @JsonProperty(STATUS) String status) {
        return new AutoValue_EntityStatus(status);
    }


    @JsonProperty(STATUS)
    @Nonnull
    public abstract String getStatus();

    @Override
    public int compareTo(EntityStatus o) {
        return this.getStatus().compareToIgnoreCase(o.getStatus());
    }
}
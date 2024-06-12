package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;
import java.io.Serializable;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("EntityStatus")
public abstract class EntityStatus implements IsSerializable, Serializable, Comparable<EntityStatus> {

    @JsonCreator
    public static EntityStatus get(@Nonnull @JsonProperty("status") String status) {
        return new AutoValue_EntityStatus(status);
    }


    @JsonProperty("status")
    @Nonnull
    public abstract String getStatus();


    @Override
    public int compareTo(EntityStatus o) {
        return this.getStatus().compareToIgnoreCase(o.getStatus());
    }
}
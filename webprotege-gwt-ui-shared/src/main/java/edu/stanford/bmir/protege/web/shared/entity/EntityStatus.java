package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;

@AutoValue
public abstract class EntityStatus implements IsSerializable, Comparable<EntityStatus> {

    public static final String STATUS = "_status";

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
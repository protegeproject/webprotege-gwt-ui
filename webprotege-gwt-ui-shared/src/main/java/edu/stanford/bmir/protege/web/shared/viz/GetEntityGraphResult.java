package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.graphs.GetEntityGraph")
public abstract class GetEntityGraphResult implements Result {

    @JsonCreator
    public static GetEntityGraphResult get(@JsonProperty("graph") @Nonnull EntityGraph entityGraph,
                                           @JsonProperty("settings") @Nonnull EntityGraphSettings settings) {
        return new AutoValue_GetEntityGraphResult(entityGraph, settings);
    }

    @Nonnull
    public abstract EntityGraph getGraph();

    @Nonnull
    public abstract EntityGraphSettings getSettings();
}

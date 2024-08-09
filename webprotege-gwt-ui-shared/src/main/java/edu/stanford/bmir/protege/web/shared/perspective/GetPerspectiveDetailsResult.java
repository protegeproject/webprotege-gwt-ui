package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-03
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.perspectives.GetPerspectiveDetails")
public abstract class GetPerspectiveDetailsResult implements Result, IsSerializable, Serializable {

    @JsonCreator
    @Nonnull
    public static GetPerspectiveDetailsResult create(@JsonProperty("perspectiveDetails") @Nonnull ImmutableList<PerspectiveDetails> perspectiveDetails) {
        return new AutoValue_GetPerspectiveDetailsResult(perspectiveDetails);
    }

    @Nonnull
    public abstract ImmutableList<PerspectiveDetails> getPerspectiveDetails();
}

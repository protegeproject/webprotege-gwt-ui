package edu.stanford.bmir.protege.web.shared.frame;

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
 * 2021-11-05
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.frames.UpdateObjectPropertyFrame")
public abstract class UpdateObjectPropertyFrameResult implements Result {

    @JsonCreator
    public static UpdateObjectPropertyFrameResult get(@Nonnull @JsonProperty("updatedFrame") ObjectPropertyFrame frame) {
        return new AutoValue_UpdateObjectPropertyFrameResult(frame);
    }

    @Nonnull
    public abstract ObjectPropertyFrame getUpdatedFrame();

}

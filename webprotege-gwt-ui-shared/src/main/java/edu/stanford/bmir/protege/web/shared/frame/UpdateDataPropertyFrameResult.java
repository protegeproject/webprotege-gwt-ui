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
@JsonTypeName("webprotege.frames.UpdateDataPropertyFrame")
public abstract class UpdateDataPropertyFrameResult implements Result {

    @JsonCreator
    public static UpdateDataPropertyFrameResult get(@Nonnull @JsonProperty("updatedFrame") DataPropertyFrame frame) {
        return new AutoValue_UpdateDataPropertyFrameResult(frame);
    }

    @Nonnull
    public abstract DataPropertyFrame getUpdatedFrame();

}

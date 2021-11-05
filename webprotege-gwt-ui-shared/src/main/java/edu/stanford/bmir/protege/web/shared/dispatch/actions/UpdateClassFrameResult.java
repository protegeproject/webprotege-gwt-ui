package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-05
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.frames.UpdateClassFrame")
public abstract class UpdateClassFrameResult implements Result {

    @JsonCreator
    public static UpdateClassFrameResult get(@Nonnull @JsonProperty("updatedFrame") ClassFrame updatedFrame) {
        return new AutoValue_UpdateClassFrameResult(updatedFrame);
    }

    @Nonnull
    public abstract ClassFrame getUpdatedFrame();
}

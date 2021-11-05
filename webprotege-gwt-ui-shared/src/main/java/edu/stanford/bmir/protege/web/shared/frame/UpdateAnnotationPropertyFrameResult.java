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
@JsonTypeName("webprotege.frames.UpdateAnnotationPropertyFrame")
public abstract class UpdateAnnotationPropertyFrameResult implements Result {

    @JsonCreator
    public static UpdateAnnotationPropertyFrameResult get(@Nonnull @JsonProperty("updatedFrame") AnnotationPropertyFrame frame) {
        return new AutoValue_UpdateAnnotationPropertyFrameResult(frame);
    }

    @Nonnull
    public abstract AnnotationPropertyFrame getUpdatedFrame();

}

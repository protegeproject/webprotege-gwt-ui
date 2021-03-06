package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.frames.UpdateDataPropertyFrame")
public abstract class UpdateDataPropertyFrameAction extends UpdateFrameAction<UpdateDataPropertyFrameResult> implements HasProjectId {

    @JsonCreator
    public static UpdateDataPropertyFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                @JsonProperty("from") PlainDataPropertyFrame from,
                                                @JsonProperty("to") PlainDataPropertyFrame to) {
        return new AutoValue_UpdateDataPropertyFrameAction(projectId, from, to);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Override
    public abstract PlainDataPropertyFrame getFrom();

    @Override
    public abstract PlainDataPropertyFrame getTo();
}

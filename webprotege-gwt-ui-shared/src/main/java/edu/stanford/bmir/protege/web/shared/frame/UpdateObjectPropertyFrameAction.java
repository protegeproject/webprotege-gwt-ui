package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
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
@JsonTypeName("webprotege.frames.UpdateObjectPropertyFrame")
public abstract class UpdateObjectPropertyFrameAction extends UpdateFrameAction<UpdateObjectPropertyFrameResult> {


    @JsonCreator
    public static UpdateObjectPropertyFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                       @JsonProperty("from") PlainObjectPropertyFrame from,
                                                       @JsonProperty("to") PlainObjectPropertyFrame to,
                                                       @JsonProperty("changeRequestId") ChangeRequestId changeRequestId) {
        return new AutoValue_UpdateObjectPropertyFrameAction(projectId, from, to, changeRequestId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Override
    public abstract PlainObjectPropertyFrame getFrom();

    @Override
    public abstract PlainObjectPropertyFrame getTo();

    @Override
    public abstract ChangeRequestId getChangeRequestId();
}

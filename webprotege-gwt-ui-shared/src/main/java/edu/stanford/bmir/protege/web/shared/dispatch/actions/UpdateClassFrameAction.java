package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.frame.PlainAnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PlainClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.UpdateFrameAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.frames.UpdateClassFrame")
public abstract class UpdateClassFrameAction extends UpdateFrameAction<UpdateClassFrameResult> {

    @JsonCreator
    public static UpdateClassFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                @JsonProperty("from") PlainClassFrame from,
                                                @JsonProperty("to") PlainClassFrame to,
                                                @JsonProperty("changeRequestId") ChangeRequestId changeRequestId) {
        return new AutoValue_UpdateClassFrameAction(projectId, from, to, changeRequestId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Override
    public abstract PlainClassFrame getFrom();

    @Override
    public abstract PlainClassFrame getTo();

    @Override
    public abstract ChangeRequestId getChangeRequestId();
}

package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;


@JsonTypeName("webprotege.comments.GetEntityEarliestCommentTimestamp")
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetEntityEarliestCommentTimestampAction
        implements ProjectAction<GetEntityEarliestCommentTimestampResult> {

    @JsonCreator
    public static GetEntityEarliestCommentTimestampAction create(
            @JsonProperty("projectId") ProjectId projectId,
            @JsonProperty("entityIri") IRI entityIri) {
        return new AutoValue_GetEntityEarliestCommentTimestampAction(projectId, entityIri);
    }

    @Nonnull
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("entityIri")
    public abstract IRI getEntity();
}

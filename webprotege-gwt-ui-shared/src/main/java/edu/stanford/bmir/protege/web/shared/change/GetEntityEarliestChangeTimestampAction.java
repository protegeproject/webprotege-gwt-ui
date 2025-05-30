package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;


@JsonTypeName("webprotege.history.GetEntityEarliestChangeTimestamp")
@AutoValue
public abstract class GetEntityEarliestChangeTimestampAction
        implements ProjectAction<GetEntityEarliestChangeTimestampResult> {

    @JsonCreator
    public static GetEntityEarliestChangeTimestampAction create(
            @JsonProperty("projectId") ProjectId projectId,
            @JsonProperty("entityIri") IRI entityIri) {
        return new AutoValue_GetEntityEarliestChangeTimestampAction(projectId, entityIri);
    }

    @Nonnull
    public abstract ProjectId getProjectId();
    public abstract IRI getEntity();
}

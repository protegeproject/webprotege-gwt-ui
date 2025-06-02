package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;


@JsonTypeName("webprotege.history.GetEntityEarliestChangeTimestamp")
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetEntityEarliestChangeTimestampAction
        implements ProjectAction<GetEntityEarliestChangeTimestampResult> {

    @JsonCreator
    public static GetEntityEarliestChangeTimestampAction create(
            @JsonProperty("projectId") ProjectId projectId,
            @JsonProperty("entityIri") IRI entityIri) {
        return new AutoValue_GetEntityEarliestChangeTimestampAction(projectId, entityIri);
    }

    @Nonnull
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("entityIri")
    public abstract IRI getEntity();
}

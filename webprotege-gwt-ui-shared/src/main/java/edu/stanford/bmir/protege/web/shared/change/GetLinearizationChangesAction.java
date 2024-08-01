package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.*;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.linearization.GetLinearizationChanges")
public abstract class GetLinearizationChangesAction implements ProjectAction<GetLinearizationChangesResult> {

    @JsonCreator
    public static GetLinearizationChangesAction create(@JsonProperty("projectId") ProjectId projectId,
                                                       @JsonProperty("entity") OWLEntity subject,
                                                       @JsonProperty("pageRequest") PageRequest pageRequest) {
        return new AutoValue_GetLinearizationChangesAction(projectId, subject, pageRequest);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract OWLEntity getSubject();

    @Nonnull
    public abstract PageRequest getPageRequest();
}

package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.*;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.*;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@JsonTypeName("webprotege.linearization.GetLinearizationChanges")
public class GetLinearizationChangesAction implements ProjectAction<GetLinearizationChangesResult> {

    private ProjectId projectId;

    @Nullable
    private OWLEntity subject;

    private PageRequest pageRequest;

    @GwtSerializationConstructor
    private GetLinearizationChangesAction() {
    }

    private GetLinearizationChangesAction(@Nonnull ProjectId projectId,
                                          @Nonnull Optional<OWLEntity> subject,
                                          @Nonnull PageRequest pageRequest) {
        this.projectId = checkNotNull(projectId);
        this.subject = checkNotNull(subject).orElse(null);
        this.pageRequest = checkNotNull(pageRequest);
    }

    public static GetLinearizationChangesAction create(@Nonnull ProjectId projectId,
                                                       @Nonnull Optional<OWLEntity> subject,
                                                       @Nonnull PageRequest pageRequest) {
        return new GetLinearizationChangesAction(projectId, subject, pageRequest);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public Optional<OWLEntity> getSubject() {
        return Optional.ofNullable(subject);
    }

    @Nonnull
    public PageRequest getPageRequest() {
        return pageRequest;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, subject);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetLinearizationChangesAction)) {
            return false;
        }
        GetLinearizationChangesAction other = (GetLinearizationChangesAction) obj;
        return this.projectId.equals(other.projectId)
                && java.util.Objects.equals(this.subject, other.subject);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(GetLinearizationChangesAction.class.getName())
                .addValue(projectId)
                .addValue(subject)
                .toString();
    }
}

package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.*;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.*;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.change.GetProjectChangesForHistoryViewAction.CHANNEL;

@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public class GetProjectChangesForHistoryViewAction implements ProjectAction<GetProjectChangesForHistoryViewResult> {

    public final static String CHANNEL = "webprotege.events.projects.history.ProjectChangesForHistory";

    private ProjectId projectId;
    private OWLEntity subject;
    private PageRequest pageRequest;
    private String filter;

    @GwtSerializationConstructor
    private GetProjectChangesForHistoryViewAction() {
    }

    public GetProjectChangesForHistoryViewAction(ProjectId projectId, Optional<OWLEntity> subject, PageRequest pageRequest, String filter) {
        this.projectId = projectId;
        this.subject = checkNotNull(subject).orElse(null);
        this.pageRequest = pageRequest;
        this.filter = filter;
    }

    @JsonCreator
    public static GetProjectChangesForHistoryViewAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                               @JsonProperty("subject") @Nonnull Optional<OWLEntity> subject,
                                                               @JsonProperty("pageRequest") @Nonnull PageRequest pageRequest,
                                                               @JsonProperty("filter") String filter) {

        return new GetProjectChangesForHistoryViewAction(projectId, subject, pageRequest, filter);
    }

    @Nonnull
    @JsonProperty("projectId")
    public ProjectId getProjectId() {
        return projectId;
    }

    @JsonProperty("subject")
    public Optional<OWLEntity> getSubject() {
        return Optional.ofNullable(subject);
    }

    @JsonProperty("pageRequest")
    public PageRequest getPageRequest() {
        return pageRequest;
    }

    @JsonProperty("filter")
    public String getFilter() {
        return filter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetProjectChangesForHistoryViewAction that = (GetProjectChangesForHistoryViewAction) o;
        return Objects.equal(getProjectId(), that.getProjectId()) && Objects.equal(getSubject(), that.getSubject()) && Objects.equal(getPageRequest(), that.getPageRequest());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId(), getSubject(), getPageRequest());
    }
}

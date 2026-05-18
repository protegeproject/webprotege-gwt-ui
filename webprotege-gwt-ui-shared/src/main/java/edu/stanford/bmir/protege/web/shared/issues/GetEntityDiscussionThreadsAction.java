package edu.stanford.bmir.protege.web.shared.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@JsonTypeName("webprotege.discussions.GetEntityDiscussionThreads")
public class GetEntityDiscussionThreadsAction implements ProjectAction<GetEntityDiscussionThreadsResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    public GetEntityDiscussionThreadsAction(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
    }

    @JsonCreator
    public static GetEntityDiscussionThreadsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                          @JsonProperty("entity") @Nonnull OWLEntity entity) {
        return new GetEntityDiscussionThreadsAction(projectId, entity);
    }

    @GwtSerializationConstructor
    private GetEntityDiscussionThreadsAction() {
    }

    @JsonProperty("projectId")
    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @JsonProperty("entity")
    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetEntityDiscussionThreadsAction)) {
            return false;
        }
        GetEntityDiscussionThreadsAction other = (GetEntityDiscussionThreadsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity);
    }


    @Override
    public String toString() {
        return toStringHelper("GetCommentThreadsAction")
                .addValue(projectId)
                .addValue(entity)
                .toString();
    }
}

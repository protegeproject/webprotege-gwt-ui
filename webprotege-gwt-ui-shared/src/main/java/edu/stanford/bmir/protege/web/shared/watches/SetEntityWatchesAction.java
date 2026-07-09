package edu.stanford.bmir.protege.web.shared.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 *
 * Wire-conforms to the backend's webprotege.watches.SetWatches action (the
 * backend uses that name; there is no separate "SetEntityWatches" action on
 * that side).
 */
@JsonTypeName("webprotege.watches.SetWatches")
public class SetEntityWatchesAction implements ProjectAction<SetEntityWatchesResult> {

    private ChangeRequestId changeRequestId;

    private ProjectId projectId;

    private UserId userId;

    private OWLEntity entity;

    private ImmutableSet<Watch> watches;

    @GwtSerializationConstructor
    private SetEntityWatchesAction() {
    }

    private SetEntityWatchesAction(ChangeRequestId changeRequestId,
                                   ProjectId projectId,
                                   UserId userId,
                                   OWLEntity entity,
                                   ImmutableSet<Watch> watches) {
        this.changeRequestId = checkNotNull(changeRequestId);
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.entity = checkNotNull(entity);
        this.watches = checkNotNull(watches);
    }

    @JsonCreator
    public static SetEntityWatchesAction create(@JsonProperty("changeRequestId") ChangeRequestId changeRequestId,
                                                @JsonProperty("projectId") ProjectId projectId,
                                                @JsonProperty("userId") UserId userId,
                                                @JsonProperty("entity") OWLEntity entity,
                                                @JsonProperty("watches") ImmutableSet<Watch> watches) {
        return new SetEntityWatchesAction(changeRequestId, projectId, userId, entity, watches);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Nonnull
    @Override
    @JsonProperty("projectId")
    public ProjectId getProjectId() {
        return projectId;
    }

    @JsonProperty("userId")
    public UserId getUserId() {
        return userId;
    }

    @JsonProperty("entity")
    public OWLEntity getEntity() {
        return entity;
    }

    @JsonProperty("watches")
    public ImmutableSet<Watch> getWatches() {
        return watches;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(changeRequestId, projectId, userId, entity, watches);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetEntityWatchesAction)) {
            return false;
        }
        SetEntityWatchesAction other = (SetEntityWatchesAction) obj;
        return this.changeRequestId.equals(other.changeRequestId)
                && this.projectId.equals(other.projectId)
                && this.userId.equals(other.userId)
                && this.entity.equals(other.entity)
                && this.watches.equals(other.watches);
    }


    @Override
    public String toString() {
        return toStringHelper("SetEntityWatchesAction")
                .addValue(changeRequestId)
                .addValue(projectId)
                .addValue(userId)
                .addValue(entity)
                .addValue(watches)
                .toString();
    }
}

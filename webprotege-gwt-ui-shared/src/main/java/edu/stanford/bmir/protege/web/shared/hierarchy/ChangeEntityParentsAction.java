package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.bulkop.HasCommitMessage;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.ChangeEntityParents")
public abstract class ChangeEntityParentsAction implements ProjectAction<ChangeEntityParentsResult>, HasCommitMessage {

    @JsonCreator
    public static ChangeEntityParentsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                   @JsonProperty("parents") @Nonnull ImmutableSet<OWLClass> parents,
                                                   @JsonProperty("entity") @Nonnull OWLClass entity,
                                                    @JsonProperty("commitMessage") @Nonnull String commitMessage) {
        return new AutoValue_ChangeEntityParentsAction(projectId, parents, entity, commitMessage);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract ImmutableSet<? extends OWLEntity> getParents();

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    @Override
    public abstract String getCommitMessage();
}

package edu.stanford.bmir.protege.web.shared.bulkop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.bulkop.SetAnnotationValue")
public abstract class SetAnnotationValueAction implements ProjectAction<SetAnnotationValueResult>, HasCommitMessage {


    public static SetAnnotationValueAction create(@Nonnull ProjectId projectId,
                                                  @Nonnull ImmutableSet<OWLEntity> entities,
                                                  @Nonnull OWLAnnotationProperty property,
                                                  @Nonnull OWLAnnotationValue value,
                                                  @Nonnull String commitMessage) {
        return create(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, entities, property, value, commitMessage);
    }

    @JsonCreator
    public static SetAnnotationValueAction create(@JsonProperty("changeRequestId") @Nonnull ChangeRequestId changeRequestId,
                                                  @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                  @JsonProperty("entities") @Nonnull ImmutableSet<OWLEntity> entities,
                                                  @JsonProperty("property") @Nonnull OWLAnnotationProperty property,
                                                  @JsonProperty("value") @Nonnull OWLAnnotationValue value,
                                                  @JsonProperty("commitMessage") @Nonnull String commitMessage) {
        return new AutoValue_SetAnnotationValueAction(changeRequestId, projectId, entities, property, value, commitMessage);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public abstract ChangeRequestId getChangeRequestId();

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract ImmutableSet<OWLEntity> getEntities();

    @Nonnull
    public abstract OWLAnnotationProperty getProperty();

    @Nonnull
    public abstract OWLAnnotationValue getValue();

    @Nonnull
    @Override
    public abstract String getCommitMessage();
}

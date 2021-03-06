package edu.stanford.bmir.protege.web.shared.bulkop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

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


    @JsonCreator
    public static SetAnnotationValueAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                  @JsonProperty("entities") @Nonnull ImmutableSet<OWLEntity> entities,
                                                  @JsonProperty("property") @Nonnull OWLAnnotationProperty property,
                                                  @JsonProperty("value") @Nonnull OWLAnnotationValue value,
                                                  @JsonProperty("commitMessage") @Nonnull String commitMessage) {
        return new AutoValue_SetAnnotationValueAction(projectId, entities, property, value, commitMessage);
    }

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

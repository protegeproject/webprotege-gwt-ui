package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.ontologies.SetOntologyAnnotations")
public abstract class SetOntologyAnnotationsAction extends AbstractHasProjectAction<SetOntologyAnnotationsResult> {


    @GwtIncompatible
    public static SetOntologyAnnotationsAction create(ProjectId projectId,
                                                      OWLOntologyID ontologyID,
                                                      Set<PropertyAnnotationValue> fromAnnotations,
                                                      Set<PropertyAnnotationValue> toAnnotations) {
        return create(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, ontologyID, fromAnnotations, toAnnotations);
    }

    @JsonCreator
    public static SetOntologyAnnotationsAction create(@JsonProperty("changeRequestId") ChangeRequestId changeRequestId,
                                                      @JsonProperty("projectId") ProjectId projectId,
                                                      @JsonProperty("ontologyId") OWLOntologyID ontologyID,
                                                      @JsonProperty("fromAnnotations") Set<PropertyAnnotationValue> fromAnnotations,
                                                      @JsonProperty("toAnnotations") Set<PropertyAnnotationValue> toAnnotations) {
        return new AutoValue_SetOntologyAnnotationsAction(changeRequestId, projectId, ontologyID, fromAnnotations, toAnnotations);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public abstract ChangeRequestId getChangeRequestId();

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract OWLOntologyID getOntologyId();

    public abstract Set<PropertyAnnotationValue> getFromAnnotations();

    public abstract Set<PropertyAnnotationValue> getToAnnotations();
}

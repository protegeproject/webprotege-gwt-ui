package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.ontologies.SetOntologyAnnotations")
public abstract class SetOntologyAnnotationsResult implements Result {

    @JsonCreator
    public static SetOntologyAnnotationsResult create(@JsonProperty("ontologyAnnotations") Set<OWLAnnotation> ontologyAnnotations) {
        return new AutoValue_SetOntologyAnnotationsResult(ontologyAnnotations);
    }

    public abstract Set<OWLAnnotation> getOntologyAnnotations();
}

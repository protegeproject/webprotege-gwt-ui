package edu.stanford.bmir.protege.web.shared.ontology;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.event.OntologyChange;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.io.Serializable;

@JsonTypeName("RemoveOntologyAnnotation")
public class RemoveOntologyAnnotationChange implements OntologyChange, Serializable, IsSerializable {

    private OWLOntologyID ontologyId;
    private OWLAnnotation annotation;

    public RemoveOntologyAnnotationChange(@Nonnull OWLOntologyID ontologyId, @Nonnull OWLAnnotation annotation) {
        this.ontologyId = ontologyId;
        this.annotation = annotation;
    }

    public RemoveOntologyAnnotationChange() {
    }

    public void setOntologyId(OWLOntologyID ontologyId) {
        this.ontologyId = ontologyId;
    }

    public void setAnnotation(OWLAnnotation annotation) {
        this.annotation = annotation;
    }

    @Nonnull
    public OWLOntologyID getOntologyId() {
        return ontologyId;
    }

    @Nonnull
    @Override
    public OWLOntologyID ontologyId() {
        return getOntologyId();
    }

    @Nonnull
    public OWLAnnotation getAnnotation() {
        return annotation;
    }
}

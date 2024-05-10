package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gwt.user.client.rpc.IsSerializable;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.io.Serializable;

@JsonTypeName("AddOntologyAnnotation")
public class AddOntologyAnnotationChange implements OntologyChange, Serializable, IsSerializable {

    private OWLOntologyID ontologyId;
    private OWLAnnotation annotation;

    public AddOntologyAnnotationChange(OWLOntologyID owlOntologyID, OWLAnnotation owlAnnotation) {
        this.ontologyId = owlOntologyID;
        this.annotation = owlAnnotation;
    }

    public AddOntologyAnnotationChange() {
    }

    public void setOntologyId(OWLOntologyID ontologyId) {
        this.ontologyId = ontologyId;
    }

    public void setAnnotation(OWLAnnotation annotation) {
        this.annotation = annotation;
    }

    public OWLOntologyID getOntologyId() {
        return ontologyId;
    }

    @Nonnull
    @Override
    public OWLOntologyID ontologyId() {
        return getOntologyId();
    }

    public OWLAnnotation getAnnotation() {
        return annotation;
    }
}

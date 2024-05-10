package edu.stanford.bmir.protege.web.shared.ontology;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.event.OntologyChange;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.io.Serializable;

@JsonTypeName("RemoveAxiom")
public class RemoveAxiomChange implements OntologyChange, Serializable, IsSerializable {
    private OWLOntologyID ontologyId;
    private OWLAxiom owlAxiom;

    public RemoveAxiomChange(OWLOntologyID ontologyID, OWLAxiom owlAxiom) {
        this.ontologyId = ontologyID;
        this.owlAxiom = owlAxiom;
    }

    public RemoveAxiomChange() {
    }

    public void setOntologyId(OWLOntologyID ontologyId) {
        this.ontologyId = ontologyId;
    }

    public void setOwlAxiom(OWLAxiom owlAxiom) {
        this.owlAxiom = owlAxiom;
    }

    public OWLOntologyID getOntologyId() {
        return ontologyId;
    }

    @Nonnull
    @Override
    public OWLOntologyID ontologyId() {
        return getOntologyId();
    }

    public OWLAxiom getOwlAxiom() {
        return owlAxiom;
    }
}

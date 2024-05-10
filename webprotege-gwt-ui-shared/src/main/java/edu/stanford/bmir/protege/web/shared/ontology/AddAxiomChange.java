package edu.stanford.bmir.protege.web.shared.ontology;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.event.OntologyChange;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.io.Serializable;

@JsonTypeName("AddAxiom")
public class AddAxiomChange implements OntologyChange, Serializable, IsSerializable {

    private OWLOntologyID ontologyId;
    private OWLAxiom owlAxiom;

    public AddAxiomChange(OWLOntologyID ontologyID, OWLAxiom owlAxiom) {
        this.ontologyId = ontologyID;
        this.owlAxiom = owlAxiom;
    }

    public AddAxiomChange() {
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

    public void setOntologyId(OWLOntologyID ontologyId) {
        this.ontologyId = ontologyId;
    }

    public void setOwlAxiom(OWLAxiom owlAxiom) {
        this.owlAxiom = owlAxiom;
    }
}

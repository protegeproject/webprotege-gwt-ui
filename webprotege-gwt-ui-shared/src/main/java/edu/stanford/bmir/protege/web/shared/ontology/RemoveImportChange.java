package edu.stanford.bmir.protege.web.shared.ontology;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.event.OntologyChange;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.io.Serializable;

@JsonTypeName("RemoveImport")
public class RemoveImportChange implements OntologyChange, Serializable, IsSerializable {

    private OWLOntologyID ontologyId;
    private OWLImportsDeclaration importsDeclaration;

    public RemoveImportChange(OWLOntologyID ontologyId, OWLImportsDeclaration importsDeclaration) {
        this.ontologyId = ontologyId;
        this.importsDeclaration = importsDeclaration;
    }

    public RemoveImportChange() {
    }


    public void setOntologyId(OWLOntologyID ontologyId) {
        this.ontologyId = ontologyId;
    }

    public void setImportsDeclaration(OWLImportsDeclaration importsDeclaration) {
        this.importsDeclaration = importsDeclaration;
    }

    public OWLOntologyID getOntologyId() {
        return ontologyId;
    }

    @Nonnull
    @Override
    public OWLOntologyID ontologyId() {
        return getOntologyId();
    }

    public OWLImportsDeclaration getImportsDeclaration() {
        return importsDeclaration;
    }
}

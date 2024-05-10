package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.ontology.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.io.Serializable;

@JsonSubTypes({@JsonSubTypes.Type(AddAxiomChange.class), @JsonSubTypes.Type(RemoveAxiomChange.class), @JsonSubTypes.Type(AddOntologyAnnotationChange.class), @JsonSubTypes.Type(RemoveOntologyAnnotationChange.class), @JsonSubTypes.Type(AddImportChange.class), @JsonSubTypes.Type(RemoveImportChange.class)})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME
)
public interface OntologyChange extends Serializable, IsSerializable {
    @Nonnull
    @Deprecated
    default OWLOntologyID getOntologyId() {
        return this.ontologyId();
    }

    @Nonnull
    OWLOntologyID ontologyId();

}

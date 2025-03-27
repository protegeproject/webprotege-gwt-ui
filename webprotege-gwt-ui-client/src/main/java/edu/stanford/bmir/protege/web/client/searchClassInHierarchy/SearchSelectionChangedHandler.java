package edu.stanford.bmir.protege.web.client.searchClassInHierarchy;

import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Optional;

public interface SearchSelectionChangedHandler {
    void handleSelectionChanged(Optional<OWLEntity> owlEntity);
}

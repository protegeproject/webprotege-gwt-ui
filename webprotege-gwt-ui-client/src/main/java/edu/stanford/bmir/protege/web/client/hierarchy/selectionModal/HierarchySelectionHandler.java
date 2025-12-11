package edu.stanford.bmir.protege.web.client.hierarchy.selectionModal;

import edu.stanford.bmir.protege.web.shared.entity.EntityNode;

import java.util.Set;

public interface HierarchySelectionHandler {
    void handleSelection(Set<EntityNode> selection);
}

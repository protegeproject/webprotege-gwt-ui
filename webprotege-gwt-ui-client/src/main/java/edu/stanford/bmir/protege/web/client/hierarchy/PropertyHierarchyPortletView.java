package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 4 Dec 2017
 */
public interface PropertyHierarchyPortletView extends IsWidget {

    interface HierarchySelectedHandler {
        void handleHierarchyDescriptorSelected(@Nonnull HierarchyDescriptor hierarchyDescriptor);
    }

    void addHierarchy(@Nonnull HierarchyDescriptor hierarchyDescriptor,
                      @Nonnull String label,
                      @Nonnull TreeWidget<EntityNode, OWLEntity> view);

    void setHierarchyIdSelectedHandler(@Nonnull HierarchySelectedHandler hierarchySwitchedHandler);

    void setSelectedHierarchy(@Nonnull HierarchyDescriptor hierarchyDescriptor);

    Optional<HierarchyDescriptor> getSelectedHierarchyDescriptor();

    Optional<TreeWidget<EntityNode, OWLEntity>> getSelectedHierarchy();

}

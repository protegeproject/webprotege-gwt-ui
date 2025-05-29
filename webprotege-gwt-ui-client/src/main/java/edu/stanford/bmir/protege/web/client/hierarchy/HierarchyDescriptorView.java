package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.List;

public interface HierarchyDescriptorView extends IsWidget {

    void setEntityType(EntityType<?> entityType);

    EntityType<?> getEntityType();

    void setRoots(List<OWLEntityData> rootEntities);

    List<OWLEntityData> getRoots();
}

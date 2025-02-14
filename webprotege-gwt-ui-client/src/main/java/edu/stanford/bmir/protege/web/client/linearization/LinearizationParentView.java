package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.icd.AncestorClassHierarchy;

import java.util.List;
import java.util.Set;

public interface LinearizationParentView extends IsWidget, HasDispose {

    void setAncestorsTree(AncestorClassHierarchy ancestorTree);

    OWLEntityData getSelectedParent();
}

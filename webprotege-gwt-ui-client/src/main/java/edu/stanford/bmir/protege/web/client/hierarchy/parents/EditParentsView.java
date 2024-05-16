package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

public interface EditParentsView extends IsWidget, HasBusy {

    void setOwlEntityData(OWLEntityData entityData);
}

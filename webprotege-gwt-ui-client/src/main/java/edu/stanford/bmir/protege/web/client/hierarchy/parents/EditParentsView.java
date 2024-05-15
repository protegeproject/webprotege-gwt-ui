package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import org.semanticweb.owlapi.model.OWLEntity;

public interface EditParentsView extends IsWidget, HasBusy {
    void setOwlEntity(OWLEntity entity);
}

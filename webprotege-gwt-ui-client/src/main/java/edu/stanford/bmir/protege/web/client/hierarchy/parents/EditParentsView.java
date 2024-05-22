package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.Set;

public interface EditParentsView extends IsWidget, HasBusy {

    void setOwlEntityData(OWLEntityData entityData);

    void setEntityParents(Set<OWLEntityData> entityParents);

    boolean isReasonForChangeSet();

    void clear();

    @Nonnull
    String getEntityString();

    @Nonnull
    String getReasonForChange();
}

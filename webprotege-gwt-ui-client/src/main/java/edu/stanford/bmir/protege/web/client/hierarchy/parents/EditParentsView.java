package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public interface EditParentsView extends IsWidget, HasBusy {

    void setOwlEntityData(OWLEntityData entityData);

    void setEntityParents(Set<OWLEntityData> entityParents);

    void setParentsFromEquivalentClasses(Set<OWLEntityData> entityParents);

    void clear();

    @Nonnull
    String getReasonForChange();

    List<OWLPrimitiveData> getNewParentList();

    void clearClassesWithCycleErrors();

    void markClassesWithCycles(Set<OWLEntityData> classesWithCycles);

    void clearClassesWithRetiredParentsErrors();

    void markClassesWithRetiredParents(Set<OWLEntityData> classesWithRetiredParents);

    void clearLinearizationPathParentErrors();

    void clearReleasedChildrenError();

    void markLinearizationPathParent(String linearizationPathParents);

    void displayReleasedChildrenError(String entityName, String validationMessage);

    IsWidget getHelpTooltip();

    boolean isValid();

    void setLinearizationPathParents(Set<IRI> linearizationPathParents);
}

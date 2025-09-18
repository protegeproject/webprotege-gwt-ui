package edu.stanford.bmir.protege.web.client.directparents;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public interface DirectParentsListView extends IsWidget {

    void clearViews();
    void markParents(@Nonnull List<DirectParentView> directParentViews, Set<OWLEntityData> equivalentOnlyParents);
    void setMainParent(String parentIri);

    void markEquivalentOnlyParents();

}

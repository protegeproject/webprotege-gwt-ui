package edu.stanford.bmir.protege.web.client.directparents;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public interface DirectParentsListView extends IsWidget {

    void clearViews();

    void setDirectParentView(@Nonnull List<DirectParentView> directParentViews);

    void setMainParent(String parentIri);

    void setEquivalentOnlyParents(Set<OWLEntityData> equivalentOnlyParents);
}

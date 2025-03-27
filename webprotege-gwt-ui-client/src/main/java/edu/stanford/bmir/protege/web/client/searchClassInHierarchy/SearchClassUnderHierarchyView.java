package edu.stanford.bmir.protege.web.client.searchClassInHierarchy;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface SearchClassUnderHierarchyView extends IsWidget {
    @Nonnull
    Optional<OWLEntity> getSelected();

    void setCriteria(CompositeRootCriteria criteria);
    void setSelectionChangedHandler(SearchSelectionChangedHandler handler);
}

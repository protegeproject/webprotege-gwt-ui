package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public interface LogicalDefinitionModalView extends AcceptsOneWidget, IsWidget, HasDispose {

    void showTree(Set<OWLClass> roots, Consumer<EntityNode> mouseDownHandler);

    Optional<EntityNode> getSelectedEntity();
}

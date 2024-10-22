package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Set;

public interface LogicalDefinitionModalView extends AcceptsOneWidget, IsWidget, HasDispose {

    void showTree(Set<OWLClass> roots, LogicalDefinitionTableConfig.SelectedAxisValueHandler selectionHandler);
}

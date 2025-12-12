package edu.stanford.bmir.protege.web.client.hierarchy.selectionModal;

import edu.stanford.bmir.protege.web.client.hierarchy.*;
import edu.stanford.bmir.protege.web.client.searchClassInHierarchy.SearchClassUnderHierarchyPresenter;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

public class HierarchySelectionModalPresenter {

    private final HierarchySelectionModalView view;

    @Nonnull
    private final HierarchyPopupView hierarchyView;

    @Nonnull
    private final EntityHierarchyModel model;

    private Set<EntityNode> selectedEntities = new HashSet<>();

    private final SearchClassUnderHierarchyPresenter searchClassUnderHierarchyPresenter;


    @Inject
    public HierarchySelectionModalPresenter(HierarchySelectionModalView view,
                                            @Nonnull HierarchyPopupView hierarchyView,
                                            @Nonnull EntityHierarchyModel model,
                                            @Nonnull SearchClassUnderHierarchyPresenter searchClassUnderHierarchyPresenter) {
        this.view = view;
        this.hierarchyView = hierarchyView;
        this.model = model;
        this.searchClassUnderHierarchyPresenter = searchClassUnderHierarchyPresenter;
    }

    @Nonnull
    public HierarchySelectionModalView getView() {
        return view;
    }

    public void start(WebProtegeEventBus eventBus, ClassHierarchyDescriptor hierarchyDescriptor) {
        this.clean();
        model.start(eventBus,
                hierarchyDescriptor);
        hierarchyView.setModel(model);
        hierarchyView.clear();
        hierarchyView.setMultipleSelectionChangeHandler(
                entities -> selectedEntities = entities
        );
        hierarchyView.addCssClassToMain(WebProtegeClientBundle.BUNDLE.modal().treeWidth());
        this.view.getHierarchyContainer().setWidget(hierarchyView);
        this.searchClassUnderHierarchyPresenter.start(this.view.getEditorField());
        this.searchClassUnderHierarchyPresenter.setRoots(hierarchyDescriptor);
        this.searchClassUnderHierarchyPresenter.setSelectionChangedHandler((entity) ->
                entity.ifPresent(this.hierarchyView::revealEntity)
        );
    }

    public void revealEntity(OWLEntity selectedEntity) {
        this.hierarchyView.revealEntity(selectedEntity);
    }

    public Set<EntityNode> getSelection() {
        return selectedEntities;
    }

    public void clean() {
        this.selectedEntities = new HashSet<>();
        hierarchyView.clear();
        this.searchClassUnderHierarchyPresenter.clear();
    }
}

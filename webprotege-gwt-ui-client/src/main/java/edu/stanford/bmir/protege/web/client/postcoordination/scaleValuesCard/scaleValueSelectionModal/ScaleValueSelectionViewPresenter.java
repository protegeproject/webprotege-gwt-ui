package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.scaleValueSelectionModal;

import edu.stanford.bmir.protege.web.client.hierarchy.*;
import edu.stanford.bmir.protege.web.client.searchClassInHierarchy.SearchClassUnderHierarchyPresenter;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

public class ScaleValueSelectionViewPresenter {

    private ScaleValueSelectionView view;

    @Nonnull
    private final HierarchyPopupView hierarchyView;

    @Nonnull
    private final EntityHierarchyModel model;

    private Optional<EntityNode> selectedEntity = Optional.empty();

    private SearchClassUnderHierarchyPresenter searchClassUnderHierarchyPresenter;


    @Inject
    public ScaleValueSelectionViewPresenter(ScaleValueSelectionView view,
                                            @Nonnull HierarchyPopupView hierarchyView,
                                            @Nonnull EntityHierarchyModel model,
                                            @Nonnull SearchClassUnderHierarchyPresenter searchClassUnderHierarchyPresenter) {
        this.view = view;
        this.hierarchyView = hierarchyView;
        this.model = model;
        this.searchClassUnderHierarchyPresenter = searchClassUnderHierarchyPresenter;
    }

    @Nonnull
    public ScaleValueSelectionView getView() {
        return view;
    }

    public void start(WebProtegeEventBus eventBus, ClassHierarchyDescriptor hierarchyDescriptor) {
        this.clean();
        model.start(eventBus,
                hierarchyDescriptor);
        hierarchyView.setModel(model);
        hierarchyView.setSelectionChangedHandler(
                entityNode -> selectedEntity = Optional.of(entityNode)
        );
        this.view.getHierarchyContainer().setWidget(hierarchyView);
        this.searchClassUnderHierarchyPresenter.start(this.view.getEditorField());
        this.searchClassUnderHierarchyPresenter.setRoots(hierarchyDescriptor);
        this.searchClassUnderHierarchyPresenter.setSelectionChangedHandler((entity) -> {
            entity.ifPresent(owlENtity -> {
                this.hierarchyView.revealEntity(owlENtity);
            });
        });
    }

    public Optional<EntityNode> getSelection() {
        return selectedEntity;
    }

    private void clean() {
        this.selectedEntity = Optional.empty();
    }
}

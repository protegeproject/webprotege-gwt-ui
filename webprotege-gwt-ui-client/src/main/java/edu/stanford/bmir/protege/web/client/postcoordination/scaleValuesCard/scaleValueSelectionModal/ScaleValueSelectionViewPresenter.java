package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.scaleValueSelectionModal;

import edu.stanford.bmir.protege.web.client.hierarchy.*;
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


    @Inject
    public ScaleValueSelectionViewPresenter(ScaleValueSelectionView view,
                                            @Nonnull HierarchyPopupView hierarchyView,
                                            @Nonnull EntityHierarchyModel model) {
        this.view = view;
        this.hierarchyView = hierarchyView;
        this.model = model;
    }

    @Nonnull
    public ScaleValueSelectionView getView() {
        return view;
    }

    public void start(WebProtegeEventBus eventBus, HierarchyDescriptor hierarchyDescriptor) {
        this.clean();
        model.start(eventBus,
                hierarchyDescriptor);
        hierarchyView.setModel(model);
        hierarchyView.setSelectionChangedHandler(
                entityNode -> selectedEntity = Optional.of(entityNode)
        );
        this.view.getHierarchyContainer().setWidget(hierarchyView);

    }

    public Optional<EntityNode> getSelection() {
        return selectedEntity;
    }

    private void clean() {
        this.selectedEntity = Optional.empty();
    }
}

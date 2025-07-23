package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.selectionModal.HierarchySelectionModalManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.GetRenderedOwlEntitiesAction;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;

import java.util.*;
import java.util.stream.*;

public class ScaleValueCardPresenter {

    private ScaleValueCardView view;

    private PostCoordinationTableAxisLabel postCoordinationAxis;
    private PostcoordinationScaleValue scaleValue;
    private final DispatchServiceManager dispatchServiceManager;
    private final ProjectId projectId;

    private boolean isReadOnly = true;

    private WebProtegeEventBus eventBus;

    private Runnable handleChange;

    private final HierarchySelectionModalManager hierarchySelectionManager;

    public ScaleValueCardPresenter(DispatchServiceManager dispatchServiceManager,
                                   ProjectId projectId,
                                   HierarchySelectionModalManager hierarchySelectionManager) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        this.hierarchySelectionManager = hierarchySelectionManager;
    }

    public void setPostCoordinationAxis(PostCoordinationTableAxisLabel postCoordinationAxis) {
        this.postCoordinationAxis = postCoordinationAxis;
    }

    public void setHandleChange(Runnable runnable) {
        this.handleChange = runnable;
    }

    public void setScaleValue(PostcoordinationScaleValue scaleValue) {
        this.scaleValue = scaleValue;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    private void bindView() {
        view.setAddButtonClickHandler(event -> showModalForSelection());
    }

    private void initTable() {
        view.clearTable();
        view.addHeader(postCoordinationAxis.getScaleLabel(), ScaleAllowMultiValue.fromString(scaleValue.getGenericScale().getAllowMultiValue()));
        view.addSelectValueButton();
        view.setDeleteValueButtonHandler((value) -> {
            scaleValue.getValueIris().remove(value);
            handleChange.run();
        });

        Set<String> scaleValueIris = scaleValue.getValueIris()
                .stream()
                .flatMap(scaleValueIriAndName -> Stream.of(scaleValueIriAndName.getScaleValueIri()))
                .collect(Collectors.toSet());

        dispatchServiceManager.execute(GetRenderedOwlEntitiesAction.create(projectId, scaleValueIris),
                result -> {
                    result.getRenderedEntities()
                            .forEach(renderedEntity -> addRow(renderedEntity.getEntity().toStringID(), !renderedEntity.getBrowserText().equals("") ? renderedEntity.getBrowserText() : renderedEntity.getEntity().toStringID()));
                    view.setEditMode(!isReadOnly);
                }
        );


    }

    private void addRow(String iri, String value) {
        scaleValue.getValueIris().removeIf(existingValueIriAndName -> existingValueIriAndName.getScaleValueIri().equals(iri));

        ScaleValueIriAndName valueIriAndName = ScaleValueIriAndName.create(iri, value);
        scaleValue.getValueIris().add(valueIriAndName);

        view.addRow(valueIriAndName);
    }

    public ScaleValueCardView getView() {
        return view;
    }

    public PostcoordinationScaleValue getValues() {
        return scaleValue;
    }

    public void setEditMode(boolean editMode) {
        isReadOnly = !editMode;
        view.setEditMode(editMode);
    }

    public void start(WebProtegeEventBus eventBus, boolean isEditMode) {
        this.eventBus = eventBus;
        this.view = new ScaleValueCardViewImpl();
        bindView();
        initTable();
        setEditMode(isEditMode);
    }

    public void showModalForSelection() {
        String title = "Select Scale Value for " + this.scaleValue.getAxisLabel();
        Set<OWLClass> roots = new HashSet<>(Collections.singletonList(DataFactory.getOWLClass(IRI.create(scaleValue.getGenericScale().getGenericPostcoordinationScaleTopClass()))));
        hierarchySelectionManager.showModal(title, roots, (entityNode) -> {
            addRow(entityNode.getEntity().toStringID(), entityNode.getBrowserText());
            if (this.handleChange != null) {
                this.handleChange.run();
            }
        });
    }
}

package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.*;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.scaleValueSelectionModal.ScaleValueSelectionViewPresenter;
import edu.stanford.bmir.protege.web.shared.entity.GetRenderedOwlEntitiesAction;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.*;
import java.util.stream.*;

public class ScaleValueCardPresenter {

    private ScaleValueCardView view;

    private PostCoordinationTableAxisLabel postCoordinationAxis;
    private PostcoordinationScaleValue scaleValue;
    private final DispatchServiceManager dispatchServiceManager;
    private final ProjectId projectId;

    private boolean isReadOnly = true;

    private final ModalManager modalManager;

    private ScaleValueSelectionViewPresenter scaleValueSelectionPresenter;

    private WebProtegeEventBus eventBus;


    public ScaleValueCardPresenter(DispatchServiceManager dispatchServiceManager,
                                   ProjectId projectId,
                                   ModalManager modalManager) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        this.modalManager = modalManager;
    }

    public void setPostCoordinationAxis(PostCoordinationTableAxisLabel postCoordinationAxis) {
        this.postCoordinationAxis = postCoordinationAxis;
    }

    public void setScaleValue(PostcoordinationScaleValue scaleValue) {
        this.scaleValue = scaleValue;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    public void setScaleValueSelectionPresenter(ScaleValueSelectionViewPresenter scaleValueSelectionPresenter) {
        this.scaleValueSelectionPresenter = scaleValueSelectionPresenter;
    }

    /*
        ToDo:
            implement here the pop-up modal for selecting a scale value
     */
    private void bindView() {
        view.setAddButtonClickHandler(event -> showModalForSelection());
    }

    private void initTable() {
        view.clearTable();
        view.addHeader(postCoordinationAxis.getScaleLabel(), ScaleAllowMultiValue.fromString(scaleValue.getGenericScale().getAllowMultiValue()));
        view.addSelectValueButton();

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

        view.setDeleteValueButtonHandler((value) -> scaleValue.getValueIris().remove(value));

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
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle("Select Scale Value for " + this.scaleValue.getAxisLabel());
        modalPresenter.setView(scaleValueSelectionPresenter.getView());
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(DialogButton.SELECT);
        modalPresenter.setButtonHandler(DialogButton.SELECT, closer -> {
            closer.closeModal();
            selectChosenEntity();
        });
        Set<OWLClass> roots = new HashSet<>(Collections.singletonList(new OWLClassImpl(IRI.create(scaleValue.getGenericScale().getGenericPostcoordinationScaleTopClass()))));
        scaleValueSelectionPresenter.start(eventBus, ClassHierarchyDescriptor.get(roots));
        modalManager.showModal(modalPresenter);
    }

    private void selectChosenEntity() {
        scaleValueSelectionPresenter.getSelection().ifPresent(owlEntity -> addRow(owlEntity.getEntity().toStringID(), owlEntity.getBrowserText()));
    }
}

package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import com.google.gwt.user.client.ui.VerticalPanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.*;
import edu.stanford.bmir.protege.web.shared.entity.GetRenderedOwlEntitiesAction;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.HashSet;

public class ScaleValueCardPresenter {

    private final ScaleValueCardView view;
    private final PostCoordinationTableAxisLabel postCoordinationAxis;
    private final PostCoordinationScaleValue scaleValue;
    private final DispatchServiceManager dispatchServiceManager;
    private final ProjectId projectId;

    private boolean isReadOnly = true;

    private final ModalManager modalManager;


    public ScaleValueCardPresenter(PostCoordinationTableAxisLabel postCoordinationAxis,
                                   PostCoordinationScaleValue scaleValue,
                                   ScaleValueCardView view,
                                   DispatchServiceManager dispatchServiceManager,
                                   ProjectId projectId,
                                   ModalManager modalManager) {
        this.view = view;
        this.postCoordinationAxis = postCoordinationAxis;
        this.scaleValue = scaleValue;
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        this.modalManager = modalManager;
    }

    /*
        ToDo:
            implement here the pop-up modal for selecting a scale value
     */
    private void bindView() {
        view.setAddButtonClickHandler(event -> addRow("New Value"));
    }

    private void initTable() {
        view.clearTable();
        view.addHeader(postCoordinationAxis.getScaleLabel(), scaleValue.getGenericScale().getAllowMultiValue());
        view.addSelectValueButton();

        dispatchServiceManager.execute(GetRenderedOwlEntitiesAction.create(projectId, new HashSet<>(scaleValue.getValueIris())),
                result -> {
                    result.getRenderedEntities()
                            .forEach(renderedEntity -> addRow(!renderedEntity.getBrowserText().equals("") ? renderedEntity.getBrowserText() : renderedEntity.getEntity().toStringID()));
                    view.setEditMode(!isReadOnly);
                }
        );

        view.setDeleteValueButtonHandler((value) -> scaleValue.getValueIris().remove(value));

    }

    private void addRow(String value) {
        scaleValue.getValueIris().add(value);
        view.addRow(value);
    }

    public ScaleValueCardView getView() {
        return view;
    }

    public PostCoordinationScaleValue getValues() {
        return scaleValue;
    }

    public void setEditMode(boolean editMode) {
        isReadOnly = !editMode;
        view.setEditMode(editMode);
    }

    public void start(VerticalPanel panel, boolean isEditMode) {
        bindView();
        initTable();
        setEditMode(isEditMode);
        panel.add(view.asWidget());
    }

    public void showModalForSelection() {
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle("Select Scale Value for " + this.scaleValue.getGenericScale().getPostcoordinationAxis());
        modalPresenter.setView(searchPresenter.getView());
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(DialogButton.SELECT);
        modalPresenter.setButtonHandler(DialogButton.SELECT, closer -> {
            closer.closeModal();
            selectChosenEntity();
        });
        searchPresenter.setHierarchySelectionHandler(selection -> {
            selectionModel.setSelection(selection.getEntity());
            modalPresenter.closeModal();
        });
        searchPresenter.start();
        searchPresenter.setAcceptKeyHandler(modalPresenter::accept);
        searchPresenter.setSearchResultChosenHandler(result -> modalPresenter.accept());
        modalManager.showModal(modalPresenter);
    }
}

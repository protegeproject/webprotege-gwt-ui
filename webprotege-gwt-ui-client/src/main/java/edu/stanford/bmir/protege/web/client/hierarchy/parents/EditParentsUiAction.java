package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class EditParentsUiAction extends AbstractUiAction {

    @Nonnull
    private final EditParentsPresenter editParentsPresenter;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final SelectionModel selectionModel;

    @Nonnull
    private final ModalManager modalManager;

    @Inject
    protected EditParentsUiAction(@Nonnull EditParentsPresenter editParentsPresenter, @Nonnull Messages messages, @Nonnull SelectionModel selectionModel, @Nonnull ModalManager modalManager) {
        super(messages.hierarchy_editParents());
        this.editParentsPresenter = editParentsPresenter;
        this.messages = messages;
        this.selectionModel = selectionModel;
        this.modalManager = modalManager;
    }


    @Override
    public void execute() {
        selectionModel.getSelection().ifPresent(this::showDialog);
    }

    private void showDialog(OWLEntity entity) {
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(messages.hierarchy_editParents());
        modalPresenter.setView(editParentsPresenter.getView());
        editParentsPresenter.start(entity);
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(DialogButton.OK);
        modalPresenter.setButtonHandler(DialogButton.OK, closer -> {
            closer.closeModal();
        });
        modalManager.showModal(modalPresenter);
    }
}

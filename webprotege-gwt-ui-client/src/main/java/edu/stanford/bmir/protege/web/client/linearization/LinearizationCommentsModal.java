package edu.stanford.bmir.protege.web.client.linearization;

import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class LinearizationCommentsModal {

    Logger logger = java.util.logging.Logger.getLogger("LinearizationCommentsModal");


    private ModalManager modalManager;

    private LinearizationCommentsViewImpl view;

    @Inject
    public LinearizationCommentsModal(@Nonnull ModalManager modalManager, @Nonnull LinearizationCommentsViewImpl view) {
        this.modalManager = modalManager;
        this.view = view;
    }


    public void showModal(String initialBody, boolean readonly, @Nonnull Consumer<String> acceptBodyConsumer){
        ModalPresenter presenter = modalManager.createPresenter();
        view.setBody(initialBody, readonly);
        view.setReadonly(readonly);
        presenter.setView(view);
        if(readonly) {
            presenter.setPrimaryButton(DialogButton.OK);
            presenter.setTitle("Coding notes (read-only)");
            presenter.setButtonHandler(DialogButton.OK, ModalCloser::closeModal);
        }else {
            presenter.setEscapeButton(DialogButton.CANCEL);
            presenter.setPrimaryButton(DialogButton.OK);
            presenter.setTitle("Coding notes");
            presenter.setButtonHandler(DialogButton.OK, closer -> {
                closer.closeModal();
                String body = view.getBody();
                acceptBodyConsumer.accept(body);
            });
        }

        modalManager.showModal(presenter);
    }
}

package edu.stanford.bmir.protege.web.client.gh;

import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalButtonHandler;
import edu.stanford.bmir.protege.web.client.library.modal.ModalCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class LinkWithGitHubRepoModalPresenter {

    private final ModalManager modalManager;
    
    private final LinkWithGitHubRepoPresenter presenter;

    @Inject
    public LinkWithGitHubRepoModalPresenter(ModalManager modalManager, LinkWithGitHubRepoPresenter presenter) {
        this.modalManager = modalManager;
        this.presenter = presenter;
    }

    public void show() {
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle("Link with GitHub Repository");
        DialogButton linkRepositoryButton = DialogButton.get("Link repository" );
        modalPresenter.setPrimaryButton(linkRepositoryButton);
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButtonFocusedOnShow(false);
        modalPresenter.setButtonHandler(linkRepositoryButton, closer -> presenter.handleLinkRepository(closer::closeModal));
        presenter.start(modalPresenter::setView);
        modalManager.showModal(modalPresenter);
    }
}

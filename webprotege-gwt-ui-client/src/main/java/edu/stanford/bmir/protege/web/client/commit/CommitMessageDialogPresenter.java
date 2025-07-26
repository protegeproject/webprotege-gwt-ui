package edu.stanford.bmir.protege.web.client.commit;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;

import javax.inject.Inject;
import java.util.function.Consumer;

public class CommitMessageDialogPresenter {

    private final ModalManager manager;

    private final Messages messages;

    private CommitMessagePresenter commitMessagePresenter;

    @Inject
    public CommitMessageDialogPresenter(ModalManager manager, Messages messages, CommitMessagePresenter commitMessagePresenter) {
        this.manager = manager;
        this.messages = messages;
        this.commitMessagePresenter = commitMessagePresenter;
    }

    public void showCommitMessageDialog(Consumer<String> commitMessageConsumer,
                                        Runnable cancelRunnable) {
        ModalPresenter modalPresenter = manager.createPresenter();
        modalPresenter.setTitle(messages.editing_commitMessage_title());
        modalPresenter.setPrimaryButton(DialogButton.OK);
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setButtonHandler(DialogButton.OK, closer -> {
            String commitMessage = commitMessagePresenter.getCommitMessage();
            commitMessagePresenter.updateLocalHistory();
            commitMessageConsumer.accept(commitMessage);
            closer.closeModal();
        });
        modalPresenter.setButtonHandler(DialogButton.CANCEL, closer -> {
            closer.closeModal();
            cancelRunnable.run();
        });
        commitMessagePresenter.start(modalPresenter::setView);
        manager.showModal(modalPresenter);
    }
}

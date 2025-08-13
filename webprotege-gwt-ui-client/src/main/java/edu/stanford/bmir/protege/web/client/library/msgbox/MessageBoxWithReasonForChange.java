package edu.stanford.bmir.protege.web.client.library.msgbox;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.commit.CommitMessageLocalHistoryStorage;
import edu.stanford.bmir.protege.web.client.commit.CommitMessageView;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;


public class MessageBoxWithReasonForChange {

    private static final Void RETURN = null;

    private static final String DLG_TITLE = "";

    private static final String DEFAULT_SUB_MESSAGE = "";

    @Nonnull
    private final ModalManager modalManager;

    private final Messages messages;
    private final CommitMessageView commitMessageView;
    private final CommitMessageLocalHistoryStorage historyStorage;

    @Inject
    public MessageBoxWithReasonForChange(@Nonnull ModalManager modalManager, 
                                        Messages messages,
                                        CommitMessageView commitMessageView,
                                        CommitMessageLocalHistoryStorage historyStorage) {
        this.modalManager = checkNotNull(modalManager);
        this.messages = messages;
        this.commitMessageView = commitMessageView;
        this.historyStorage = historyStorage;
    }

    private MessageBoxWithReasonForChangeView createMessageBox(MessageStyle messageStyle, String mainMessage, String subMessage) {
        final MessageBoxWithReasonForChangeView messageBoxView = new MessageBoxWithReasonForChangeView(messages, commitMessageView, historyStorage);
        messageBoxView.setMainMessage(mainMessage);
        messageBoxView.setSubMessage(subMessage);
        messageBoxView.setMessageStyle(messageStyle);
        return messageBoxView;
    }

    public void showConfirmBoxWithReasonForChange(String mainMessage, String subMessage, DialogButton acceptButton,
                                                  DialogButton cancelButton, ReasonForChangeHandler acceptHandler) {
        MessageBoxWithReasonForChangeView view = createMessageBox(MessageStyle.QUESTION, mainMessage, subMessage);

        ModalPresenter presenter = modalManager.createPresenter();
        presenter.setView(view);
        presenter.setPrimaryButton(acceptButton);
        presenter.setEscapeButton(cancelButton);

        presenter.setButtonHandler(acceptButton, closer -> {
            if (view.isReasonForChangeSet()) {
                view.updateLocalHistory();
                closer.closeModal();
                acceptHandler.handle(view.getReasonForChangeString());
            }
        });

        presenter.setButtonHandler(cancelButton, ModalCloser::closeModal);

        modalManager.showModal(presenter);
    }

    public void showConfirmBoxWithReasonForChange(String mainMessage, String subMessage,
                               DialogButton escapeButton, Runnable escapeHandler, DialogButton acceptButton,
                               ReasonForChangeHandler acceptHandler) {
        MessageBoxWithReasonForChangeView view = createMessageBox(MessageStyle.QUESTION, mainMessage, subMessage);
        ModalPresenter presenter = modalManager.createPresenter();
        presenter.setView(view);
        presenter.setPrimaryButton(acceptButton);
        presenter.setEscapeButton(escapeButton);
        presenter.setPrimaryButtonFocusedOnShow(true);
        presenter.setButtonHandler(escapeButton, closer -> {
            closer.closeModal();
            escapeHandler.run();
        });
        presenter.setButtonHandler(acceptButton, closer -> {
            if (view.isReasonForChangeSet()) {
                view.updateLocalHistory();
                closer.closeModal();
                acceptHandler.handle(view.getReasonForChangeString());
            }
        });
        modalManager.showModal(presenter);
    }
}

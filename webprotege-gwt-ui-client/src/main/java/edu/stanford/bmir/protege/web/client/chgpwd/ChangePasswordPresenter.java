package edu.stanford.bmir.protege.web.client.chgpwd;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2013
 */
public class ChangePasswordPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private final MessageBox messageBox;

    private final ChangePasswordView changePasswordView;

    private final UserId userId;

    private final DispatchErrorMessageDisplay errorDisplay;

    private final ProgressDisplay progressDisplay;

    private final ModalManager modalManager;

    private final Messages messages;

    @AutoFactory
    @Inject
    public ChangePasswordPresenter(@Provided ChangePasswordView changePasswordView,
                                   UserId userId,
                                   @Provided DispatchServiceManager dispatchServiceManager,
                                   @Provided MessageBox messageBox,
                                   @Provided DispatchErrorMessageDisplay errorDisplay,
                                   @Provided ProgressDisplay progressDisplay,
                                   @Provided ModalManager modalManager,
                                   @Provided Messages messages) {
        this.changePasswordView = changePasswordView;
        this.userId = checkNotNull(userId);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.messageBox = checkNotNull(messageBox);
        this.errorDisplay = errorDisplay;
        this.progressDisplay = progressDisplay;
        this.modalManager = modalManager;
        this.messages = messages;
    }

    public void changePassword() {
        if (userId.isGuest()) {
            messageBox.showAlert("The password of the guest user cannot be changed");
            return;
        }
        showDialog();
    }

    private void showDialog() {
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(messages.changePassword());
        modalPresenter.setView(changePasswordView);
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(DialogButton.OK);
        modalPresenter.setButtonHandler(DialogButton.OK, closer -> {
            if (changePasswordView.getNewPassword().isEmpty()) {
                messageBox.showAlert(messages.password_change_specifyNewPassword());
            } else if (!isPasswordConfirmationCorrect()) {
                handleIncorrectPasswordConfirmation();
            } else {
                executeChangePassword(closer);
            }
        });
        modalManager.showModal(modalPresenter);
    }

    private boolean isPasswordConfirmationCorrect() {
        String newPassword = changePasswordView.getNewPassword();
        String newPasswordConfirmation = changePasswordView.getNewPasswordConfirmation();
        return newPassword.equals(newPasswordConfirmation);
    }


    private void handleIncorrectPasswordConfirmation() {
        messageBox.showAlert(messages.password_change_passwordsDoNotMatch_Title(),
                             messages.password_change_passwordsDoNotMatch_Body());
    }

    private void handleIncorrectCurrentPassword() {
        messageBox.showAlert(messages.password_change_currentPasswordIncorrect_Title(),
                             messages.password_change_currentPasswordIncorrect_Body());
    }


    private void executeChangePassword(final ModalCloser closer) {
        Password currentPassword = Password.create(changePasswordView.getOldPassword());
        Password newPassword = Password.create(changePasswordView.getNewPassword());
        dispatchServiceManager.execute(ChangePasswordAction.create(userId, currentPassword, newPassword),
                                       new DispatchServiceCallbackWithProgressDisplay<ChangePasswordResult>(errorDisplay,
                                                                                                            progressDisplay) {
                                           @Override
                                           public String getProgressDisplayTitle() {
                                               return "Changing password";
                                           }

                                           @Override
                                           public String getProgressDisplayMessage() {
                                               return "Please wait.";
                                           }

                                           @Override
                                           public void handleSuccess(ChangePasswordResult changePasswordResult) {
                                               AuthenticationResponse response = changePasswordResult.getResponse();
                                               if(response == AuthenticationResponse.SUCCESS) {
                                                   messageBox.showMessage(messages.password_change_passwordChanged_Title(),
                                                                          messages.password_change_passwordChanged_Body(),
                                                                          closer::closeModal);
                                               }
                                               else {
                                                   handleIncorrectCurrentPassword();
                                               }
                                           }
                                       });
    }
}

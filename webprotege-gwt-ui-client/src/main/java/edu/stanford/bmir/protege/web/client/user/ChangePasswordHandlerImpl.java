package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.chgpwd.ChangePasswordPresenter;
import edu.stanford.bmir.protege.web.client.chgpwd.ChangePasswordPresenterFactory;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class ChangePasswordHandlerImpl implements ChangePasswordHandler {

    private final MessageBox messageBox;

    @Inject
    public ChangePasswordHandlerImpl(MessageBox messageBox) {
        this.messageBox = messageBox;
    }

    @Override
    public void handleChangePassword() {
        messageBox.showConfirmBox("Reset password?", "Are you sure you want to reset your password?", DialogButton.CANCEL, DialogButton.get("Reset passord"), () -> {
            String resetPasswordUrl = "/keycloak/realms/webprotege/login-actions/reset-credentials?client_id=webprotege";
            Window.Location.assign(resetPasswordUrl);
        }, DialogButton.CANCEL);


    }
}

package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetAuthenticatedUserDetailsAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetAuthenticatedUserDetailsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22 Dec 2017
 */
public class ReadLoggedInUserInitializationTask implements ApplicationInitManager.ApplicationInitializationTask {
    Logger logger = Logger.getLogger("ReadLoggedInUserInitializationTask");

    @Nonnull
    private final LoggedInUserManager loggedInUserManager;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final DispatchErrorMessageDisplay errorMessageDisplay;

    @Inject
    public ReadLoggedInUserInitializationTask(@Nonnull LoggedInUserManager loggedInUserManager,
                                              @Nonnull DispatchServiceManager dispatch,
                                              @Nonnull DispatchErrorMessageDisplay errorMessageDisplay) {
        this.loggedInUserManager = loggedInUserManager;
        this.dispatch = dispatch;
        this.errorMessageDisplay = errorMessageDisplay;
    }

    @Override
    public void run(ApplicationInitManager.ApplicationInitTaskCallback callback) {
        dispatch.execute(GetAuthenticatedUserDetailsAction.create(),
                         new DispatchServiceCallback<GetAuthenticatedUserDetailsResult>(errorMessageDisplay) {
                             @Override
                             public void handleSuccess(GetAuthenticatedUserDetailsResult result) {
                                 logger.log(Level.FINE, "[ReadLoggedInUserInitializationTask] set logged user: " + result);

                                 loggedInUserManager.setLoggedInUser(new UserInSession(result.getUserDetails(),
                                                                                       result.getPermittedActions()));
                                 callback.taskComplete();
                             }

                             @Override
                             public void handleExecutionException(Throwable cause) {
                                 callback.taskFailed(cause);
//                                 GWT.log("Task failed");
//                                 callback.taskComplete();
                             }
                         });
    }

    @Override
    public String getName() {
        return "Init Logged In User";
    }
}

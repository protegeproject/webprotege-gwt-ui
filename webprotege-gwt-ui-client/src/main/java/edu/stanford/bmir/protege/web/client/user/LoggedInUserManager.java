package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.app.ApplicationEnvironmentManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
@ApplicationSingleton
public class LoggedInUserManager {

    @Nonnull
    private final LoggedInUser loggedInUser;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ApplicationEnvironmentManager applicationEnvironmentManager;

    @Inject
    public LoggedInUserManager(@Nonnull LoggedInUser loggedInUser,
                               @Nonnull DispatchServiceManager dispatchServiceManager,
                               @Nonnull DispatchErrorMessageDisplay errorDisplay,
                               @Nonnull ApplicationEnvironmentManager applicationEnvironmentManager) {
        this.loggedInUser = loggedInUser;
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.errorDisplay = checkNotNull(errorDisplay);
        this.applicationEnvironmentManager = applicationEnvironmentManager;
    }

    /**
     * Gets the id of the currently logged in user.
     * @return The id of the currently logged in user.  Not {@code null}.  The returned id may correspond to the id
     * of the guest user.
     */
    @Nonnull
    public UserId getLoggedInUserId() {
        return loggedInUser.getCurrentUserId();
    }

    /**
     * Sets the logged in user.  An event will be fired (asynchronously) to indicate whether the user has logged in or out.
     * @param userInSession The user in the session that is the logged in user.
     * @throws NullPointerException if {@code userId} is {@code null}.
     */
    public void setLoggedInUser(@Nonnull final UserInSession userInSession) {
        loggedInUser.setLoggedInUser(userInSession);
    }

    /**
     * If the current user is not the guest user calling this method will log out the current user.  An event will
     * be fired (asynchronously) when the current user has been logged out.
     */
    public void logOutCurrentUser() {
        if(loggedInUser.getCurrentUserId().isGuest()) {
            return;
        }

     RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
                applicationEnvironmentManager.getAppEnvVariables().getLogoutUrl());
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        // Process the response
                        Window.Location.assign(applicationEnvironmentManager.getAppEnvVariables().getRedirectAfterLogoutUrl());

                    } else {
                        // Handle the error
                        Window.alert("Failed to get response: " + response.getStatusText());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    // Handle the error
                    Window.alert("Request error: " + exception.getMessage());
                }
            });
        } catch (RequestException e) {
            // Handle the exception
            Window.alert("Request exception: " + e.getMessage());
        }
    }

    public Set<ActionId> getLoggedInUserApplicationActions() {
        return loggedInUser.getUserInSession().getAllowedApplicationActions();
    }

    public boolean isAllowedApplicationAction(ActionId actionId) {
        return loggedInUser.isAllowedApplicationAction(actionId);
    }

    public boolean isAllowedApplicationAction(BuiltInAction action) {
        return isAllowedApplicationAction(action.getActionId());
    }

//    private void restoreUserFromServerSideSession(final Optional<AsyncCallback<UserDetails>> callback) {
//        dispatchServiceManager.execute(GetAuthenticatedUserDetailsAction.create(), new DispatchServiceCallback<GetAuthenticatedUserDetailsResult>(errorDisplay) {
//            @Override
//            public void handleExecutionException(Throwable cause) {
//                callback.ifPresent(userDetailsAsyncCallback -> userDetailsAsyncCallback.onFailure(cause));
//            }
//
//            @Override
//            public void handleSuccess(GetAuthenticatedUserDetailsResult result) {
//                loggedInUser.setLoggedInUser(result.getUserInSession());
//                callback.ifPresent(userDetailsAsyncCallback -> userDetailsAsyncCallback.onSuccess(result.getUserInSession().getUserDetails()));
//            }
//
//        });
//    }
}

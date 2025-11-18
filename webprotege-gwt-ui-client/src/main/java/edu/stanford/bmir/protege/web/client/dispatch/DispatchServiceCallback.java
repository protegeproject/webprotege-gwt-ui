package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.http.client.URL;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.web.bindery.event.shared.UmbrellaException;
import edu.stanford.bmir.protege.web.client.app.FragmentManager;
import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectUnderMaintenanceException;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/02/15
 */
public class DispatchServiceCallback<T> {

    private static final Logger logger = Logger.getLogger(DispatchServiceCallback.class.getName());

    private DispatchErrorMessageDisplay errorMessageDisplay;

    public DispatchServiceCallback(DispatchErrorMessageDisplay errorMessageDisplay) {
        this.errorMessageDisplay = errorMessageDisplay;
    }

    /**
     * Called when the action is submitted for execution.
     * This can be used to display a progress indicator to the user, for example.
     */
    public void handleSubmittedForExecution() {

    }

    public final void onFailure(Throwable t) {
        Throwable throwable;
        if(t instanceof ActionExecutionException && ((ActionExecutionException) t).getWrappedException() != null) {
            throwable = ((ActionExecutionException) t).getWrappedException();
        } else {
            throwable = t;
        }

        if(throwable instanceof StatusCodeException) {
            _handleStatusCodeException((StatusCodeException) throwable);
        } else if (throwable instanceof PermissionDeniedException) {
            handlePermissionDeniedException((PermissionDeniedException) throwable);
        } else if (throwable instanceof ProjectUnderMaintenanceException) {
            handleProjectUnderMaintenanceException((ProjectUnderMaintenanceException) throwable);
        } else if (throwable instanceof IncompatibleRemoteServiceException) {
            _handleIncompatibleRemoteServiceException((IncompatibleRemoteServiceException) throwable);
        } else if (throwable instanceof InvocationException) {
             _handleInvocationException((InvocationException) throwable);
        } else if (throwable instanceof ActionExecutionException) {
             handleExecutionException(throwable);
        } else if (throwable instanceof UmbrellaException) {
            _handleUmbrellaException((UmbrellaException) throwable);
        } else {
            // Should we actually get here?  I don't think so.
            displayAndLogError(throwable);
        }
        handleErrorFinally(throwable);
        handleFinally();
    }

    private void handleProjectUnderMaintenanceException(ProjectUnderMaintenanceException projectUnderMaintenanceException) {

        errorMessageDisplay.displayGeneralErrorMessage("Project under maintenance", "This project is currently unavailable due to being under maintenance. Please try again in 5 minutes");}

    private void _handleStatusCodeException(StatusCodeException exception) {
        if(exception.getStatusCode() == 504) {
            errorMessageDisplay.displayGeneralErrorMessage("Timeout", "Something took too long to respond. A service might be busy or temporarily unavailable. Please try again in a moment.");
        }
        else {
            errorMessageDisplay.displayGeneralErrorMessage("Error", "An error occurred: " + exception.getStatusText() + " (Error code: " + exception.getStatusCode() + ")");
        }
    }

    public final void onSuccess(T t) {
        try {
            handleSuccess(t);
        } finally {
            handleFinally();
        }
    }

    /**
     * Handles success.
     *
     * @param t The return value from the call.
     */
    public void handleSuccess(T t) {

    }


    /**
     * Called after some kind of error has occurred.  This can be used to clean up after all errors.
     *
     * @param throwable The error that occurred.
     */
    public void handleErrorFinally(Throwable throwable) {

    }

    /**
     * Called after either {@link #handleSuccess(Object)} or {@link #handleErrorFinally(Throwable)}.  This can be used
     * to perform clean up that should take place whether the call succeeded or failed.  It is like the "finally"
     * block in a "try-catch-finally" statement.
     */
    public void handleFinally() {
    }

    public void handleExecutionException(Throwable cause) {
        displayAndLogError(cause);
    }

    public void handlePermissionDeniedException(PermissionDeniedException e) {
        // Only display the permission denied message if the user is not the guest user.  This
        // is because when a guest user gets a permission denied error message they are first
        // redirected to the login in page
        if (!e.getUserId().isGuest()) {
            if(e.getMessage() != null) {
                errorMessageDisplay.displayPermissionDeniedErrorMessage(e.getMessage());
            }
            else {
                errorMessageDisplay.displayPermissionDeniedErrorMessage();
            }
        }
    }

    private void displayAndLogError(Throwable throwable) {
        errorMessageDisplay.displayGeneralErrorMessage(getErrorMessageTitle(), getErrorMessage(throwable));
        logger.log(Level.SEVERE, getErrorMessage(throwable), throwable);
    }

    private void _handleIncompatibleRemoteServiceException(IncompatibleRemoteServiceException e) {
        errorMessageDisplay.displayIncompatibleRemoteServiceExceptionErrorMessage();
    }

    private void _handleInvocationException(InvocationException e) {
        String message = e.getMessage();
        if (isKeycloakLoginPage(message)) {
            // Likely the Keycloak login page was returned instead of a proper RPC response
            handleExpiredSession();
        }
        else {
            errorMessageDisplay.displayInvocationExceptionErrorMessage(e);
        }
    }
    
    /**
     * Checks to see if it is *likely* that the message (of an invocation exception) contains
     * the keycloak login page.
     * @param message The message
     */
    private static boolean isKeycloakLoginPage(String message) {
        return message != null && message.contains("<html" ) && message.contains("login" );
    }

    private static void handleExpiredSession() {
        FragmentManager.storeCurrentWindowLocationFragment();
        // Go to a protected page.  This will cause the authorization flow to be triggered by the
        // Tomcat keycloak valve
        String protocol = Window.Location.getProtocol();
        String host = Window.Location.getHost();
        Window.Location.assign(protocol + "//" + host);
    }

    private void _handleUmbrellaException(UmbrellaException e) {
        for (Throwable cause : e.getCauses()) {
            onFailure(cause);
        }
    }

    protected String getErrorMessageTitle() {
        return "Error";
    }

    protected String getErrorMessage(Throwable throwable) {
        return "[" + throwable.getClass().getSimpleName() + "] " + throwable.getMessage();
    }


}

package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.user.client.rpc.InvocationException;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class DispatchServiceCallback_TestCase<T> {

    private DispatchErrorMessageDisplay messageDisplay;

    private DispatchServiceCallback<T> callback;

    @Mock
    private T value;

    @Before
    public void setUp() throws Exception {
        messageDisplay = mock(DispatchErrorMessageDisplay.class);
        callback = spy(new DispatchServiceCallback<T>(messageDisplay) {});
    }

    @Test
    public void shouldCall_handleSuccess() {
        callback.onSuccess(value);
        verify(callback, times(1)).handleSuccess(value);
    }

    @Test
    public void shouldCall_handleExecutionException() {
        ActionExecutionException actionExecutionException = mock(ActionExecutionException.class);
        Throwable cause = mock(Throwable.class);
        when(actionExecutionException.getCause()).thenReturn(cause);
        callback.onFailure(actionExecutionException);
        verify(callback, times(1)).handleExecutionException(cause);
        verify(messageDisplay, times(1)).displayGeneralErrorMessage(anyString(), anyString());
    }

    @Test
    public void shouldCall_handlePermissionDeniedException() {
        PermissionDeniedException exception = mock(PermissionDeniedException.class);
        UserInSession userInSession = mock(UserInSession.class);
        when(userInSession.isGuest()).thenReturn(false);
        callback.onFailure(exception);
        verify(callback, times(1)).handlePermissionDeniedException(exception);
        verify(messageDisplay, times(1)).displayPermissionDeniedErrorMessage();
    }

    @Test
    public void shouldDisplayInvocationExceptionMessage() {
        InvocationException exception = mock(InvocationException.class);
        callback.onFailure(exception);
        verify(messageDisplay, times(1)).displayInvocationExceptionErrorMessage(exception);
    }

    @Test
    public void shouldDealWithOtherKindOfException() {
        Exception exception = mock(Exception.class);
        callback.onFailure(exception);
        verify(messageDisplay, times(1)).displayGeneralErrorMessage(anyString(), anyString());
    }


    @Test
    public void shouldCallErrorFinallyOnError() {
        Exception ex = mock(Exception.class);
        callback.onFailure(ex);
        verify(callback, times(1)).handleErrorFinally(ex);
    }

    @Test
    public void shouldCallFinallyOnError() {
        callback.onFailure(mock(Exception.class));
        verify(callback, times(1)).handleFinally();
    }

    @Test
    public void shouldCallFinallyOnSuccess() {
        callback.onSuccess(value);
        verify(callback, times(1)).handleFinally();
    }

    @Test
    public void shouldNotCallErrorFinallyOnSuccess() {
        callback.onSuccess(value);
        verify(callback, never()).handleErrorFinally(any(Exception.class));
    }

}

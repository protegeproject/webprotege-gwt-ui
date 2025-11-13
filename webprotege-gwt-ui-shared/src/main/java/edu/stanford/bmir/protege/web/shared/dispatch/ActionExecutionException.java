package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.gwt.user.client.rpc.IsSerializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/02/2013
 * <p>
 *     An exception which indicates an exception occurred whilst executing an action.  The cause of this exception
 *     is the exception thrown during action execution.
 * </p>
 */
public class ActionExecutionException extends RuntimeException implements IsSerializable {

    private Exception wrappedException;


    public ActionExecutionException(Exception wrappedException) {
        this.wrappedException = wrappedException;
    }

    public Exception getWrappedException() {
        return wrappedException;
    }

    /**
     * For serialization purposes only
     */
    private ActionExecutionException() {
    }

    /**
     * Constructs an {@link ActionExecutionException} that wraps the specified exception which occurred whilst executing
     * an action.
     * @param cause The cause.  Not {@code null}.
     */
    public ActionExecutionException(String message) {
        super(message);
    }
}

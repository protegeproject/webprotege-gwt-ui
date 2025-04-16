package edu.stanford.bmir.protege.web.client.permissions;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BasicCapability;
import edu.stanford.bmir.protege.web.shared.access.BuiltInCapability;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 */
public interface LoggedInUserProjectCapabilityChecker {

    /**
     * Checks that the signed in user possesses the specified capability
     * on the current active project.  The current active project is defined by the
     * {@link ActiveProjectManager}.
     * @param basicCapability The {@link BasicCapability} to test for.
     * @param callback A callback for receiving the result of the query.
     */
    void hasCapability(@Nonnull BasicCapability basicCapability,
                       @Nonnull DispatchServiceCallback<Boolean> callback);

    /**
     * Checks that the signed in user possesses the specified capability
     * on the current active project.  The current active project is defined by the
     * {@link ActiveProjectManager}.
     * @param basicCapability The {@link BasicCapability} to test for execute permission.
     * @param callback A callback for receiving the result of the query.
     */
    void hasCapability(@Nonnull BasicCapability basicCapability,
                       @Nonnull Consumer<Boolean> callback);

    /**
     * Checks that the signed in user possesses the specified capability
     * on the current active project.  The current active project is defined by the
     * {@link ActiveProjectManager}.
     * @param builtInCapability The {@link BuiltInCapability} to test for execute permission.  This
     *                      is a convenience function that calls the function with the
     *                      {@link BasicCapability}.
     * @param callback A callback for receiving the result of the query.
     */
    void hasCapability(@Nonnull BuiltInCapability builtInCapability,
                       @Nonnull Consumer<Boolean> callback);
}

package edu.stanford.bmir.protege.web.client.permissions;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 */
public interface LoggedInUserProjectPermissionChecker {

    /**
     * Checks that the logged in user has permission to execute the specified
     * action on the current active project.  The current active project is defined by the
     * {@link ActiveProjectManager}.
     * @param actionId The {@link ActionId} to test for execute permission.
     * @param callback A callback for receiving the result of the query.
     */
    void hasPermission(@Nonnull ActionId actionId,
                       @Nonnull DispatchServiceCallback<Boolean> callback);

    default void hasPermissions(@Nonnull Collection<ActionId> actionIds,
                                @Nonnull Consumer<Boolean> callback) {
        // A running lis of everything that needs to be checked
        Set<ActionId> toCheck = new HashSet<>(actionIds);
        // A record of any failures that accumulate as we go
        Set<ActionId> failed = new HashSet<>();
        for(ActionId actionId : actionIds) {
            hasPermission(actionId, b -> {
                // One more action has been checked
                toCheck.remove(actionId);
                if(!b) {
                    // If it failed add it to the list of failed
                    failed.add(actionId);
                    // Early termination on failure
                    callback.accept(false);
                }
                if(toCheck.isEmpty() && failed.isEmpty()) {
                    // Done checking everything and everything passed
                    // Note: We do not need to tell the callback about failure because this is
                    // handled by early termination above.
                    callback.accept(true);
                }
            });
        }
    }

    /**
     * Checks that the logged in user has permission to execute the specified
     * action on the current active project.  The current active project is defined by the
     * {@link ActiveProjectManager}.
     * @param actionId The {@link ActionId} to test for execute permission.
     * @param callback A callback for receiving the result of the query.
     */
    void hasPermission(@Nonnull ActionId actionId,
                       @Nonnull Consumer<Boolean> callback);

    /**
     * Checks that the logged in user has the specified permission to execute the specified
     * action on the current active project.  The current active project is defined by the
     * {@link ActiveProjectManager}.
     * @param builtInAction The {@link BuiltInAction} to test for execute permission.  This
     *                      is a convenience function that calls the function with the
     *                      {@link ActionId}.
     * @param callback A callback for receiving the result of the query.
     */
    void hasPermission(@Nonnull BuiltInAction builtInAction,
                       @Nonnull Consumer<Boolean> callback);
}

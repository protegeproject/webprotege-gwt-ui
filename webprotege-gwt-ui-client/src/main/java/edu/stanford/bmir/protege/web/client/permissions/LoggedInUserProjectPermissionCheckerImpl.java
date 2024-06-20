package edu.stanford.bmir.protege.web.client.permissions;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/01/16
 */
public class LoggedInUserProjectPermissionCheckerImpl implements LoggedInUserProjectPermissionChecker {

    private final Logger logger = Logger.getLogger(LoggedInUserProjectPermissionCheckerImpl.class.getName());

    private final LoggedInUserProvider loggedInUserProvider;

    private final ActiveProjectManager activeProjectManager;

    private final PermissionManager permissionManager;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Inject
    public LoggedInUserProjectPermissionCheckerImpl(@Nonnull LoggedInUserProvider loggedInUserProvider,
                                                    @Nonnull ActiveProjectManager activeProjectManager,
                                                    @Nonnull PermissionManager permissionManager, @Nonnull DispatchErrorMessageDisplay errorDisplay) {
        this.loggedInUserProvider = checkNotNull(loggedInUserProvider);
        this.activeProjectManager = checkNotNull(activeProjectManager);
        this.permissionManager = checkNotNull(permissionManager);
        this.errorDisplay = checkNotNull(errorDisplay);
    }

    @Override
    public void hasPermission(@Nonnull ActionId actionId,
                              @Nonnull DispatchServiceCallback<Boolean> callback) {
        Optional<ProjectId> projectId = activeProjectManager.getActiveProjectId();
        if (!projectId.isPresent()) {
            callback.onSuccess(false);
            return;
        }
        UserId userId = loggedInUserProvider.getCurrentUserId();
        logger.fine("[LoggedInUserProjectPermissionCheckerImpl] Checking permissions for: " + userId + " on " + projectId.get());
        permissionManager.hasPermissionForProject(userId, actionId, projectId.get(), callback);
    }

    @Override
    public void hasPermission(@Nonnull ActionId action,
                              @Nonnull Consumer<Boolean> callback) {
        hasPermission(action, new DispatchServiceCallback<Boolean>(errorDisplay) {
            @Override
            public void handleSuccess(Boolean hasPermission) {
                callback.accept(hasPermission);
            }

            @Override
            public void handleErrorFinally(Throwable throwable) {
                logger.info("[LoggedInUserProjectPermissionCheckerImpl] Error when checking permissions: " + throwable.getMessage());
            }
        });
    }

    @Override
    public void hasPermission(@Nonnull BuiltInAction action,
                              @Nonnull Consumer<Boolean> callback) {
        hasPermission(action.getActionId(), new DispatchServiceCallback<Boolean>(errorDisplay) {
            @Override
            public void handleSuccess(Boolean hasPermission) {
                callback.accept(hasPermission);
            }
        });
    }
}

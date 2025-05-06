package edu.stanford.bmir.protege.web.client.permissions;

import edu.stanford.bmir.protege.web.client.dispatch.*;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.access.*;
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
public class LoggedInUserProjectCapabilityCheckerImpl implements LoggedInUserProjectCapabilityChecker {

    private final Logger logger = Logger.getLogger(LoggedInUserProjectPermissionCheckerImpl.class.getName());

    private final LoggedInUserProvider loggedInUserProvider;

    private final ActiveProjectManager activeProjectManager;

    private final PermissionManager permissionManager;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Inject
    public LoggedInUserProjectCapabilityCheckerImpl(@Nonnull LoggedInUserProvider loggedInUserProvider,
                                                    @Nonnull ActiveProjectManager activeProjectManager,
                                                    @Nonnull PermissionManager permissionManager, @Nonnull DispatchErrorMessageDisplay errorDisplay) {
        this.loggedInUserProvider = checkNotNull(loggedInUserProvider);
        this.activeProjectManager = checkNotNull(activeProjectManager);
        this.permissionManager = checkNotNull(permissionManager);
        this.errorDisplay = checkNotNull(errorDisplay);
    }

    @Override
    public void hasCapability(@Nonnull BasicCapability basicCapability,
                              @Nonnull DispatchServiceCallback<Boolean> callback) {
        Optional<ProjectId> projectId = activeProjectManager.getActiveProjectId();
        if (!projectId.isPresent()) {
            callback.onSuccess(false);
            return;
        }
        UserId userId = loggedInUserProvider.getCurrentUserId();
        GWT.log("[LoggedInUserProjectCapabilityCheckerImpl] Checking permissions for: " + userId + " on " + projectId.get());
        permissionManager.hasPermissionForProject(userId, basicCapability, projectId.get(), callback);
    }

    @Override
    public void hasCapability(@Nonnull BasicCapability action,
                              @Nonnull Consumer<Boolean> callback) {
        hasCapability(action, new DispatchServiceCallback<Boolean>(errorDisplay) {
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
    public void hasCapability(@Nonnull BuiltInCapability action,
                              @Nonnull Consumer<Boolean> callback) {
        hasCapability(action.getCapability(), new DispatchServiceCallback<Boolean>(errorDisplay) {
            @Override
            public void handleSuccess(Boolean hasPermission) {
                callback.accept(hasPermission);
            }
        });
    }
}

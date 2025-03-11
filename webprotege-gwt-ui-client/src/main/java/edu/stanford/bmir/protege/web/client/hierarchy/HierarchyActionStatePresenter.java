package edu.stanford.bmir.protege.web.client.hierarchy;

import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.shared.access.BasicCapability;
import edu.stanford.bmir.protege.web.shared.access.BuiltInCapability;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent.ON_USER_LOGGED_IN;
import static edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent.ON_USER_LOGGED_OUT;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 6 Dec 2017
 */
public class HierarchyActionStatePresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    private final Map<BasicCapability, UIAction> actionMap = new HashMap<>();

    private boolean selectionPresent = false;

    @Inject
    public HierarchyActionStatePresenter(@Nonnull ProjectId projectId,
                                         @Nonnull LoggedInUserProjectCapabilityChecker capabilityChecker) {
        this.projectId = projectId;
        this.capabilityChecker = capabilityChecker;
    }

    public void registerAction(@Nonnull BuiltInCapability actionId,
                               @Nonnull UIAction uiAction) {
        registerAction(actionId.getCapability(), uiAction);
    }

    public void registerAction(@Nonnull BasicCapability basicCapability,
                               @Nonnull UIAction action) {
        actionMap.put(checkNotNull(basicCapability),
                      checkNotNull(action));
    }

    public void start(@Nonnull WebProtegeEventBus eventBus) {
        eventBus.addProjectEventHandler(projectId,
                                        ON_PERMISSIONS_CHANGED,
                                        event -> updateActionStates());
        eventBus.addApplicationEventHandler(ON_USER_LOGGED_OUT,
                                            event -> updateActionStates());
        eventBus.addApplicationEventHandler(ON_USER_LOGGED_IN,
                                            event -> updateActionStates());
        updateActionStates();
    }

    public void setSelectionPresent(boolean selectionPresent) {
        if (selectionPresent != this.selectionPresent) {
            this.selectionPresent = selectionPresent;
            updateActionStates();
        }

    }

    private void updateActionStates() {
        actionMap.forEach(this::updateState);
    }

    private void updateState(@Nonnull BasicCapability basicCapability,
                             @Nonnull UIAction action) {
        action.setEnabled(false);
        capabilityChecker.hasCapability(basicCapability,
                                        perm -> {
                                            action.setEnabled(perm && (!action.requiresSelection() || selectionPresent));
                                        });

    }
}

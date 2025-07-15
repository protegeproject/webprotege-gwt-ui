package edu.stanford.bmir.protege.web.client.issues;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.VIEW_OBJECT_COMMENT;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_CAPABILITIES_CHANGED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
@Portlet(id = "portlets.CommentedEntities",
         title = "Commented entities",
         tooltip = "Displays a list of commented entities")
public class CommentedEntitiesPortletPresenter extends AbstractWebProtegePortletPresenter {

    @Nonnull
    private final CommentedEntitiesPresenter presenter;

    @Nonnull
    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    @Nonnull
    private final Messages messages;

    private Optional<PortletUi> portletUi = Optional.empty();

    @Inject
    public CommentedEntitiesPortletPresenter(@Nonnull SelectionModel selectionModel,
                                             @Nonnull SelectedPathsModel selectedPathsModel,
                                             @Nonnull ProjectId projectId,
                                             @Nonnull CommentedEntitiesPresenter presenter,
                                             @Nonnull LoggedInUserProjectCapabilityChecker capabilityChecker,
                                             @Nonnull Messages messages,
                                             DisplayNameRenderer displayNameRenderer,
                                             DispatchServiceManager dispatch) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.presenter = presenter;
        this.capabilityChecker = capabilityChecker;
        this.messages = messages;
    }

    @Override
    public void startPortlet(final PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setForbiddenMessage(messages.discussionThread_ViewingForbidden());
        capabilityChecker.hasCapability(VIEW_OBJECT_COMMENT,
                                        canView -> {
                                            if (canView) {
                                                presenter.start(portletUi, eventBus);
                                                portletUi.setForbiddenVisible(false);
                                            }
                                            else {
                                                portletUi.setForbiddenVisible(true);
                                            }
                                        });
        eventBus.addProjectEventHandler(getProjectId(),
                ON_CAPABILITIES_CHANGED,
                                        event -> capabilityChecker.hasCapability(VIEW_OBJECT_COMMENT,
                                                                                 canView -> portletUi.setForbiddenVisible(!canView)));
        presenter.setHasBusy(portletUi);
    }

    @Override
    protected void handleReloadRequest() {
        presenter.reload();
    }
}

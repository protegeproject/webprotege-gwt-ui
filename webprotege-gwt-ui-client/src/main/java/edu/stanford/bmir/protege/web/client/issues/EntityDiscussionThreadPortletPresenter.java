package edu.stanford.bmir.protege.web.client.issues;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.VIEW_OBJECT_COMMENT;
import static edu.stanford.bmir.protege.web.shared.filter.FilterSetting.OFF;
import static edu.stanford.bmir.protege.web.shared.filter.FilterSetting.ON;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_CAPABILITIES_CHANGED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@Portlet(id = "portlets.Comments", title = "Comments", tooltip = "Displays comments for the selected entity")
public class EntityDiscussionThreadPortletPresenter extends AbstractWebProtegePortletPresenter {

    @Nonnull
    private final FilterId displayResolvedThreadsFilter;

    @Nonnull
    private final DiscussionThreadListPresenter presenter;

    @Nonnull
    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    @Nonnull
    private final UIAction addCommentAction;

    @Nonnull
    private final FilterView filterView;

    @Nonnull
    private final Messages messages;

    private Optional<PortletUi> portletUi = Optional.empty();



    @Inject
    public EntityDiscussionThreadPortletPresenter(@Nonnull SelectionModel selectionModel,
                                                  @Nonnull SelectedPathsModel selectedPathsModel,
                                                  @Nonnull FilterView filterView,
                                                  @Nonnull Messages messages,
                                                  @Nonnull LoggedInUserProjectCapabilityChecker capabilityChecker,
                                                  @Nonnull ProjectId projectId,
                                                  @Nonnull DiscussionThreadListPresenter presenter,
                                                  DisplayNameRenderer displayNameRenderer,
                                                  DispatchServiceManager dispatch) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.filterView = filterView;
        this.messages = messages;
        this.displayResolvedThreadsFilter = new FilterId(messages.discussionThread_DisplayResolvedThreads());
        filterView.addFilter(displayResolvedThreadsFilter, OFF);
        filterView.addValueChangeHandler(event -> handleFilterSettingChanged());
        this.presenter = presenter;
        this.capabilityChecker = capabilityChecker;
        this.addCommentAction = new PortletAction(messages.startNewCommentThread(),
                                                  "wp-btn-g--create-thread",
                                                  presenter::createThread);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        eventBus.addProjectEventHandler(getProjectId(),
                ON_CAPABILITIES_CHANGED,
                                        this::handlePemissionsChange);
        this.portletUi = Optional.of(portletUi);
        portletUi.setWidget(presenter.getView());
        portletUi.addAction(addCommentAction);
        addCommentAction.setEnabled(false);
        portletUi.setFilterView(filterView);
        portletUi.setForbiddenMessage(messages.discussionThread_ViewingForbidden());
        presenter.setHasBusy(portletUi);
        presenter.start(eventBus);
        presenter.setEntityDisplay(this);
        handleSetEntity(getSelectedEntity());
        setDisplaySelectedEntityNameAsSubtitle(true);
    }

    private void handleFilterSettingChanged() {
        boolean displayResolvedThreads = filterView.getFilterSet()
                  .getFilterSetting(displayResolvedThreadsFilter, OFF) == ON;
        presenter.setDisplayResolvedThreads(displayResolvedThreads);
    }

    private void handlePemissionsChange(PermissionsChangedEvent event) {
        handleAfterSetEntity(getSelectedEntity());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        handleSetEntity(entity);
    }

    @Override
    protected void handleReloadRequest() {
        handleAfterSetEntity(getSelectedEntity());
    }

    private void handleSetEntity(Optional<OWLEntity> entity) {
        addCommentAction.setEnabled(entity.isPresent());
        portletUi.ifPresent(portletUi -> {
            capabilityChecker.hasCapability(VIEW_OBJECT_COMMENT, canViewComments -> {
                portletUi.setForbiddenVisible(!canViewComments);
                if(canViewComments) {
                    if(entity.isPresent()) {
                        presenter.setEntity(entity.get());
                    }
                    else {
                        presenter.clear();
                        setDisplayedEntity(Optional.empty());
                    }
                }
            });
        });

    }
}

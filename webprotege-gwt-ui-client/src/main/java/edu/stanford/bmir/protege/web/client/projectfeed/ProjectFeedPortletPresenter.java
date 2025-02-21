package edu.stanford.bmir.protege.web.client.projectfeed;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.filter.FilterSetting.ON;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
@Portlet(id = "portlets.ProjectFeed", title = "Project Feed")
public class ProjectFeedPortletPresenter extends AbstractWebProtegePortletPresenter {

    public static final FilterId SHOW_MY_ACTIVITY_FILTER = new FilterId("Show my activity");

    public static final FilterId SHOW_PROJECT_CHANGES_FILTER = new FilterId("Show project changes");

    private final LoggedInUserProvider loggedInUserProvider;

    private final ProjectFeedPresenter presenter;

    private final FilterView filterView;

    @Inject
    public ProjectFeedPortletPresenter(ProjectFeedPresenter presenter,
                                       FilterView filterView,
                                       SelectionModel selectionModel,
                                       @Nonnull SelectedPathsModel selectedPathsModel,
                                       ProjectId projectId,
                                       LoggedInUserProvider loggedInUserManager,
                                       DisplayNameRenderer displayNameRenderer, DispatchServiceManager dispatch) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.loggedInUserProvider = loggedInUserManager;
        this.presenter = presenter;
        this.filterView = filterView;
        filterView.addFilterGroup("Project feed settings");
        filterView.addFilter(SHOW_MY_ACTIVITY_FILTER, ON);
        filterView.addFilter(SHOW_PROJECT_CHANGES_FILTER, ON);
        filterView.closeCurrentGroup();
        filterView.addValueChangeHandler(event -> applyFilters(event.getValue()));
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        presenter.start(eventBus);
        portletUi.setWidget(presenter.getView());
        portletUi.setFilterView(filterView);
    }

    @Override
    protected void handleReloadRequest() {

    }

    private void applyFilters(FilterSet filterSet) {
        FilterSetting showMyActivity = filterSet.getFilterSetting(SHOW_MY_ACTIVITY_FILTER, ON);
        presenter.setUserActivityVisible(loggedInUserProvider.getCurrentUserId(), showMyActivity == ON);

        FilterSetting showProjectChanges = filterSet.getFilterSetting(SHOW_PROJECT_CHANGES_FILTER, ON);
        presenter.setUserActivityVisible(loggedInUserProvider.getCurrentUserId(), showProjectChanges == ON);

    }

}

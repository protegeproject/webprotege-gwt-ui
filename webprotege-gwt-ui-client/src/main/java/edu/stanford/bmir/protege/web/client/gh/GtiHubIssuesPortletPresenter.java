package edu.stanford.bmir.protege.web.client.gh;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.gh.GetGitHubIssuesAction;
import edu.stanford.bmir.protege.web.shared.gh.GetGitHubIssuesResult;
import edu.stanford.bmir.protege.web.shared.gh.GitHubIssue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-18
 */
@Portlet(id = "github.issues",
         title = "GitHub Issues",
        tooltip = "Displays associated GitHub issues for the selected entity")
public class GtiHubIssuesPortletPresenter extends AbstractWebProtegePortletPresenter {


    @Nonnull
    private final DispatchServiceManager dispatch;

    private final GitHubIssuesView view;

    @Nonnull
    private final GitHubIssuePresenterFactory issuePresenterFactory;

    private WebProtegeEventBus eventBus;

    @Inject
    public GtiHubIssuesPortletPresenter(@Nonnull SelectionModel selectionModel,
                                        @Nonnull ProjectId projectId,
                                        @Nonnull DisplayNameRenderer displayNameRenderer,
                                        @Nonnull DispatchServiceManager dispatch, 
                                        GitHubIssuesView view,
                                        @Nonnull GitHubIssuePresenterFactory issuePresenterFactory) {
        super(selectionModel, projectId, displayNameRenderer, dispatch);
        this.dispatch = dispatch;
        this.view = view;
        this.issuePresenterFactory = issuePresenterFactory;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(view);
        this.eventBus = eventBus;
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        entityData.ifPresent(entity -> {
            dispatch.execute(GetGitHubIssuesAction.get(getProjectId(),
                                                       entity),
                             this, this::displayIssues);
        });
    }

    private void displayIssues(GetGitHubIssuesResult result) {
        view.clearIssueContainers();
        List<GitHubIssue> issues = result.getIssues();
        for(GitHubIssue issue : issues) {
            GitHubIssuePresenter presenter = issuePresenterFactory.create();
            AcceptsOneWidget issueContainer = view.addIssueContainer();
            presenter.start(eventBus, issueContainer);
            presenter.displayIssue(issue);
        }
    }

    @Override
    protected void handleReloadRequest() {

    }
}

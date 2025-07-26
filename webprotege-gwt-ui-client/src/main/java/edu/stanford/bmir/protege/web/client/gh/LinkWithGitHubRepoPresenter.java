package edu.stanford.bmir.protege.web.client.gh;

import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.gh.GitHubAppInstallationStatus;
import edu.stanford.bmir.protege.web.shared.gh.GitHubRepositoryCoordinates;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

public class LinkWithGitHubRepoPresenter {

    private final ProjectId projectId;

    private final LinkWithGitHubRepoView view;

    private final DispatchServiceManager dispatch;

    @Inject
    public LinkWithGitHubRepoPresenter(ProjectId projectId,
                                       LinkWithGitHubRepoView view,
                                       DispatchServiceManager dispatch) {
        this.projectId = projectId;
        this.view = view;
        this.dispatch = dispatch;
    }

    public void start(AcceptsOneWidget container) {
        view.setRepoCoordinatesChangedHandler(this::handleRepositoryCoordinatesChanged);
        view.setInstallGitHubAppHandler(this::handleInstallGitHubApp);
        dispatch.execute(GetLinkedGitHubRepositoryAction.create(projectId), result -> {
            result.getRepositoryCoordinates()
                            .ifPresent(rc -> {
                                view.setOwner(rc.ownerName());
                                view.setRepo(rc.repositoryName());
                            });
            container.setWidget(view);
        });
    }

    private GitHubRepositoryCoordinates getRepoCoordinates() {
        return GitHubRepositoryCoordinates.of(view.getOwner(), view.getRepo());
    }

    private void handleRepositoryCoordinatesChanged(String ownerName, String repoName) {
        if(ownerName.isEmpty()) {
            return;
        }
        if(repoName.isEmpty()) {
            return;
        }
        GitHubRepositoryCoordinates coordinates = getRepoCoordinates();
        dispatch.execute(GetGitHubAppInstallationStatusAction.create(projectId, coordinates), this::handleGetGitHubAppInstallationStatusResult);
    }

    private void handleGetGitHubAppInstallationStatusResult(GetGitHubAppInstallationStatusResult result) {
        GitHubAppInstallationStatus installationStatus = result.getInstallationStatus();
        view.setStatusMessage("");
        view.setInstallButtonVisible(false);
        if(installationStatus.equals(GitHubAppInstallationStatus.NOT_INSTALLED)) {
            view.setStatusMessage("The WebProtégé GitHub App is NOT installed in the " +  getRepoCoordinates().getFullName() + " repository.  You must install the WebProtégé GitHub App in this repository in order to connect to this repository in WebProtégé.  To do this, click the install button.  You will be taken to the installation page in a new broswer window.  When you have completed the installation you should come back here.");
            view.setInstallButtonVisible(true);
        }
        else {
            view.setStatusMessage("The WebProtégé GitHub App is already installed in the " + getRepoCoordinates().getFullName() + " repository.  To link this project to the repository, press Link repository.");
        }
    }

    private void handleInstallGitHubApp() {
        String returnToUrl = view.getReturnToUrl();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("returnTo", new JSONString(returnToUrl));
        jsonObject.put("projectId", new JSONString(projectId.getId()));
        String encodedStateJson = URL.encodeQueryString(jsonObject.toString());
        String installLink = "https://github.com/apps/webprotege-stanford-edu-github-app/installations/new?state=" + encodedStateJson;

        String windowName = "_blank"; // This opens the URL in a new window or tab
        Window.open(installLink, windowName, "");
    }

    public void handleLinkRepository(Runnable callback) {
        if(view.getOwner().isEmpty()) {
            callback.run();
            return;
        }
        if(view.getRepo().isEmpty()) {
            callback.run();
            return;
        }
        dispatch.execute(SetLinkedGitHubRepositoryAction.create(projectId,
                getRepoCoordinates()), result -> {
                callback.run();
        });
    }
}

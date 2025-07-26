package edu.stanford.bmir.protege.web.client.gh;

import com.google.gwt.user.client.ui.IsWidget;

public interface LinkWithGitHubRepoView extends IsWidget {

    String getOwner();

    void setOwner(String owner);

    String getRepo();

    void setRepo(String repo);

    void setRepoCoordinatesChangedHandler(RepoCoordinatesChangedHandler handler);

    void setInstallGitHubAppHandler(InstallGitHubAppHandler handler);

    String getReturnToUrl();

    void setStatusMessage(String message);

    void setInstallButtonVisible(boolean visible);
}

package edu.stanford.bmir.protege.web.client.gh;

import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.JSON;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ProjectSingleton
public class ConnectToGitHubHandler {

    private final ProjectId projectId;

    @Inject
    public ConnectToGitHubHandler(ProjectId projectId, LinkWithGitHubRepoModalPresenter modalPresenter) {
        this.projectId = projectId;
        this.modalPresenter = modalPresenter;
    }

    private final LinkWithGitHubRepoModalPresenter modalPresenter;

    public void handleConnectToGitHub() {
        modalPresenter.show();
    }

}

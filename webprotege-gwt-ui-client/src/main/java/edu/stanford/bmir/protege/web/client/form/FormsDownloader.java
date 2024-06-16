package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.*;
import edu.stanford.bmir.protege.web.client.download.FetchAndOpenInBrowserWindow;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetUserInfoAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
public class FormsDownloader {

    @Nonnull
    private final ProjectId projectId;

    private final DispatchServiceManager dispatch;

    private final MessageBoxErrorDisplay errorDisplay;

    @Inject
    public FormsDownloader(@Nonnull ProjectId projectId, DispatchServiceManager dispatch, MessageBoxErrorDisplay errorDisplay) {
        this.projectId = checkNotNull(projectId);
        this.dispatch = checkNotNull(dispatch);
        this.errorDisplay = checkNotNull(errorDisplay);
    }

    public void download() {
        dispatch.execute(new GetUserInfoAction(), result -> {
            String baseURL = GWT.getHostPageBaseURL() + "data/";
            String token = result.getToken();
            FetchAndOpenInBrowserWindow.fetchUrlAndOpenInWindow("/data/projects/" + projectId.getId() + "/forms",
                                                                token,
                                                                () -> {
                                                                    errorDisplay.displayGeneralErrorMessage("Download error",
                                                                                                            "Could not download project forms");
                                                                });
        });
    }
}

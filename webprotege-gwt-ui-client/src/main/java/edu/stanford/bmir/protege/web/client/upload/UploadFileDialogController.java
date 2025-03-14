package edu.stanford.bmir.protege.web.client.upload;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.dlg.*;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetUserInfoAction;

import javax.annotation.Nonnull;
import java.util.logging.Logger;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/05/2013
 */
@AutoFactory
public class UploadFileDialogController extends WebProtegeOKCancelDialogController<String> {
    Logger logger = java.util.logging.Logger.getLogger("UploadFileDialogController");

    private final DispatchServiceManager dispatch;
    private final ProgressDisplay progressDisplay;
    private final UploadFileDialogForm form;

    public UploadFileDialogController(String title,
                                      final UploadFileResultHandler resultHandler,
                                      boolean showOverrideCheckbox,
                                      @Provided DispatchServiceManager dispatch,
                                      @Provided ProgressDisplay progressDisplay) {
        super(title);
        this.dispatch = dispatch;
        this.progressDisplay = progressDisplay;
        this.form = new UploadFileDialogForm(showOverrideCheckbox);

        setDialogButtonHandler(DialogButton.OK, (data, closer) -> handleButtonPress(resultHandler, closer));
        form.getFileUpload().getElement().setId(UuidV4.uuidv4());
    }

    private void handleButtonPress(UploadFileResultHandler resultHandler, WebProtegeDialogCloser closer) {
        progressDisplay.displayProgress("Uploading file", "Uploading file. Please wait.");
        dispatch.execute(new GetUserInfoAction(), userInfo -> {
            String token = userInfo.getToken();
            String fileUploadId = form.getFileUpload().getElement().getId();
            boolean overrideExisting = form.shouldOverrideExisting();

            FileUploader fileUploader = new FileUploader();
            fileUploader.uploadFile(fileUploadId, token, fileSubmissionId -> {
                progressDisplay.hideProgress();
                closer.hide();
                resultHandler.handleFileUploaded(new DocumentId(fileSubmissionId), overrideExisting);
            }, errorCode -> {
                progressDisplay.hideProgress();
                closer.hide();
                resultHandler.handleFileUploadFailed("An error occurred uploading the file. Error code: " + errorCode);
            });
        });
    }

    @Nonnull
    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return form.getInitialFocusable();
    }

    @Override
    public String getData() {
        return form.getFileName();
    }

    @Override
    public Widget getWidget() {
        return form;
    }
}

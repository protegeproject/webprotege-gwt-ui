package edu.stanford.bmir.protege.web.client.upload;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.dlg.*;
import edu.stanford.bmir.protege.web.client.progress.ProgressMonitor;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4;
import edu.stanford.bmir.protege.web.shared.upload.SubmitFileAction;
import edu.stanford.bmir.protege.web.shared.upload.SubmitFileResult;

import javax.annotation.Nonnull;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/05/2013
 */
@AutoFactory
public class UploadFileDialogController extends WebProtegeOKCancelDialogController<String> {

    private final DispatchServiceManager dispatch;

    private final DispatchErrorMessageDisplay errorMessageDisplay;

    private final ProgressDisplay progressDisplay;

    private UploadFileDialogForm form = new UploadFileDialogForm();

    public UploadFileDialogController(String title,
                                      final UploadFileResultHandler resultHandler,
                                      @Provided DispatchServiceManager dispatch,
                                      @Provided DispatchErrorMessageDisplay errorMessageDisplay,
                                      @Provided ProgressDisplay progressDisplay) {
        super(title);
        this.dispatch = dispatch;
        this.errorMessageDisplay = errorMessageDisplay;
        this.progressDisplay = progressDisplay;
        setDialogButtonHandler(DialogButton.OK, (data, closer) -> handleButtonPress(resultHandler, closer));
        form.getFileUpload().getElement().setId(UuidV4.uuidv4());
    }

    private void handleButtonPress(UploadFileResultHandler resultHandler, WebProtegeDialogCloser closer) {
        ProgressMonitor.get().showProgressMonitor("Preparing file", "Preparing file for upload.  Please wait.");
        FileUploadFileReader reader = new FileUploadFileReader();
        reader.readFiles(form.getFileUpload().getElement().getId(),
                         content -> {
                             dispatch.execute(SubmitFileAction.create(content), new DispatchServiceCallbackWithProgressDisplay<SubmitFileResult>(errorMessageDisplay,
                                                                                                                                                 progressDisplay) {
                                 @Override
                                 public String getProgressDisplayTitle() {
                                     return "Uploading file";
                                 }

                                 @Override
                                 public String getProgressDisplayMessage() {
                                     return "Please wait";
                                 }

                                 @Override
                                 public void handleSuccess(SubmitFileResult submitFileResult) {
                                     closer.hide();
                                     resultHandler.handleFileUploaded(submitFileResult.getFileSubmissionId());
                                 }

                                 @Override
                                 public void handleErrorFinally(Throwable throwable) {
                                     closer.hide();
                                     resultHandler.handleFileUploadFailed("An error occurred uploading the file: " + throwable.getMessage());
                                 }
                             });
                         },
                         errorHandler -> {
                             ProgressMonitor.get().hideProgressMonitor();
                             resultHandler.handleFileUploadFailed("An error occurred preparing the file for upload");
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

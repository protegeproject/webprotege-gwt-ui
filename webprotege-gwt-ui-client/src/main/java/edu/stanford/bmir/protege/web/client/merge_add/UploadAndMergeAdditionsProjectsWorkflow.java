package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.*;
import edu.stanford.bmir.protege.web.client.library.dlg.*;
import edu.stanford.bmir.protege.web.client.upload.*;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.merge_add.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

public class UploadAndMergeAdditionsProjectsWorkflow {

    @Nonnull
    private final SelectOptionForMergeAdditionsWorkflow selectOptionsWorkflow;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final UploadFileDialogControllerFactory uploadFileDialogControllerFactory;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ProgressDisplay progressDisplay;

    @Inject
    public UploadAndMergeAdditionsProjectsWorkflow(@Nonnull SelectOptionForMergeAdditionsWorkflow selectOptionsWorkflow,
                                                   @Nonnull DispatchServiceManager dispatchServiceManager,
                                                   @Nonnull UploadFileDialogControllerFactory uploadFileDialogControllerFactory,
                                                   @Nonnull DispatchErrorMessageDisplay errorDisplay,
                                                   @Nonnull ProgressDisplay progressDisplay) {
        this.selectOptionsWorkflow = selectOptionsWorkflow;
        this.dispatchServiceManager = dispatchServiceManager;
        this.uploadFileDialogControllerFactory = uploadFileDialogControllerFactory;
        this.errorDisplay = errorDisplay;
        this.progressDisplay = progressDisplay;
    }

    public void start(ProjectId projectId) {
        uploadProject(projectId);
    }

    private void uploadProject(final ProjectId projectId) {
        UploadFileDialogController uploadFileDialogController = uploadFileDialogControllerFactory.create("Upload ontologies", new UploadFileResultHandler() {
            @Override
            public void handleFileUploaded(DocumentId fileDocumentId, boolean overrideExisting) {
                getOntologies(projectId, fileDocumentId);
            }

            @Override
            public void handleFileUploadFailed(String errorMessage) {
                GWT.log("Upload failed");
            }
        },
                false);
        WebProtegeDialog.showDialog(uploadFileDialogController);
    }

    private void getOntologies(ProjectId projectId, DocumentId documentId){
        dispatchServiceManager.execute(GetAllOntologiesAction.create(projectId, documentId), new DispatchServiceCallbackWithProgressDisplay<GetAllOntologiesResult>(errorDisplay, progressDisplay) {
            @Override
            public String getProgressDisplayTitle() {
                return "Uploading Ontologies";
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Uploading and processing Ontologies. Please Wait.";
            }

            @Override
            public void handleSuccess(GetAllOntologiesResult result){
                selectOntologies(projectId, documentId, result);
            }
        });
    }

    private void selectOntologies(ProjectId projectId, DocumentId documentId, GetAllOntologiesResult result){
        ArrayList<OWLOntologyID> list = (ArrayList<OWLOntologyID>) result.getOntologies();

        SelectOntologiesForMergeView view = new SelectOntologiesForMergeViewImpl(list);
        SelectOntologiesForMergeDialogController controller = new SelectOntologiesForMergeDialogController(view);
        controller.setDialogButtonHandler(DialogButton.OK, (data, closer) -> {
            List<OWLOntologyID> l = view.getSelectedOntologies();
            startSelectAdditionsWorkflow(projectId, documentId, list, l);
            closer.hide();
        });
        WebProtegeDialog.showDialog(controller);
    }


    private void startSelectAdditionsWorkflow(ProjectId projectId, DocumentId documentId, List<OWLOntologyID> allOntologies, List<OWLOntologyID> selectedOntologies) {
        selectOptionsWorkflow.start(projectId, documentId, allOntologies, selectedOntologies);
    }
}

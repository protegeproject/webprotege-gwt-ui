package edu.stanford.bmir.protege.web.client.postcoordination;

import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.function.BiFunction;


public class UploadPostCoordinationWorkflowConfiguration {

    private final String menuTitle;

    private final String progressDisplayTitle;

    private final String progressDisplayMessages;

    private final String uploadSuccessMessage;

    private final BiFunction<ProjectId, DocumentId, AbstractHasProjectAction> actionForDocumentUploadSupplier;

    public UploadPostCoordinationWorkflowConfiguration(String menuTitle, String progressDisplayTitle, String progressDisplayMessages, String uploadSucessMessage, BiFunction<ProjectId, DocumentId, AbstractHasProjectAction> actionForDocumentUpload) {
        this.menuTitle = menuTitle;
        this.progressDisplayTitle = progressDisplayTitle;
        this.progressDisplayMessages = progressDisplayMessages;
        this.uploadSuccessMessage = uploadSucessMessage;
        this.actionForDocumentUploadSupplier = actionForDocumentUpload;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public String getProgressDisplayTitle() {
        return progressDisplayTitle;
    }

    public String getProgressDisplayMessages() {
        return progressDisplayMessages;
    }

    public String getUploadSuccessMessage() {
        return uploadSuccessMessage;
    }

    public BiFunction<ProjectId, DocumentId, AbstractHasProjectAction> getActionForDocumentUploadSupplier() {
        return actionForDocumentUploadSupplier;
    }
}

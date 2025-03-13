package edu.stanford.bmir.protege.web.client.upload;

import edu.stanford.bmir.protege.web.shared.csv.DocumentId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2013
 */
public interface UploadFileResultHandler {

    void handleFileUploaded(DocumentId fileDocumentId, boolean overrideExisting);

    void handleFileUploadFailed(String errorMessage);
}

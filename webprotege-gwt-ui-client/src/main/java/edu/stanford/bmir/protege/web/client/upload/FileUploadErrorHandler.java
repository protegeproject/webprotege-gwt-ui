package edu.stanford.bmir.protege.web.client.upload;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-10
 */
public interface FileUploadErrorHandler {

    void handleFileUploadError(int statusCode);
}

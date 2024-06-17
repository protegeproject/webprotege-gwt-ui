package edu.stanford.bmir.protege.web.client.upload;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-10
 */
public class FileUploader {

    private static final String END_POINT = "/files/submit";

    /**
     * Upload a file that is retrieved from a FileInput element
     * @param fileInputId The Id of the file input element
     * @param token The authentication token to use when uploading the file
     * @param successHandler The handler that will be called if the upload succeeds.
     * @param errorHandler The handle that will be called if the upload fails.
     */
    public void uploadFile(String fileInputId,
                           String token,
                           FileUploadSuccessHandler successHandler,
                           FileUploadErrorHandler errorHandler) {
        uploadFileNative(fileInputId, END_POINT, token, successHandler, errorHandler);
    }

    private native void uploadFileNative(String fileInputId, String endPoint, String token,
                                  FileUploadSuccessHandler successHandler,
                                  FileUploadErrorHandler errorHandler)/*-{

        var fileElement = $doc.getElementById(fileInputId);
        var file = fileElement.files[0];

        var formData = new FormData();
        formData.append("file", file);

        var xhr = new XMLHttpRequest();
        xhr.open("POST", endPoint, true);
        var authHeader = "bearer " + token;
        xhr.setRequestHeader('Authorization', authHeader);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if(xhr.status === 200) {
                    var submissionId = xhr.responseText.substring(1, xhr.responseText.length - 1);
                    successHandler.@edu.stanford.bmir.protege.web.client.upload.FileUploadSuccessHandler::handleFileUploadSuccess(*)(submissionId);
                }
                else {
                    var errorCode = xhr.status;
                    errorHandler.@edu.stanford.bmir.protege.web.client.upload.FileUploadErrorHandler::handleFileUploadError(*)(errorCode);
                }
            }
        };
        // Initiate a multipart/form-data upload
        xhr.send(formData);

    }-*/;
}

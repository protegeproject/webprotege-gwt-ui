package edu.stanford.bmir.protege.web.client.upload;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-01
 */
public class FileUploadFileReader {

    public void readFiles(String fileUploadElementId,
                          FileReadHandler fileReadHandler,
                          FileReadErrorHandler errorHandler) {
        readFileNative(fileUploadElementId, fileReadHandler, errorHandler);
    }

    private native void readFileNative(String fileInputId,
                                       FileReadHandler handler,
                                       FileReadErrorHandler errorHandler) /*-{
        var fileElement = $doc.getElementById(fileInputId);
        var file = fileElement.files[0];
        var reader = new FileReader();
        reader.readAsDataURL(file);

        reader.onerror = function (event) {
            var msg = JSON.stringify(event);
            errorHandler.@edu.stanford.bmir.protege.web.client.upload.FileReadErrorHandler::handleFileReadError(*)(msg);
        }

        reader.onloadend = function () {
            var base64data = reader.result.substring('data:application/octet-stream;base64,'.length);
            handler.@edu.stanford.bmir.protege.web.client.upload.FileReadHandler::handleFileRead(*)(base64data);
        }
        return "";
    }-*/;


}

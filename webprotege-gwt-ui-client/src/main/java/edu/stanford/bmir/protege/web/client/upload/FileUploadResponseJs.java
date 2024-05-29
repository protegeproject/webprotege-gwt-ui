package edu.stanford.bmir.protege.web.client.upload;

import edu.stanford.bmir.protege.web.client.d3.Transform;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-01
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class FileUploadResponseJs {

    @JsProperty(name = "result")
    public native String getResult();

    @JsProperty(name = "fileId")
    public native String getFileId();
}

package edu.stanford.bmir.protege.web.client.markdown;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "marked", name = "marked")
public class Marked {
    @JsMethod
    public static native String parse(String markdownText);
}

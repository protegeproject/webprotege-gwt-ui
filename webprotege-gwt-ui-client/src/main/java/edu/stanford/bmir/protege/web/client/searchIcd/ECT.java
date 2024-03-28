package edu.stanford.bmir.protege.web.client.searchIcd;

import edu.stanford.bmir.protege.web.client.d3.Selection;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import java.util.HashMap;
import java.util.Map;

//@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class ECT {

    public static native void configure(Map<String, Object> opts);

//    @JsMethod(name = "search")
    public static native void triggerSearch(String iNo, String query);
}

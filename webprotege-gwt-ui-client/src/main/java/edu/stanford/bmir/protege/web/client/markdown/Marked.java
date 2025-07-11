package edu.stanford.bmir.protege.web.client.markdown;

import jsinterop.annotations.JsType;
import jsinterop.annotations.JsMethod;

/**
 * JSInterop facade for the global `marked` parser.
 */
@JsType(isNative = true, namespace = "marked", name = "marked")
public class Marked {

    /** 
     * Parses a Markdown string into HTML.
     * @param markdownText the raw Markdown text
     * @return the rendered HTML
     */
    @JsMethod
    public static native String parse(String markdownText);

    /** 
     * (Optional) Configure marked before parsing.
     * e.g. you can enable GFM, breaks, autolinks, etc.
     */
    @JsMethod
    public static native void setOptions(Options opts);

    @JsType(isNative = true, namespace = "<global>", name = "Object")
    public static class Options {
        public boolean gfm;
        public boolean breaks;
        public boolean sanitize;
        // …add other flags as needed
    }
}

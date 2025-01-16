package edu.stanford.bmir.protege.web.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.LinkedHashMap;
import java.util.Map;

public class DisplayContext implements IsSerializable {

    private final Map<String, String> properties = new LinkedHashMap<>();

    public DisplayContext() {

    }

    @JsonCreator
    public DisplayContext(Map<String, String> properties) {
        this.properties.putAll(properties);
    }

    public void merge(DisplayContext context) {
        properties.putAll(context.properties);
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public String toString() {
        return "DisplayContext(" + properties + ")";
    }

    @JsonValue
    public Map<String, String> getProperties() {
        return new LinkedHashMap<>(properties);
    }
}

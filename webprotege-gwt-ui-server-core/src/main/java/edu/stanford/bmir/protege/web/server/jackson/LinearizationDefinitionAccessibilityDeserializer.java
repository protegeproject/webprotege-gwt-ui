package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinitionAccessibility;

import java.io.IOException;

public class LinearizationDefinitionAccessibilityDeserializer
        extends StdDeserializer<LinearizationDefinitionAccessibility> {

    public LinearizationDefinitionAccessibilityDeserializer() {
        super(LinearizationDefinitionAccessibility.class);
    }

    @Override
    public LinearizationDefinitionAccessibility deserialize(JsonParser p,
                                                            DeserializationContext ctxt)
                                                            throws IOException {
        String text = p.getText();
        return LinearizationDefinitionAccessibility.valueOf(text.toUpperCase());
    }
}

package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-18
 */
public class TextControlDescriptor_Serialization_TestCase {

    @Test
    public void shouldSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperProvider().get();
        var resourceAsStream = TextControlDescriptor_Serialization_TestCase.class.getResourceAsStream(
                "/forms/TextControlDescriptor.json");
        TextControlDescriptor read = objectMapper.readValue(resourceAsStream, TextControlDescriptor.class);
        String written = objectMapper.writeValueAsString(read);
        TextControlDescriptor reread = objectMapper.readValue(written, TextControlDescriptor.class);
        assertEquals(read, reread);
    }
}

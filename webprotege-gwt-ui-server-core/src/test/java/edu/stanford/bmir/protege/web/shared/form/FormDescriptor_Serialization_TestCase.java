package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-18
 */
public class FormDescriptor_Serialization_TestCase {


    @Test
    public void shouldSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperProvider().get();
        var resourceAsStream = FormDescriptor.class.getResourceAsStream(
                "/forms/FormDescriptor.json");
        FormDescriptor read = objectMapper.readValue(resourceAsStream, FormDescriptor.class);
        String written = objectMapper.writeValueAsString(read);
        System.out.println(written);
        FormDescriptor reread = objectMapper.readValue(written, FormDescriptor.class);
        assertEquals(read, reread);
    }
}

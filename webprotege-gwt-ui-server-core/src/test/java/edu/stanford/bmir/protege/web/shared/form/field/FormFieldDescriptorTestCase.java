package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-18
 */
public class FormFieldDescriptorTestCase {

    @Test
    public void shouldSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperProvider().get();
        var resourceAsStream = FormFieldDescriptorTestCase.class.getResourceAsStream(
                "/forms/FormFieldDescriptor.json");
        FormFieldDescriptor read = objectMapper.readValue(resourceAsStream, FormFieldDescriptor.class);
        String written = objectMapper.writeValueAsString(read);
        System.out.println(written);
        FormFieldDescriptor reread = objectMapper.readValue(written, FormFieldDescriptor.class);
        assertEquals(read, reread);
    }
}

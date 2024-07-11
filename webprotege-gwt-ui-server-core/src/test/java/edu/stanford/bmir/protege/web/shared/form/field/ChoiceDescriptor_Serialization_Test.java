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
public class ChoiceDescriptor_Serialization_Test {

    @Test
    public void shouldSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperProvider().get();
        var resourceAsStream = this.getClass().getResourceAsStream(
                "/forms/ChoiceDescriptor.json");
        ChoiceDescriptor read = objectMapper.readValue(resourceAsStream, ChoiceDescriptor.class);
        String written = objectMapper.writeValueAsString(read);
        System.out.println(written);
        ChoiceDescriptor reread = objectMapper.readValue(written, ChoiceDescriptor.class);
        assertEquals(read, reread);
    }
}

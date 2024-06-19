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
public class EntityNameControlDescriptor_Serialization_TestCase {

    @Test
    public void shouldSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperProvider().get();
        var resourceAsStream = getClass().getResourceAsStream(
                "/forms/EntityNameControlDescriptor.json");
        EntityNameControlDescriptor read = objectMapper.readValue(resourceAsStream, EntityNameControlDescriptor.class);
        String written = objectMapper.writeValueAsString(read);
        EntityNameControlDescriptor reread = objectMapper.readValue(written, EntityNameControlDescriptor.class);
        assertEquals(read, reread);
    }
}

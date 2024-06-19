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
public class GridControlDescriptor_Serialization_TestCase {

    @Test
    public void shouldSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperProvider().get();
        var resourceAsStream = GridControlDescriptor.class.getResourceAsStream(
                "/forms/GridControlDescriptor.json");
        GridControlDescriptor read = objectMapper.readValue(resourceAsStream, GridControlDescriptor.class);
        String written = objectMapper.writeValueAsString(read);
        System.out.println(written);
        GridControlDescriptor reread = objectMapper.readValue(written, GridControlDescriptor.class);
        assertEquals(read, reread);
    }
}

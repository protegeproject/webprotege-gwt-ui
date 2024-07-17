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
public class GridColumnDescriptor_Serialization_TestCase {

    @Test
    public void shouldSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperProvider().get();
        var resourceAsStream = GridColumnDescriptor.class.getResourceAsStream(
                "/forms/GridColumnDescriptor.json");
        GridColumnDescriptor read = objectMapper.readValue(resourceAsStream, GridColumnDescriptor.class);
        String written = objectMapper.writeValueAsString(read);
        System.out.println(written);
        GridColumnDescriptor reread = objectMapper.readValue(written, GridColumnDescriptor.class);
        assertEquals(read, reread);
    }
}

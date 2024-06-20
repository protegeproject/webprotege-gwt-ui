package edu.stanford.bmir.protege.web.shared.form.data;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-18
 */
public class PrimitiveFormControlData_TestCase {

    @Test
    public void shouldDeserializeOnlyValueAsString() throws IOException {
        PrimitiveFormControlData expected = PrimitiveFormControlData.get("xyz");
        JsonSerializationTestUtil.testDeserialization("{\"value\":\"xyz\"}",
                                                      expected,
                                                      PrimitiveFormControlData.class);
    }

    @Test
    public void shouldDeserializeBooleanValue() throws IOException {
        PrimitiveFormControlData expected = PrimitiveFormControlData.get(true);
        JsonSerializationTestUtil.testDeserialization("{\"value\":\"true\", \"type\":\"http://www.w3.org/2001/XMLSchema#boolean\"}",
                                                      expected,
                                                      PrimitiveFormControlData.class);
    }

    @Test
    public void shouldDeserializeBooleanValueAtType() throws IOException {
        PrimitiveFormControlData expected = PrimitiveFormControlData.get(true);
        JsonSerializationTestUtil.testDeserialization("{\"value\":\"true\", \"@type\":\"http://www.w3.org/2001/XMLSchema#boolean\"}",
                                                      expected,
                                                      PrimitiveFormControlData.class);
    }
}

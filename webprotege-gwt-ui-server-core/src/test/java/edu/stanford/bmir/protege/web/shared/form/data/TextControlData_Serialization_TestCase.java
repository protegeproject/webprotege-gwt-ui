package edu.stanford.bmir.protege.web.shared.form.data;

import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.field.TextControlDescriptor;
import org.junit.*;
import org.springframework.boot.test.json.JacksonTester;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-07-02
 */
public class TextControlData_Serialization_TestCase {

    private JacksonTester<FormControlData> tester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test
    public void shouldSerializeWithNullValue() throws IOException {
        var written = tester.write(TextControlData.get(
                TextControlDescriptor.getDefault(),
                null
        ));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("control");
        assertThat(written).hasJsonPath("value");
    }

    @Test
    public void shouldSerializeWithNonNullValue() throws IOException {
        var written = tester.write(TextControlData.get(
                TextControlDescriptor.getDefault(),
                new OWLLiteralImpl("Test", "en", null)
        ));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("control");
        assertThat(written).hasJsonPathValue("value");
    }

    @Test
    public void shouldDeserialize() throws IOException {
        var resource = getClass().getResourceAsStream("/forms/TextControlData.json");
        var read = tester.read(resource);
        assertThat(read).isInstanceOf(TextControlData.class);
    }
}

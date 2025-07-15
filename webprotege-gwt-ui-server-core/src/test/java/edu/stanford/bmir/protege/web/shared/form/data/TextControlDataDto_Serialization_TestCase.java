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
public class TextControlDataDto_Serialization_TestCase {


    private JacksonTester<FormControlDataDto> tester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test
    public void shouldSerializeWithNullValue() throws IOException {
        var written = tester.write(TextControlDataDto.get(
                TextControlDescriptor.getDefault(),
                null,
                3
        ));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("control");
        assertThat(written).hasJsonPath("value");
        assertThat(written).hasJsonPath("depth");
    }

    @Test
    public void shouldSerializeWithNonNullValue() throws IOException {
        var written = tester.write(TextControlDataDto.get(
                TextControlDescriptor.getDefault(),
                new OWLLiteralImpl("Test", "en", null),
                3
        ));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("control");
        assertThat(written).hasJsonPathValue("value");
        assertThat(written).hasJsonPathValue("depth");
    }

    @Test
    public void shouldDeserialize() throws IOException {
        var resource = getClass().getResourceAsStream("/forms/TextControlDataDto.json");
        var read = tester.read(resource);
        assertThat(read).isInstanceOf(TextControlDataDto.class);
    }
}

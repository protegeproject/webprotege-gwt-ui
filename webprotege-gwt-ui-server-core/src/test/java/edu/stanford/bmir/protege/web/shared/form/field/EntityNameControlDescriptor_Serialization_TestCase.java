package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.junit.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.ResolvableType;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-18
 */
public class EntityNameControlDescriptor_Serialization_TestCase {


    JacksonTester<FormControlDescriptor> tester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test
    public void shouldDeserialize() throws IOException {
        var resourceAsStream = TextControlDescriptor_Serialization_TestCase.class.getResourceAsStream(
                "/forms/EntityNameControlDescriptor.json");
        var read = tester.read(resourceAsStream);
        assertThat(read.getObject()).isInstanceOf(EntityNameControlDescriptor.class);
    }

    @Test
    public void shouldDeserializeWithNonIncludedEmpties() throws IOException {
        var resourceAsStream = TextControlDescriptor_Serialization_TestCase.class.getResourceAsStream(
                "/forms/EntityNameControlDescriptor.min.json");
        var read = tester.read(resourceAsStream);
        assertThat(read.getObject()).isInstanceOf(EntityNameControlDescriptor.class);
    }

    @Test
    public void shouldSerializeWithoutEmpties() throws IOException {
        var desc = EntityNameControlDescriptor.get(null, null);
        var written = tester.write(desc);
        System.out.println(written.getJson());
        assertThat(written).doesNotHaveJsonPath("placeholder");
        assertThat(written).doesNotHaveJsonPath("criteria");
    }
}

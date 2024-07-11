package edu.stanford.bmir.protege.web.shared.form.field;

import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import org.junit.*;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-07-02
 */
public class EntityNameControlDescriptorDto_Serialization_TestCase {


    JacksonTester<FormControlDescriptorDto> tester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test
    public void shouldDeserialize() throws IOException {
        var resourceAsStream = TextControlDescriptor_Serialization_TestCase.class.getResourceAsStream(
                "/forms/EntityNameControlDescriptorDto.json");
        var read = tester.read(resourceAsStream);
        assertThat(read.getObject()).isInstanceOf(EntityNameControlDescriptorDto.class);
    }

    @Test
    public void shouldDeserializeWithNonIncludedEmpties() throws IOException {
        var resourceAsStream = TextControlDescriptor_Serialization_TestCase.class.getResourceAsStream(
                "/forms/EntityNameControlDescriptorDto.min.json");
        var read = tester.read(resourceAsStream);
        assertThat(read.getObject()).isInstanceOf(EntityNameControlDescriptorDto.class);
    }

    @Test
    public void shouldSerializeWithoutEmpties() throws IOException {
        var desc = EntityNameControlDescriptorDto.get(null, null);
        var written = tester.write(desc);
        System.out.println(written.getJson());
        assertThat(written).doesNotHaveJsonPath("placeholder");
        assertThat(written).doesNotHaveJsonPath("criteria");
    }
}

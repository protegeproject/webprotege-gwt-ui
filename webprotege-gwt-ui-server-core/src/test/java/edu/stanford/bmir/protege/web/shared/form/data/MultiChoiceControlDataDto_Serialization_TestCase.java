package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.collect.*;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import org.junit.*;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-07-03
 */
public class MultiChoiceControlDataDto_Serialization_TestCase {


    private JacksonTester<MultiChoiceControlDataDto> tester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test
    public void shouldSerialize() throws IOException {
        var data = MultiChoiceControlDataDto.get(MultiChoiceControlDescriptor.get(FixedChoiceListSourceDescriptor.get(
                                                         ImmutableList.of()), ImmutableList.of()),
                                                 ImmutableList.of(PrimitiveFormControlDataDto.get(IRIData.get(IRI.create(
                                                         "http://example.org/A"), ImmutableMap.of()))),
                                                 3);
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "MultiChoiceControlDataDto");
        assertThat(written).hasJsonPathValue("control");
        assertThat(written).hasJsonPathValue("values");
        assertThat(written).hasJsonPathValue("depth");

    }

    @Test
    public void shouldDeserialize() throws IOException {
        var expected = MultiChoiceControlDataDto.get(MultiChoiceControlDescriptor.get(FixedChoiceListSourceDescriptor.get(
                                                         ImmutableList.of()), ImmutableList.of()),
                                                 ImmutableList.of(PrimitiveFormControlDataDto.get(IRIData.get(IRI.create(
                                                         "http://example.org/A"), ImmutableMap.of()))),
                                                 3);
        var read = tester.read(getClass().getResourceAsStream("/forms/MultiChoiceControlDataDto.json"));
        assertThat(read.getObject()).isEqualTo(expected);
    }
}

package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.entity.*;
import org.junit.*;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.boot.test.json.JacksonTester;
import uk.ac.manchester.cs.owl.owlapi.*;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-07-03
 */
public class PrimitiveFormControlDataDto_Serialization_TestCase {

    private JacksonTester<PrimitiveFormControlDataDto> tester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test
    public void shouldSerializeEntityData() throws IOException {
        var data = PrimitiveFormControlDataDto.get(OWLClassData.get(new OWLClassImpl(IRI.create("http://example.org/A")), ImmutableMap.of()));
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "EntityFormControlDataDto");
        assertThat(written).hasJsonPathValue("entity");
    }

    @Test
    public void shouldDeserializeEntityData() throws IOException {
        var expected = PrimitiveFormControlDataDto.get(OWLClassData.get(new OWLClassImpl(IRI.create("http://example.org/A")), ImmutableMap.of()));
        var read = tester.read(getClass().getResourceAsStream("/forms/EntityFormControlDataDto.json"));
        assertThat(read.getObject()).isEqualTo(expected);
    }

    @Test
    public void shouldSerializeIriData() throws IOException {
        var data = PrimitiveFormControlDataDto.get(IRIData.get(IRI.create("http://example.org/A"), ImmutableMap.of()));
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "IriFormControlDataDto");
        assertThat(written).hasJsonPathValue("iri");
    }

    @Test
    public void shouldDeserializeIriData() throws IOException {
        var expected = PrimitiveFormControlDataDto.get(IRIData.get(IRI.create("http://example.org/A"), ImmutableMap.of()));
        var read = tester.read(getClass().getResourceAsStream("/forms/IriFormControlDataDto.json"));
        assertThat(read.getObject()).isEqualTo(expected);
    }

    @Test
    public void shouldSerializeLiteralData() throws IOException {
        var data = PrimitiveFormControlDataDto.get(OWLLiteralData.get(new OWLLiteralImpl("abc", "en", null)));
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "LiteralFormControlDataDto");
        assertThat(written).hasJsonPathValue("literal");
    }

    @Test
    public void shouldDeserializeLiteralData() throws IOException {
        var expected = PrimitiveFormControlDataDto.get(OWLLiteralData.get(new OWLLiteralImpl("abc", "en", null)));
        var read = tester.read(getClass().getResourceAsStream("/forms/LiteralFormControlDataDto.json"));
        assertThat(read.getObject()).isEqualTo(expected);
    }
}

package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import org.junit.*;
import org.semanticweb.owlapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.io.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-07-03
 */
public class FormSubjectFactoryDescriptor_Serialization_Test {

    private JacksonTester<FormSubjectFactoryDescriptor> tester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test
    public void shouldSerialize() throws IOException {
        var descriptor = FormSubjectFactoryDescriptor.get(
                EntityType.NAMED_INDIVIDUAL,
                new OWLClassImpl(IRI.create("http://example.org/A")),
                Optional.empty()
        );
        var written = tester.write(descriptor);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("entityType");
        assertThat(written).hasJsonPathValue("parent");
        assertThat(written).hasJsonPath("targetOntologyIri");
    }

    @Test
    public void shouldDeserialize() throws IOException {
        var expected = FormSubjectFactoryDescriptor.get(
                EntityType.NAMED_INDIVIDUAL,
                new OWLClassImpl(IRI.create("http://example.org/A")),
                Optional.empty()
        );
        var read = tester.read(getClass().getResourceAsStream("/forms/FormSubjectFactoryDescriptor.json"));
        assertThat(read.getObject()).isEqualTo(expected);

    }
}

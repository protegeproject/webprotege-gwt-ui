package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.junit.*;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.boot.test.json.JacksonTester;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-07-02
 */
public class OwlPropertyBinding_Serialization_TestCase {

    private JacksonTester<OwlBinding> tester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test
    public void shouldDeserialize() throws IOException {
        var read = tester.read(getClass().getResourceAsStream("/forms/OwlPropertyBinding.json"));
        assertThat(read.getObject()).isInstanceOf(OwlPropertyBinding.class);
        var obj = (OwlPropertyBinding) read.getObject();
        assertThat(obj.getProperty()).isNotNull();
    }

    @Test
    public void shouldSerialize() throws IOException {
        var written = tester.write(OwlPropertyBinding.get(
                new OWLAnnotationPropertyImpl(IRI.create("http://example.org/p")),
                CompositeRelationshipValueCriteria.get(MultiMatchType.ALL, ImmutableList.of())
        ));
        assertThat(written).hasJsonPathMapValue("property");
        assertThat(written).hasJsonPathMapValue("criteria");
    }

    @Test
    public void shouldSerializeWithNullCriteria() throws IOException {
        var written = tester.write(OwlPropertyBinding.get(
                new OWLAnnotationPropertyImpl(IRI.create("http://example.org/p")), null
        ));
        assertThat(written).hasJsonPathMapValue("property");
        assertThat(written).doesNotHaveJsonPath("criteria");
    }
}

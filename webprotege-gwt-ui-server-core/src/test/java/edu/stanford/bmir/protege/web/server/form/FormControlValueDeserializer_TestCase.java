package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.data.EntityFormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.IriFormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.LiteralFormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FormControlValueDeserializer_TestCase {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        var objectMapperProvider = new ObjectMapperProvider();
        objectMapper = objectMapperProvider.get();

    }

    @Test
    public void shouldRoundTripIri() throws IOException {
        var value = IriFormControlData.get(MockingUtils.mockIRI());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }

    @Test
    public void shouldRoundTripLiteral() throws IOException {
        var value = LiteralFormControlData.get(MockingUtils.mockLiteral());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }

    @Test
    public void shouldRoundTripOwlClass() throws IOException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLClass());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }

    @Test
    public void shouldRoundTripOwlObjectProperty() throws IOException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLObjectProperty());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }

    @Test
    public void shouldRoundTripOwlDataProperty() throws IOException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLDataProperty());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }

    @Test
    public void shouldRoundTripOwlAnnotationProperty() throws IOException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLAnnotationProperty());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }

    @Test
    public void shouldRoundTripOwlNamedIndividual() throws IOException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLNamedIndividual());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }

    @Test
    public void shouldRoundTripOwlDatatype() throws IOException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLDatatype());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }
}
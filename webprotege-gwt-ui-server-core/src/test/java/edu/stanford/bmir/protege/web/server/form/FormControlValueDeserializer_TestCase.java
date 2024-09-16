package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.data.EntityFormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.IriFormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.LiteralFormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.*;

import java.io.IOException;
import java.io.StringReader;

import static org.hamcrest.Matchers.is;

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
    public void shouldDeserializeOwlClass() throws IOException {
        var json =  "{\"@type\":\"owl:Class\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLClass.class);
    }

    @Test
    public void shouldDeserializeClass() throws IOException {
        var json =  "{\"@type\":\"Class\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLClass.class);
    }

    @Test
    public void shouldRoundTripOwlObjectProperty() throws IOException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLObjectProperty());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }



    @Test
    public void shouldDeserializeOwlObjectProperty() throws IOException {
        var json =  "{\"@type\":\"owl:ObjectProperty\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLObjectProperty.class);
    }

    @Test
    public void shouldDeserializeObjectProperty() throws IOException {
        var json =  "{\"@type\":\"ObjectProperty\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLObjectProperty.class);
    }


    @Test
    public void shouldRoundTripOwlDataProperty() throws IOException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLDataProperty());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }

    @Test
    public void shouldDeserializeOwlDatatypeProperty() throws IOException {
        var json =  "{\"@type\":\"owl:DatatypeProperty\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLDataProperty.class);
    }

    @Test
    public void shouldDeserializeDatatypeProperty() throws IOException {
        var json =  "{\"@type\":\"DatatypeProperty\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLDataProperty.class);
    }

    @Test
    public void shouldDeserializeDataProperty() throws IOException {
        var json =  "{\"@type\":\"DataProperty\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLDataProperty.class);
    }

    @Test
    public void shouldRoundTripOwlAnnotationProperty() throws IOException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLAnnotationProperty());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }

    @Test
    public void shouldDeserializeOwlAnnotationProperty() throws IOException {
        var json =  "{\"@type\":\"owl:AnnotationProperty\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLAnnotationProperty.class);
    }

    @Test
    public void shouldDeserializeAnnotationProperty() throws IOException {
        var json =  "{\"@type\":\"AnnotationProperty\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLAnnotationProperty.class);
    }

    @Test
    public void shouldRoundTripOwlNamedIndividual() throws IOException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLNamedIndividual());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }

    @Test
    public void shouldDeserializeOwlNamedIndividual() throws IOException {
        var json =  "{\"@type\":\"owl:NamedIndividual\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLNamedIndividual.class);
    }

    @Test
    public void shouldDeserializeNamedIndividual() throws IOException {
        var json =  "{\"@type\":\"NamedIndividual\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLNamedIndividual.class);
    }

    @Test
    public void shouldRoundTripOwlDatatype() throws IOException {
        var value = EntityFormControlData.get(MockingUtils.mockOWLDatatype());
        JsonSerializationTestUtil.testSerialization(value, PrimitiveFormControlData.class);
    }

    @Test
    public void shouldDeserializeRdfsDatatype() throws IOException {
        var json =  "{\"@type\":\"rdfs:Datatype\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLDatatype.class);
    }

    @Test
    public void shouldDeserializeDatatype() throws IOException {
        var json =  "{\"@type\":\"Datatype\",\"iri\":\"http://stuff.com/I1\"}";
        var value = objectMapper.readValue(new StringReader(json), PrimitiveFormControlData.class);
        Assertions.assertThat(value.asEntity()).containsInstanceOf(OWLDatatype.class);
    }
}
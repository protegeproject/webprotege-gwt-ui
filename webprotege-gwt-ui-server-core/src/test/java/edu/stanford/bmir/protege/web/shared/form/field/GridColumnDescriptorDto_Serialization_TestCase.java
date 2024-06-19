package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;

import java.io.*;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-18
 */
public class GridColumnDescriptorDto_Serialization_TestCase {

    @Test
    public void shouldRoundTrip() throws IOException {
        GridColumnDescriptorDto dto = GridColumnDescriptorDto.get(
                GridColumnId.get(UUID.randomUUID().toString()),
                Optionality.REQUIRED,
                Repeatability.NON_REPEATABLE,
                OwlPropertyBinding.get(MockingUtils.mockOWLObjectProperty()), LanguageMap.of("en", "test"),
                TextControlDescriptorDto.get(
                        TextControlDescriptor.getDefault()
                )
        );
        JsonSerializationTestUtil.testSerialization(dto, GridColumnDescriptorDto.class);
    }

    @Test
    public void shouldDeserialize() throws IOException {
        ObjectMapperProvider mapperProvider = new ObjectMapperProvider();
        ObjectMapper mapper = mapperProvider.get();
        GridColumnDescriptorDto deserializedObject = mapper.readValue(new StringReader(SERIALIZATION), GridColumnDescriptorDto.class);
        assertEquals(deserializedObject.getId().getId(), "66bf9c2b-d50c-43d9-adbd-61b94151750d");
    }

    private static final String SERIALIZATION = "{\t\t\t\"id\": {\n" + "\t\t\t\t\"type\": \"GridColumnId\",\n" + "\t\t\t\t\"id\": \"66bf9c2b-d50c-43d9-adbd-61b94151750d\"\n" + "\t\t\t},\n" + "\t\t\t\"optionality\": \"REQUIRED\",\n" + "\t\t\t\"repeatability\": \"NON_REPEATABLE\",\n" + "\t\t\t\"label\": {\n" + "\t\t\t\t\"en\": \"Label\"\n" + "\t\t\t},\n" + "\t\t\t\"formControlDescriptor\": {\n" + "\t\t\t\t\"type\": \"TextControlDescriptorDto\",\n" + "\t\t\t\t\"descriptor\": {\n" + "\t\t\t\t\t\"type\": \"TEXT\",\n" + "\t\t\t\t\t\"placeholder\": {\n" + "\t\t\t\t\t},\n" + "\t\t\t\t\t\"stringType\": \"SIMPLE_STRING\",\n" + "\t\t\t\t\t\"lineMode\": \"SINGLE_LINE\",\n" + "\t\t\t\t\t\"pattern\": \"\",\n" + "\t\t\t\t\t\"patternViolationErrorMessage\": {\n" + "\t\t\t\t\t},\n" + "\t\t\t\t\t\"specificLangTag\": \"\"\n" + "\t\t\t\t}\n" + "\t\t\t},\n" + "\t\t\t\"owlBinding\": {\n" + "\t\t\t\t\"type\": \"PROPERTY\",\n" + "\t\t\t\t\"property\": {\n" + "\t\t\t\t\t\"@type\": \"AnnotationProperty\",\n" + "\t\t\t\t\t\"iri\": \"http://who.int/icd#label\"\n" + "\t\t\t\t},\n" + "\t\t\t\t\"valuesCriteria\": {\n" + "\t\t\t\t\t\"match\": \"CompositeCriteria\",\n" + "\t\t\t\t\t\"multiMatchType\": \"ALL\",\n" + "\t\t\t\t\t\"criteria\": [\n" + "\t\t\t\t\t\t{\n" + "\t\t\t\t\t\t\t\"match\": \"AnyValue\"\n" + "\t\t\t\t\t\t}\n" + "\t\t\t\t\t]\n" + "\t\t\t\t}\n" + "\t\t\t}\n" + "\t\t}";
}

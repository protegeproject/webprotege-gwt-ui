package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.junit.*;
import org.semanticweb.owlapi.model.EntityType;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.ResolvableType;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-18
 */
public class GridControlDescriptor_Serialization_TestCase {

    JacksonTester<FormControlDescriptor> tester;

    @Before
    public void setUp() throws Exception {
        tester = new JacksonTester<>(GridControlDescriptor_Serialization_TestCase.class,
                                     ResolvableType.forClass(GridControlDescriptor.class),
                                     new ObjectMapperProvider().get());

    }

    @Test
    public void shouldDeserialize() throws IOException {
        var resourceAsStream = GridControlDescriptor_Serialization_TestCase.class.getResourceAsStream(
                "/forms/GridControlDescriptor.json");
        var read = tester.read(resourceAsStream);
        assertThat(read.getObject()).isInstanceOf(GridControlDescriptor.class);
        var obj = (GridControlDescriptor) read.getObject();
        assertThat(obj.getSubjectFactoryDescriptor()).isPresent();
        assertThat(obj.getColumns()).hasSize(1);
    }

    @Test
    public void shouldSerialize() throws IOException {
        var written = tester.write(GridControlDescriptor.get(ImmutableList.of(
                GridColumnDescriptor.get(
                        FormRegionId.get(UUID.randomUUID().toString()),
                        Optionality.OPTIONAL,
                        Repeatability.NON_REPEATABLE,
                        null,
                        LanguageMap.empty(),
                        EntityNameControlDescriptor.getDefault()
                )
        ), 33, FormSubjectFactoryDescriptor.get(EntityType.CLASS,
                                            null, Optional.empty())));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathArrayValue("columns");
        assertThat(written).hasJsonPath("subjectFactory");
        assertThat(written).hasJsonPath("pageSize");
    }
}

package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-07-03
 */
public class GridRowDataDto_Serialization_TestCase {

    private JacksonTester<GridRowDataDto> tester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test
    public void shouldSerialize() throws IOException {
        var data = GridRowDataDto.get(ImmutableList.of());
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathArrayValue("cells");
        assertThat(written).hasJsonPath("subject");
    }

    @Test
    public void shouldDeserialize() throws IOException {
        var read = tester.read(getClass().getResourceAsStream("/forms/GridRowDataDto.json"));
    }
}

package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.collect.*;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.FilterState;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
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
public class GridControlDataDto_Serialization_TestCase {

    private JacksonTester<FormControlDataDto> tester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test
    public void shouldSerialize() throws IOException {
        var data = GridControlDataDto.get(GridControlDescriptor.get(ImmutableList.of(), null), Page.emptyPage(),
                                          ImmutableSet.of(),
                                          3,
                                          FilterState.FILTERED);
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "GridControlDataDto");
        assertThat(written).hasJsonPathValue("control");
        assertThat(written).hasJsonPathValue("rows");
        assertThat(written).hasJsonPathValue("ordering");
        assertThat(written).hasJsonPathValue("depth");
        assertThat(written).hasJsonPathValue("filterState");
    }

    @Test
    public void shouldDeserialize() throws IOException {
        tester.read(getClass().getResourceAsStream("/forms/GridControlDataDto.json"));
    }
}

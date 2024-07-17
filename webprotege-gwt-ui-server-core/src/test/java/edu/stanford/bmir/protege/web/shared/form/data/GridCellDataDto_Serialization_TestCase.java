package edu.stanford.bmir.protege.web.shared.form.data;

import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.FilterState;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.junit.*;
import org.springframework.boot.test.json.JacksonTester;

import java.io.*;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-07-03
 */
public class GridCellDataDto_Serialization_TestCase {

    JacksonTester<GridCellDataDto> tester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test
    public void shouldSerialize() throws IOException {
        var written = tester.write(GridCellDataDto.get(
                FormRegionId.get(UUID.randomUUID().toString()),
                Page.emptyPage(),
                FilterState.FILTERED
        ));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("columnId");
        assertThat(written).hasJsonPathValue("values");
        assertThat(written).hasJsonPathValue("filterState");
    }

    @Test
    public void shouldDeserialize() throws IOException {
        var read = tester.read(getClass().getResourceAsStream("/forms/GridCellDataDto.json"));
        var obj = read.getObject();
        assertThat(obj.getColumnId().getId()).isEqualTo("a60a9e62-367f-4441-8dda-6c1554447e0d");
        assertThat(obj.getFilterState()).isEqualTo(FilterState.FILTERED);
    }
}

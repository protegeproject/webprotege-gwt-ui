package edu.stanford.bmir.protege.web.shared.form.field;

import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.*;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FormRegionOrdering_Serialization_TestCase {

    JacksonTester<FormRegionOrdering> tester;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test
    public void shouldSerializeOrderBy() throws IOException {
        var orderBy = FormRegionOrdering.get(FormRegionId.get("12345678-1234-1234-1234-123456789abc"),
                                             FormRegionOrderingDirection.DESC);
        var written = tester.write(orderBy);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("regionId");
        assertThat(written).hasJsonPathValue("direction");
    }

    @Test
    public void shouldDeserialize() throws IOException {
        var read = tester.read(getClass().getResourceAsStream("/forms/FormRegionOrdering.json"));
        assertThat(read.getObject().getDirection()).isEqualTo(FormRegionOrderingDirection.DESC);
        assertThat(read.getObject().getRegionId().getId()).isEqualTo("12345678-1234-1234-1234-123456789abc");
    }
}

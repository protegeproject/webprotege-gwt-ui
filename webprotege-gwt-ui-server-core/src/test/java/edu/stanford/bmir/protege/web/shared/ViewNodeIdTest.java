package edu.stanford.bmir.protege.web.shared;

import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class ViewNodeIdTest {

    JacksonTester<ViewNodeId> tester;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapperProvider().get());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpe() {
        ViewNodeId.get(null);
    }

    @Test
    public void shouldDeserializeViewId() throws Exception {
        ObjectContent<ViewNodeId> parsed = tester.parse("\"87b8d691-76c8-4aa0-bbbb-2ab6487d250b\"");
        assertEquals(parsed.getObject().getId(), "87b8d691-76c8-4aa0-bbbb-2ab6487d250b");
    }

    @Test
    public void shouldSerializeViewId() throws Exception {
        JsonContent<ViewNodeId> written = tester.write(ViewNodeId.get("69791d2d-920f-4fd7-beab-9bcd1dee7228"));
        assertThat(written.getJson()).isEqualTo("\"69791d2d-920f-4fd7-beab-9bcd1dee7228\"");
    }
}
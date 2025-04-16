package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicCapabilityTest {


    private JacksonTester<Capability> tester;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void shouldDeserializeAndSerialize() throws IOException {
        String json = "{\n" +
                      "                    \"@type\" : \"BasicCapability\",\n" +
                      "                    \"id\" : \"PerformActionX\"" +
                      "                }";
        var parsed = tester.parse(json);
        assertThat(parsed).isEqualTo(BasicCapability.valueOf("PerformActionX"));

        var written = tester.write(parsed.getObject());
        assertThat(written).extractingJsonPathStringValue("['@type']").isEqualTo("BasicCapability");
        assertThat(written).extractingJsonPathStringValue("['id']").isEqualTo("PerformActionX");
    }
}

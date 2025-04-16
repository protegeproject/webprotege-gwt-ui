package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericParameterizedCapabilityTest {

    private JacksonTester<Capability> tester;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void shouldDeserializeAndSerialize() throws IOException {
        String json = "{\n" +
                      "                    \"@type\" : \"OtherTypeNotRecognized\",\n" +
                      "                    \"id\" : \"PerformActionX\",\n" +
                      "                    \"otherProperty\" : \"OtherValue\"\n" +
                      "                }";
        var parsed = tester.parse(json);
        assertThat(parsed).isEqualTo(GenericParameterizedCapability.get("OtherTypeNotRecognized", "PerformActionX", Map.of("otherProperty", "OtherValue")));

        var written = tester.write(parsed.getObject());
        assertThat(written).extractingJsonPathStringValue("['@type']").isEqualTo("OtherTypeNotRecognized");
        assertThat(written).extractingJsonPathStringValue("['id']").isEqualTo("PerformActionX");
        assertThat(written).extractingJsonPathStringValue("['otherProperty']").isEqualTo("OtherValue");
    }
}
package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Feb 2018
 */
public class BasicCapability_Json_TestCase {

    private static final String ID = "TheCapability";

    private BasicCapability basicCapability;

    @Before
    public void setUp() throws Exception {
        basicCapability = new BasicCapability(ID);
    }

    @Test
    public void shouldSerializeJson() throws Exception {
        String result = new ObjectMapper().writeValueAsString(basicCapability);
        result = result.replaceAll("\\s", "");
        assertThat(result, is("{\"@type\":\"BasicCapability\",\"id\":\"TheCapability\"}"));
    }

    @Test
    public void shouldDeserializeJson() throws Exception {
        BasicCapability readBasicCapability = new ObjectMapper()
                .readerFor(BasicCapability.class)
                .readValue("{\"@type\":\"BasicCapability\",\"id\":\"TheCapability\"}");
        assertThat(readBasicCapability, is(basicCapability));
    }
}

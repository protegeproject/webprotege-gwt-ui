package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetAuthenticatedUserDetailsResult;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class JsonRpcResponse_Test {


    @Test
    public void shouldDeserailzeResponse() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapperProvider().get();

        String json = "{ \"jsonrpc\":\"2.0\",\n" + "\t\"method\": \"webprotege.users.GetAuthenticatedUserDetails\",\n" + "\t\"result\": { \"@type\":\"webprotege.users.GetAuthenticatedUserDetails\",\n" + "\t\t\"userDetails\": {\n" + "\t\t\t\"userId\": \"guest\",\n" + "\t\t\t\"displayName\": \"Guest\",\n" + "\t\t\t\"emailAddress\": null\n" + "\t\t},\n" + "\t\t\"permittedActions\": []\n" + "\t}\n" + "}";

        JsonRpcResponse response = objectMapper.readValue(json, JsonRpcResponse.class);
        assertThat(response.getErrorInternal(), is(nullValue()));
        assertThat(response.getResultInternal(), is(not(nullValue())));


    }
}
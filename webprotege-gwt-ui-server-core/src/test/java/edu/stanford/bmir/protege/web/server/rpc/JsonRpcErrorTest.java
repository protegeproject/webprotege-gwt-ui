package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonRpcErrorTest {

    @Test
    public void shouldDeserializeError() throws JsonProcessingException {
        String json = "{\"timestamp\":\"2025-06-04T00:14:04.871+00:00\",\"status\":500,\"error\":\"Internal Server Error\",\"path\":\"/api/execute\"}";
        ObjectMapper objectMapper = new ObjectMapperProvider().get();
        JsonRpcError rpcError = objectMapper.readValue(json, JsonRpcError.class);
        assertEquals(rpcError.getMessage(), "Internal Server Error");
        assertEquals(rpcError.getCode(), 500);
    }
}
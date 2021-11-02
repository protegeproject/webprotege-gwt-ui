package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.project.GetProjectDetailsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.*;

public class JsonRpcRequest_Test {

    private JsonRpcRequest request;

    @Before
    public void setUp() throws Exception {
        var action = new GetProjectDetailsAction(ProjectId.getNil());
        request = JsonRpcRequest.create(action);
    }

    @Test
    public void shouldSerializeToJson() throws IOException {
        ObjectMapperProvider mapperProvider = new ObjectMapperProvider();
        ObjectMapper mapper = mapperProvider.get();
        Map<String, Object> object = mapper.convertValue(request, Map.class);
        assertThat(object, hasKey("method"));
        assertThat(object, hasKey("params"));
    }
}
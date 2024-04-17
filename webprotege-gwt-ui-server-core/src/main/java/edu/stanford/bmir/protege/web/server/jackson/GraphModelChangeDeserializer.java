package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import edu.stanford.protege.gwt.graphtree.shared.graph.*;

import java.io.IOException;

public class GraphModelChangeDeserializer extends StdDeserializer<GraphModelChange> {

    protected GraphModelChangeDeserializer() {
        super(GraphModelChange.class);
    }


    @Override
    public GraphModelChange deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String type = jsonParser.getValueAsString("type");
        switch (type) {
            case "AddEdge" :
                return jsonParser.readValueAs(AddEdge.class);
            case "RemoveEdge" :
                return jsonParser.readValueAs(RemoveEdge.class);
            case "AddRootNode" :
                return jsonParser.readValueAs(AddRootNode.class);
            case "RemoveRootNode" :
                return jsonParser.readValueAs(RemoveRootNode.class);
            default:
                return null;
        }
    }
}

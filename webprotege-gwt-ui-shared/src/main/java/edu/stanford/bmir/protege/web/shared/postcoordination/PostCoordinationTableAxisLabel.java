package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;

@GwtCompatible(serializable = true)
public class PostCoordinationTableAxisLabel implements IsSerializable, Serializable {

    private String postCoordinationAxis;

    private String tableLabel;

    private String scaleLabel;

    @GwtSerializationConstructor
    private PostCoordinationTableAxisLabel() {
    }



    @JsonCreator
    public PostCoordinationTableAxisLabel(@JsonProperty("postcoordinationAxis") String postCoordinationAxis, @JsonProperty("tableLabel") String tableLabel, @JsonProperty("scaleLabel") String scaleLabel) {
        this.postCoordinationAxis = postCoordinationAxis;
        this.tableLabel = tableLabel;
        this.scaleLabel = scaleLabel;
    }

    @JsonProperty("postcoordinationAxis")
    public String getPostCoordinationAxis() {
        return postCoordinationAxis;
    }

    @JsonProperty("tableLabel")
    public String getTableLabel() {
        return tableLabel;
    }

    @JsonProperty("scaleLabel")
    public String getScaleLabel() {
        return scaleLabel;
    }
}

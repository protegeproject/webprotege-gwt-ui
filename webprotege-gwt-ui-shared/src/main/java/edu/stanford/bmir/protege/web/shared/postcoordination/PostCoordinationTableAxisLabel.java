package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostCoordinationTableAxisLabel that = (PostCoordinationTableAxisLabel) o;
        return postCoordinationAxis.equals(that.postCoordinationAxis) && Objects.equals(tableLabel, that.tableLabel) && Objects.equals(scaleLabel, that.scaleLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postCoordinationAxis, tableLabel, scaleLabel);
    }
}

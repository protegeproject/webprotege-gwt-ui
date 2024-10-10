package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@GwtCompatible(serializable = true)
public class PostCoordinationSpecification implements Serializable, IsSerializable {
    private String linearizationView;

    private List<String> allowedAxes;

    private List<String> defaultAxes;

    private List<String> notAllowedAxes;

    private List<String> requiredAxes;

    @JsonCreator
    public PostCoordinationSpecification(@JsonProperty("linearizationView") String linearizationView,
                                         @JsonProperty("allowedAxes") List<String> allowedAxes,
                                         @JsonProperty("defaultAxes") List<String> defaultAxes,
                                         @JsonProperty("notAllowedAxes") List<String> notAllowedAxes,
                                         @JsonProperty("requiredAxes") List<String> requiredAxes) {
        this.linearizationView = linearizationView;
        this.allowedAxes = allowedAxes == null ? new ArrayList<>() : allowedAxes;
        this.defaultAxes = defaultAxes == null ? new ArrayList<>() : defaultAxes;
        this.notAllowedAxes = notAllowedAxes == null ? new ArrayList<>() : notAllowedAxes;
        this.requiredAxes = requiredAxes == null ? new ArrayList<>() : requiredAxes;
    }
    @GwtSerializationConstructor
    private PostCoordinationSpecification() {
    }

    public String getLinearizationView() {
        return linearizationView;
    }

    public List<String> getAllowedAxes() {
        return allowedAxes;
    }

    public List<String> getDefaultAxes() {
        return defaultAxes;
    }

    public List<String> getNotAllowedAxes() {
        return notAllowedAxes;
    }

    public List<String> getRequiredAxes() {
        return requiredAxes;
    }

    @Override
    public String toString() {
        return "PostCoordinationSpecification{" +
                "linearizationView='" + linearizationView + '\'' +
                ", allowedAxes=" + allowedAxes +
                ", defaultAxes=" + defaultAxes +
                ", notAllowedAxes=" + notAllowedAxes +
                ", requiredAxes=" + requiredAxes +
                '}';
    }
}

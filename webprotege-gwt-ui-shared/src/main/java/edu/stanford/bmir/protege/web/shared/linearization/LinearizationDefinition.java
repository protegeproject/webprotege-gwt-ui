package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;

@GwtCompatible(serializable = true)
public class LinearizationDefinition implements IsSerializable, Serializable {
    private String linearizationId;

    private String linearizationUri;

    private String linearizationMode;

    private String rootId;


    private String coreLinId;

    private String sortingCode;

    private String displayLabel;

    @GwtSerializationConstructor
    private LinearizationDefinition() {
    }

    @JsonCreator
    public LinearizationDefinition(@JsonProperty("linearizationId") String linearizationId,
                                   @JsonProperty("linearizationUri") String linearizationUri,
                                   @JsonProperty("linearizationMode") String linearizationMode,
                                   @JsonProperty("rootId") String rootId,
                                   @JsonProperty("displayLabel") String displayLabel,
                                   @JsonProperty("coreLinId") String coreLinId,
                                   @JsonProperty("sortingCode") String sortingCode) {
        this.linearizationId = linearizationId;
        this.linearizationUri = linearizationUri;
        this.linearizationMode = linearizationMode;
        this.rootId = rootId;
        this.coreLinId = coreLinId;
        this.sortingCode = sortingCode;
        this.displayLabel = displayLabel;
    }

    public String getLinearizationId() {
        return linearizationId;
    }

    public String getLinearizationUri() {
        return linearizationUri;
    }

    public String getLinearizationMode() {
        return linearizationMode;
    }

    public String getRootId() {
        return rootId;
    }

    public String getCoreLinId() {
        return coreLinId;
    }

    public String getSortingCode() {
        return sortingCode;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    @Override
    public String toString() {
        return "LinearizationDefinition{" +
                "linearizationId='" + linearizationId + '\'' +
                ", linearizationUri='" + linearizationUri + '\'' +
                ", linearizationMode='" + linearizationMode + '\'' +
                ", rootId='" + rootId + '\'' +
                ", coreLinId='" + coreLinId + '\'' +
                ", sortingCode='" + sortingCode + '\'' +
                '}';
    }
}

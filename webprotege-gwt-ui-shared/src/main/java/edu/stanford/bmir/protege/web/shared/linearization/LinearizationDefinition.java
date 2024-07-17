package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;

@GwtCompatible(serializable = true)
public class LinearizationDefinition implements IsSerializable, Serializable {
    private String id;

    private String whoficEntityIri;

    private String linearizationMode;

    private String rootId;


    private String coreLinId;

    private String sortingCode;

    @GwtSerializationConstructor
    private LinearizationDefinition() {
    }

    @JsonCreator
    public LinearizationDefinition(@JsonProperty("Id") String id,
                                   @JsonProperty("whoficEntityIri") String whoficEntityIri,
                                   @JsonProperty("linearizationMode") String linearizationMode,
                                   @JsonProperty("rootId") String rootId,
                                   @JsonProperty("coreLinId") String coreLinId,
                                   @JsonProperty("sortingCode") String sortingCode) {
        this.id = id;
        this.whoficEntityIri = whoficEntityIri;
        this.linearizationMode = linearizationMode;
        this.rootId = rootId;
        this.coreLinId = coreLinId;
        this.sortingCode = sortingCode;
    }

    public String getId() {
        return id;
    }

    public String getWhoficEntityIri() {
        return whoficEntityIri;
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

    @Override
    public String toString() {
        return "LinearizationDefinition{" +
                "id='" + id + '\'' +
                ", whoficEntityIri='" + whoficEntityIri + '\'' +
                ", linearizationMode='" + linearizationMode + '\'' +
                ", rootId='" + rootId + '\'' +
                ", coreLinId='" + coreLinId + '\'' +
                ", sortingCode='" + sortingCode + '\'' +
                '}';
    }
}

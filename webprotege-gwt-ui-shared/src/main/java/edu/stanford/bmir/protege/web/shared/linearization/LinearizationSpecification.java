package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;


import javax.annotation.Nonnull;

import java.io.Serializable;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;


@GwtCompatible(serializable = true)
public class LinearizationSpecification implements Serializable, IsSerializable {

    private String isAuxiliaryAxisChild;
    private String isGrouping;
    private String isIncludedInLinearization;
    private String linearizationParent;
    private String linearizationView;
    private String sortingLabel;
    private String codingNote;

    @GwtSerializationConstructor
    private LinearizationSpecification() {
    }

    @JsonCreator
    public LinearizationSpecification(@JsonProperty("isAuxiliaryAxisChild") String isAuxiliaryAxisChild,
                                      @JsonProperty("isGrouping") String isGrouping,
                                      @JsonProperty("isIncludedInLinearization") String isIncludedInLinearization,
                                      @JsonProperty("linearizationParent") String linearizationParent,
                                      @JsonProperty("sortingLabel") String sortingLabel,
                                      @JsonProperty("linearizationView") @Nonnull String linearizationView,
                                      @JsonProperty("codingNote") String codingNote) {
        this.isAuxiliaryAxisChild = isAuxiliaryAxisChild;
        this.isGrouping = isGrouping;
        this.isIncludedInLinearization = isIncludedInLinearization;
        this.linearizationParent = linearizationParent;
        this.sortingLabel = sortingLabel;
        this.linearizationView = checkNotNull(linearizationView);
        this.codingNote = codingNote;
    }

    public String getIsAuxiliaryAxisChild() {
        return isAuxiliaryAxisChild;
    }

    public String getIsGrouping() {
        return isGrouping;
    }

    public String getIsIncludedInLinearization() {
        return isIncludedInLinearization;
    }

    public String getLinearizationParent() {
        return linearizationParent;
    }

    public String getLinearizationView() {
        return linearizationView;
    }

    public String getCodingNote() {
        return codingNote;
    }

    public String getSortingLabel() {
        return sortingLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinearizationSpecification)) return false;
        LinearizationSpecification that = (LinearizationSpecification) o;
        return Objects.equals(isAuxiliaryAxisChild, that.isAuxiliaryAxisChild) &&
                Objects.equals(isGrouping, that.isGrouping) &&
                Objects.equals(isIncludedInLinearization, that.isIncludedInLinearization) &&
                Objects.equals(linearizationParent, that.linearizationParent) &&
                Objects.equals(linearizationView, that.linearizationView) &&
                Objects.equals(sortingLabel, that.sortingLabel) &&
                Objects.equals(codingNote, that.codingNote);
    }

    @Override
    public String toString() {
        return "LinearizationSpecification{" +
                "isAuxiliaryAxisChild='" + isAuxiliaryAxisChild + '\'' +
                ", isGrouping='" + isGrouping + '\'' +
                ", isIncludedInLinearization='" + isIncludedInLinearization + '\'' +
                ", linearizationParent='" + linearizationParent + '\'' +
                ", linearizationView='" + linearizationView + '\'' +
                ", sortingLabel='" + sortingLabel + '\'' +
                ", codingNote='" + codingNote + '\'' +
                '}';
    }
}

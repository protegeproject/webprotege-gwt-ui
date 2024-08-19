package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;

@GwtCompatible(serializable = true)
public class LinearizationResiduals implements Serializable, IsSerializable {

    private String suppressOtherSpecifiedResiduals;
    private String unspecifiedResidualTitle;
    private String otherSpecifiedResidualTitle;
    private String suppressUnspecifiedResiduals;


    @GwtSerializationConstructor
    private LinearizationResiduals() {
    }

    @JsonCreator
    public LinearizationResiduals( @JsonProperty("suppressOtherSpecifiedResiduals") String suppressOtherSpecifiedResiduals,
                                   @JsonProperty("suppressUnspecifiedResiduals") String suppressUnspecifiedResiduals,
                                   @JsonProperty("otherSpecifiedResidualTitle") String otherSpecifiedResidualTitle,
                                   @JsonProperty("unspecifiedResidualTitle") String unspecifiedResidualTitle) {
        this.suppressOtherSpecifiedResiduals = suppressOtherSpecifiedResiduals;
        this.suppressUnspecifiedResiduals = suppressUnspecifiedResiduals;
        this.unspecifiedResidualTitle = unspecifiedResidualTitle;
        this.otherSpecifiedResidualTitle = otherSpecifiedResidualTitle;
    }


    @JsonProperty("suppressOtherSpecifiedResiduals")
    public String getSuppressedOtherSpecifiedResiduals() {
        return suppressOtherSpecifiedResiduals;
    }
    @JsonProperty("unspecifiedResidualTitle")
    public String getUnspecifiedResidualTitle() {
        return unspecifiedResidualTitle;
    }

    @JsonProperty("otherSpecifiedResidualTitle")
    public String getOtherSpecifiedResidualTitle() {
        return otherSpecifiedResidualTitle;
    }

    @JsonProperty("suppressUnspecifiedResiduals")
    public String getSuppressUnspecifiedResiduals() {
        return suppressUnspecifiedResiduals;
    }

    @Override
    public String toString() {
        return "LinearizationResiduals{" +
                "suppressSpecifiedResidual='" + suppressOtherSpecifiedResiduals + '\'' +
                ", unspecifiedResidualTitle='" + unspecifiedResidualTitle + '\'' +
                '}';
    }
}

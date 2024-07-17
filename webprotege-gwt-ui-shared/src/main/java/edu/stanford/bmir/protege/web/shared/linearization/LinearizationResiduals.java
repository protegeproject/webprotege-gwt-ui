package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;

@GwtCompatible(serializable = true)
public class LinearizationResiduals implements Serializable, IsSerializable {

    private String suppressSpecifiedResidual;
    private String unspecifiedResidualTitle;

    @GwtSerializationConstructor
    private LinearizationResiduals() {
    }

    @JsonCreator
    public LinearizationResiduals( @JsonProperty("suppressOtherSpecifiedResiduals") String suppressSpecifiedResidual,
                                   @JsonProperty("unspecifiedResidualTitle") String unspecifiedResidualTitle) {
        this.suppressSpecifiedResidual = suppressSpecifiedResidual;
        this.unspecifiedResidualTitle = unspecifiedResidualTitle;
    }


    public String getSuppressSpecifiedResidual() {
        return suppressSpecifiedResidual;
    }

    public String getUnspecifiedResidualTitle() {
        return unspecifiedResidualTitle;
    }

    @Override
    public String toString() {
        return "LinearizationResiduals{" +
                "suppressSpecifiedResidual='" + suppressSpecifiedResidual + '\'' +
                ", unspecifiedResidualTitle='" + unspecifiedResidualTitle + '\'' +
                '}';
    }
}

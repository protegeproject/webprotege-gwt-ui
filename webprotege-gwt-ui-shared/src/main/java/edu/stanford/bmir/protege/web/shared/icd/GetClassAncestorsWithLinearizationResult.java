package edu.stanford.bmir.protege.web.shared.icd;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import java.util.List;

import static edu.stanford.bmir.protege.web.shared.icd.GetClassAncestorsWithLinearizationAction.CHANNEL;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class GetClassAncestorsWithLinearizationResult implements Result {

    @JsonProperty("ancestorsWithLinearization")
    public abstract List<OWLEntityData> getAncestorsWithLinearization();

    @JsonCreator
    public static GetClassAncestorsWithLinearizationResult create(@JsonProperty("ancestorsWithLinearization")  List<OWLEntityData> ancestorsWithLinearization ) {
        return new AutoValue_GetClassAncestorsWithLinearizationResult(ancestorsWithLinearization);
    }
}

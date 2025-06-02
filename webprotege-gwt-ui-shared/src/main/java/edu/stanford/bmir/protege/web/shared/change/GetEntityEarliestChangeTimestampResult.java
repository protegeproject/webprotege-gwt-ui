package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@JsonTypeName("webprotege.history.GetEntityEarliestChangeTimestamp")
@GwtCompatible(serializable = true)
public abstract class GetEntityEarliestChangeTimestampResult 
        implements Result {

    @JsonCreator
    public static GetEntityEarliestChangeTimestampResult create(
            @JsonProperty("earliestTimestamp") Long earliestTimestamp) {
        return new AutoValue_GetEntityEarliestChangeTimestampResult(earliestTimestamp);
    }

    @JsonProperty("earliestTimestamp")
    public abstract Long getEarliestTimestamp();
}

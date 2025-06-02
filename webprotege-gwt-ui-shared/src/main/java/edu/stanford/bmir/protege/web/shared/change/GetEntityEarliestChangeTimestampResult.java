package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@JsonTypeName("webprotege.history.GetEntityEarliestChangeTimestamp")
public abstract class GetEntityEarliestChangeTimestampResult 
        implements Result {

    @JsonCreator
    public static GetEntityEarliestChangeTimestampResult create(
            @JsonProperty("earliestTimestamp") Long earliestTimestamp) {
        return new AutoValue_GetEntityEarliestChangeTimestampResult(earliestTimestamp);
    }

    public abstract Long getEarliestTimestamp();
}

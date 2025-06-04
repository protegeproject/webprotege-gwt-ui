package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nullable;

@AutoValue
@JsonTypeName("webprotege.history.GetEntityEarliestChangeTimestamp")
@GwtCompatible(serializable = true)
public abstract class GetEntityEarliestChangeTimestampResult
        implements Result {

    @JsonCreator
    public static GetEntityEarliestChangeTimestampResult create(
            @JsonProperty("earliestTimestamp") @Nullable Long earliestTimestamp) {
        return new AutoValue_GetEntityEarliestChangeTimestampResult(earliestTimestamp);
    }

    @JsonProperty("earliestTimestamp")
    @Nullable
    public abstract Long getEarliestTimestamp();
}

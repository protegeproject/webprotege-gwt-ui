package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@JsonTypeName("webprotege.comments.GetEntityEarliestCommentTimestamp")
@GwtCompatible(serializable = true)
public abstract class GetEntityEarliestCommentTimestampResult
        implements Result {

    @JsonCreator
    public static GetEntityEarliestCommentTimestampResult create(
            @JsonProperty("earliestTimestamp") Long earliestTimestamp) {
        return new AutoValue_GetEntityEarliestCommentTimestampResult(earliestTimestamp);
    }

    @JsonProperty("earliestTimestamp")
    public abstract Long getEarliestTimestamp();
}

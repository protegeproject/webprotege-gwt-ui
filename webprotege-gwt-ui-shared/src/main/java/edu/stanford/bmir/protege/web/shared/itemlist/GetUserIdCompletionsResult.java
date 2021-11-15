package edu.stanford.bmir.protege.web.shared.itemlist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
@JsonTypeName("webprotege.users.GetUserIdCompletions")
public class GetUserIdCompletionsResult extends GetPossibleItemCompletionsResult<UserId> {

    /**
     * For serialization only
     */
    private GetUserIdCompletionsResult() {
    }

    private GetUserIdCompletionsResult(List<UserId> possibleItemCompletions) {
        super(possibleItemCompletions);
    }

    @JsonCreator
    public static GetUserIdCompletionsResult create(@JsonProperty("completions") List<UserId> completions) {
        return new GetUserIdCompletionsResult(completions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPossibleItemCompletions());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetUserIdCompletionsResult)) {
            return false;
        }
        GetUserIdCompletionsResult other = (GetUserIdCompletionsResult) obj;
        return this.getPossibleItemCompletions().equals(other.getPossibleItemCompletions());
    }


    @Override
    public String toString() {
        return toStringHelper("GetUserIdCompletionsResult")
                .addValue(getPossibleItemCompletions())
                .toString();
    }
}

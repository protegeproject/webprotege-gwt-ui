package edu.stanford.bmir.protege.web.shared.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 *
 * Wire-conforms to the backend's webprotege.watches.SetWatches result, which
 * carries no fields.
 */
@JsonTypeName("webprotege.watches.SetWatches")
public class SetEntityWatchesResult implements Result {

    @GwtSerializationConstructor
    private SetEntityWatchesResult() {
    }

    @JsonCreator
    public static SetEntityWatchesResult create() {
        return new SetEntityWatchesResult();
    }

    @Override
    public int hashCode() {
        return SetEntityWatchesResult.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SetEntityWatchesResult;
    }

    @Override
    public String toString() {
        return "SetEntityWatchesResult{}";
    }
}

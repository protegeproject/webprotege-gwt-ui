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
}

package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2018
 */
@JsonTypeName("webprotege.entities.MergeEntities")
public class MergeEntitiesResult implements Result {


    private MergeEntitiesResult() {
    }

    public static MergeEntitiesResult create() {
        return new MergeEntitiesResult();
    }

    @Override
    public int hashCode() {
        return 33;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MergeEntitiesResult)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return toStringHelper("MergeEntitiesResult")
                .toString();
    }
}

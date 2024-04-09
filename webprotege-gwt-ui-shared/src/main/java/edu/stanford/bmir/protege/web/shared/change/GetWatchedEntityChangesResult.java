package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
@JsonTypeName("webprotege.watches.GetWatchedEntities")
public class GetWatchedEntityChangesResult implements Result, HasProjectChanges {

    private Page<ProjectChange> changes;

    private GetWatchedEntityChangesResult() {
    }

    private GetWatchedEntityChangesResult(Page<ProjectChange> changes) {
        this.changes = checkNotNull(changes);
    }

    @JsonCreator
    public static GetWatchedEntityChangesResult create(@JsonProperty("projectChanges") Page<ProjectChange> changes) {
        return new GetWatchedEntityChangesResult(changes);
    }

    @Nonnull
    public Page<ProjectChange> getProjectChanges() {
        return changes;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(changes);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetWatchedEntityChangesResult)) {
            return false;
        }
        GetWatchedEntityChangesResult other = (GetWatchedEntityChangesResult) obj;
        return this.changes.equals(other.changes);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetWatchedEntityChangesResult")
                          .addValue(changes)
                          .toString();
    }
}

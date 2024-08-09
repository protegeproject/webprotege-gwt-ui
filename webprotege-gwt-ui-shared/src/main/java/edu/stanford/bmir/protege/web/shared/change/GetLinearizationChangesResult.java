package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.linearization.GetLinearizationChanges")
public abstract class GetLinearizationChangesResult implements Result, HasProjectChanges {

    @JsonCreator
    public static GetLinearizationChangesResult create(@JsonProperty("projectChanges") Page<ProjectChange> projectChanges) {
        return new AutoValue_GetLinearizationChangesResult(projectChanges);
    }


    @Nonnull
    @Override
    public abstract Page<ProjectChange> getProjectChanges();
}

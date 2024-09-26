package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

import static edu.stanford.bmir.protege.web.shared.change.GetProjectChangesForHistoryViewAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class GetProjectChangesForHistoryViewResult implements Result, HasProjectChanges {

    private Page<ProjectChange> projectChanges;

    @JsonCreator
    public static GetProjectChangesForHistoryViewResult create(@JsonProperty("projectChanges") Page<ProjectChange> projectChanges) {
        return new AutoValue_GetProjectChangesForHistoryViewResult(projectChanges);
    }

    @Nonnull
    @Override
    @JsonProperty("projectChanges")
    public abstract Page<ProjectChange> getProjectChanges();
}

package edu.stanford.bmir.protege.web.shared.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("AddWatch")
public abstract class AddWatchAction implements ProjectAction<AddWatchResult>, HasUserId {

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Override
    public abstract UserId getUserId();

    @Nonnull
    public abstract Watch getWatch();

    @JsonCreator
    public static AddWatchAction create(@JsonProperty("projectId") ProjectId newProjectId,
                                        @JsonProperty("userId") UserId newUserId,
                                        @JsonProperty("watch") Watch newWatch) {
        return new AutoValue_AddWatchAction(newProjectId, newUserId, newWatch);
    }
}

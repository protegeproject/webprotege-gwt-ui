package edu.stanford.bmir.protege.web.shared.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.cards.GetEntityCardDescriptors")
public abstract class GetEntityCardDescriptorsResult implements Result {

    @JsonCreator
    public static GetEntityCardDescriptorsResult create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                        @JsonProperty("descriptors") @Nonnull List<CardDescriptor> descriptors,
                                                        @JsonProperty("writableCards") @Nonnull List<CardId> writableCards) {
        return new AutoValue_GetEntityCardDescriptorsResult(projectId,
                new ArrayList<>(descriptors), new ArrayList<>(writableCards));
    }

    @JsonProperty("projectId")
    @Nonnull
    public abstract ProjectId getProjectId();

    @JsonProperty("descriptors")
    @Nonnull
    public abstract List<CardDescriptor> getDescriptors();

    @JsonProperty("writableCards")
    @Nonnull
    public abstract List<CardId> getWritableCards();
}

package edu.stanford.bmir.protege.web.shared.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Mar 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.tags.SetProjectTags")
public abstract class SetProjectTagsAction implements ProjectAction<SetProjectTagsResult> {

    @GwtIncompatible
    public static SetProjectTagsAction create(@Nonnull ProjectId projectId,
                                              @Nonnull List<TagData> tagData) {
        return create(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, tagData);
    }

    @JsonCreator
    public static SetProjectTagsAction create(@JsonProperty("changeRequestId") @Nonnull ChangeRequestId changeRequestId,
                                              @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                              @JsonProperty("tagData") @Nonnull List<TagData> tagData) {
        return new AutoValue_SetProjectTagsAction(changeRequestId, projectId, tagData);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public abstract ChangeRequestId getChangeRequestId();

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract List<TagData> getTagData();
}

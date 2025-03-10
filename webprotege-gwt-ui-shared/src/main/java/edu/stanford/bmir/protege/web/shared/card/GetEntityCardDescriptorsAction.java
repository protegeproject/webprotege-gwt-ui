package edu.stanford.bmir.protege.web.shared.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.cards.GetEntityCardDescriptors")
public abstract class GetEntityCardDescriptorsAction implements ProjectAction<GetEntityCardDescriptorsResult> {

    @JsonCreator
    public static GetEntityCardDescriptorsAction get(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                     @JsonProperty("subject") @Nonnull OWLEntity subject) {
        return new AutoValue_GetEntityCardDescriptorsAction(projectId, subject);
    }

    @JsonProperty("projectId")
    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @JsonProperty("subject")
    @Nonnull
    public abstract OWLEntity getSubject();
}

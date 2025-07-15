package edu.stanford.bmir.protege.web.shared.directparents;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.GetEntityDirectParents")
public abstract class GetEntityDirectParentsAction extends AbstractHasProjectAction<GetEntityDirectParentsResult> {

    @JsonCreator
    public static GetEntityDirectParentsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                       @JsonProperty("entity") @Nonnull OWLEntity entity) {
        return new AutoValue_GetEntityDirectParentsAction(projectId, entity);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract OWLEntity getEntity();

}

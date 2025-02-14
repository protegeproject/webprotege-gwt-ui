package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.RenderedOwlEntities")
public abstract class GetRenderedOwlEntitiesAction implements ProjectAction<GetRenderedOwlEntitiesResult> {

    @JsonCreator
    public static GetRenderedOwlEntitiesAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                     @JsonProperty("entityIris") @Nonnull Set<String> entityIris) {
        return new AutoValue_GetRenderedOwlEntitiesAction(entityIris, projectId);
    }


    @JsonProperty("entityIris")
    public abstract Set<String> getEntitiesIris();

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();
}

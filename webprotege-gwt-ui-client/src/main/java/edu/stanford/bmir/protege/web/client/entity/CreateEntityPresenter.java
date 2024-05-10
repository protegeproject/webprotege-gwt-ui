package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.GetEntityCreationFormsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 5 Dec 2017
 */
public class CreateEntityPresenter {

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DefaultCreateEntitiesPresenter defaultCreateEntitiesPresenter;

    @Nonnull
    private final WhoCreateClassPresenter whoCreateClassPresenter;

    @Nonnull
    private final CreateEntityFormPresenter createEntityFormPresenter;

    private final static Logger logger = Logger.getLogger(CreateEntityPresenter.class.getName());


    @Inject
    public CreateEntityPresenter(@Nonnull DispatchServiceManager dispatch,
                                 @Nonnull ProjectId projectId,
                                 @Nonnull DefaultCreateEntitiesPresenter defaultCreateEntitiesPresenter,
                                 @Nonnull WhoCreateClassPresenter whoCreateClassPresenter, @Nonnull CreateEntityFormPresenter createEntityFormPresenter) {
        this.dispatch = checkNotNull(dispatch);
        this.projectId = checkNotNull(projectId);
        this.defaultCreateEntitiesPresenter = checkNotNull(defaultCreateEntitiesPresenter);
        this.whoCreateClassPresenter = checkNotNull(whoCreateClassPresenter);
        this.createEntityFormPresenter = checkNotNull(createEntityFormPresenter);
    }

    public void createEntities(@Nonnull EntityType<?> entityType,
                               @Nonnull Optional<? extends OWLEntity> parentEntity,
                               @Nonnull EntitiesCreatedHandler entitiesCreatedHandler) {

        parentEntity.ifPresent(owlEntity -> dispatch.execute(GetEntityCreationFormsAction.create(projectId,
                                                                                                 owlEntity,
                                                                                                 entityType), result -> {
            handleEntityCreationFormsResult(entityType,
                                            parentEntity,
                                            entitiesCreatedHandler,
                                            result.getFormDescriptors());
        }));


    }

    private void handleEntityCreationFormsResult(@Nonnull EntityType<?> entityType,
                                                 @Nonnull Optional<? extends OWLEntity> parentEntity,
                                                 @Nonnull EntitiesCreatedHandler entitiesCreatedHandler,
                                                 @Nonnull ImmutableList<FormDescriptorDto> createEntityForms) {

        if (isImmutableListNotEmpty(createEntityForms)) {

            createEntityFormPresenter.createEntities(entityType,
                    parentEntity,
                    entitiesCreatedHandler,
                    createEntityForms);

            return;
        }
        if(isEntityTypeClass(entityType)){
            whoCreateClassPresenter.createEntities(entityType,
                    parentEntity,
                    entitiesCreatedHandler);

            return;
        }

        defaultCreateEntitiesPresenter.createEntities(entityType,
                parentEntity,
                entitiesCreatedHandler);
    }

    private <T> boolean  isImmutableListNotEmpty(ImmutableList<T> listToCheck){
        return !listToCheck.isEmpty();
    }

    private boolean isEntityTypeClass(EntityType entityType){
        return entityType.equals(EntityType.CLASS);
    }

    public interface EntitiesCreatedHandler {

        void handleEntitiesCreated(ImmutableCollection<EntityNode> entities);
    }
}

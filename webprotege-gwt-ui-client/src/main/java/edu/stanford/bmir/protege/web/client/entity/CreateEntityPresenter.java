package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
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
    private final WhoDefaultCreateEntitiesPresenter defaultCreateEntitiesPresenter;

    @Nonnull
    private final CreateEntityFormPresenter createEntityFormPresenter;

    private final static Logger logger = Logger.getLogger(CreateEntityPresenter.class.getName());


    @Inject
    public CreateEntityPresenter(@Nonnull DispatchServiceManager dispatch,
                                 @Nonnull ProjectId projectId,
                                 @Nonnull WhoDefaultCreateEntitiesPresenter defaultCreateEntitiesPresenter,
                                 @Nonnull CreateEntityFormPresenter createEntityFormPresenter) {
        this.dispatch = checkNotNull(dispatch);
        this.projectId = checkNotNull(projectId);
        this.defaultCreateEntitiesPresenter = checkNotNull(defaultCreateEntitiesPresenter);
        this.createEntityFormPresenter = checkNotNull(createEntityFormPresenter);
    }

    public void createEntities(@Nonnull EntityType<?> entityType,
                               @Nonnull Optional<? extends OWLEntity> parentEntity,
                               @Nonnull EntitiesCreatedHandler entitiesCreatedHandler) {

        GWT.log("..........................................................");
        GWT.log("Suntem in CreateEntitiesPresenter: ");
        GWT.log("..........................................................");

        logger.info("..........................................................");
        logger.info("Suntem in CreateEntitiesPresenter: ");
        logger.info("..........................................................");

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
        logger.info("..........................................................");
        logger.info("Suntem in CreateEntitiesPresenter.handleEntityCreationFormsResult ");
        logger.info("..........................................................");
        if (createEntityForms.isEmpty()) {
            logger.info("..........................................................");
            logger.info("Suntem in CreateEntitiesPresenter.handleEntityCreationFormsResult: if() ");
            logger.info("..........................................................");
            defaultCreateEntitiesPresenter.createEntities(entityType,
                                                          parentEntity,
                                                          entitiesCreatedHandler);

        }
        else {
            logger.info("..........................................................");
            logger.info("Suntem in CreateEntitiesPresenter.handleEntityCreationFormsResult: else() ");
            logger.info("..........................................................");

            createEntityFormPresenter.createEntities(entityType,
                                                     parentEntity,
                                                     entitiesCreatedHandler,
                                                     createEntityForms);
        }
    }

    public interface EntitiesCreatedHandler {

        void handleEntitiesCreated(ImmutableCollection<EntityNode> entities);
    }
}

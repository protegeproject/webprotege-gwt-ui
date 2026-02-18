package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsNotCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-06
 */
public class EntityIsNotCriteriaPresenter implements CriteriaPresenter<EntityIsNotCriteria> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityIsCriteriaView view;

    @Nonnull
    private final EntityRenderingCache entityRenderingCache;

    @Inject
    public EntityIsNotCriteriaPresenter(@Nonnull ProjectId projectId,
                                     @Nonnull EntityIsCriteriaView view,
                                     @Nonnull EntityRenderingCache entityRenderingCache) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.entityRenderingCache = checkNotNull(entityRenderingCache);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<? extends EntityIsNotCriteria> getCriteria() {
        return view.getEntityData()
                   .map(OWLEntityData::getEntity)
                   .map(EntityIsNotCriteria::get);
    }

    @Override
    public void setCriteria(@Nonnull EntityIsNotCriteria criteria) {
        entityRenderingCache.load(projectId, criteria.getEntity(),
                result -> view.setEntity(result.getEntityData()));
    }
}

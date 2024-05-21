package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.logging.Logger;

public class EditParentsPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EditParentsView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private EntityType<?> entityType = EntityType.CLASS;

    @Nullable
    private OWLEntity entity;

    private static final Logger logger = Logger.getLogger(EditParentsPresenter.class.getName());

    @Inject
    public EditParentsPresenter(@Nonnull ProjectId projectId,
                                @Nonnull EditParentsView view, @Nonnull DispatchServiceManager dispatch) {
        this.projectId = projectId;
        this.view = view;
        this.dispatch = dispatch;
    }

    public void start(@Nonnull OWLEntity entity) {
        this.entity = entity;
        dispatch.execute(GetEntityRenderingAction.create(projectId, entity),
                result -> view.setOwlEntityData(result.getEntityData()));
    }

    @Nonnull
    public EditParentsView getView() {
        return view;
    }
}

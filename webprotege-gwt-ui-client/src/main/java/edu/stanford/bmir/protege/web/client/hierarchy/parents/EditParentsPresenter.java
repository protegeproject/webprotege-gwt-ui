package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

public class EditParentsPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EditParentsView view;

    @Nonnull
    private EntityType<?> entityType = EntityType.CLASS;

    @Nullable
    private OWLEntity entity;

    @Inject
    public EditParentsPresenter(@Nonnull ProjectId projectId,
                                @Nonnull EditParentsView view) {
        this.projectId = projectId;
        this.view = view;
    }

    public void start(@Nonnull OWLEntity entity) {
        this.entity = entity;
        this.view.setOwlEntity(entity);
    }

    @Nonnull
    public EditParentsView getView() {
        return view;
    }
}

package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.hierarchy.ChangeEntityParentsAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyParentsAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.issues.CreateEntityDiscussionThreadAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

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

    private Optional<HierarchyId> hierarchyId = Optional.empty();

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final Messages messages;

    private static final Logger logger = Logger.getLogger(EditParentsPresenter.class.getName());

    @Inject
    public EditParentsPresenter(@Nonnull ProjectId projectId,
                                @Nonnull EditParentsView view,
                                @Nonnull DispatchServiceManager dispatch,
                                @Nonnull ModalManager modalManager,
                                @Nonnull Messages messages) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatch = checkNotNull(dispatch);
        this.modalManager = checkNotNull(modalManager);
        this.messages = checkNotNull(messages);
    }

    public void start(@Nonnull OWLEntity entity) {
        view.clear();
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(messages.hierarchy_editParents());
        modalPresenter.setView(view);
        this.entity = entity;
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(DialogButton.OK);
        modalPresenter.setButtonHandler(DialogButton.OK, closer -> {
            if (view.isReasonForChangeSet()) {
                handleHierarchyChange(entity, view.getNewParentList());
                closer.closeModal();
            }
        });
        modalManager.showModal(modalPresenter);
        dispatch.execute(GetEntityRenderingAction.create(projectId, entity),
                result -> view.setOwlEntityData(result.getEntityData()));

        hierarchyId.ifPresent(id -> dispatch.execute(GetHierarchyParentsAction.create(projectId, entity, id),
                result -> view.setEntityParents(result.getParents())));
    }

    @Nonnull
    public EditParentsView getView() {
        return view;
    }

    public void setHierarchyId(@Nonnull HierarchyId hierarchyId) {
        this.hierarchyId = Optional.of(hierarchyId);
    }

    private void handleHierarchyChange(OWLEntity entity, List<OWLPrimitiveData> parentsList) {
        ImmutableSet<OWLClass> parentsSet = parentsList.stream()
                .map(owlPrimitiveData -> owlPrimitiveData.asEntity().get().asOWLClass())
                .collect(toImmutableSet());
        dispatch.execute(ChangeEntityParentsAction.create(projectId, parentsSet, entity.asOWLClass(), view.getReasonForChange()),
                changeEntityParentsResult -> dispatch.execute(
                        CreateEntityDiscussionThreadAction.create(projectId, entity, view.getReasonForChange()),
                        threadActionResult -> {
                        }));
    }
}

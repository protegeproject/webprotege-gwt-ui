package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.hierarchy.ChangeEntityParentsAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.ChangeEntityParentsResult;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyParentsAction;
import edu.stanford.bmir.protege.web.shared.issues.CreateEntityDiscussionThreadAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
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

    @Nullable
    private OWLEntity entity;

    private Optional<HierarchyDescriptor> hierarchyDescriptor = Optional.empty();

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
        modalPresenter.setButtonHandler(DialogButton.OK, this::handleHierarchyChange);
        modalManager.showModal(modalPresenter);
        dispatch.execute(GetEntityRenderingAction.create(projectId, entity),
                result -> view.setOwlEntityData(result.getEntityData()));

        hierarchyDescriptor.ifPresent(id -> dispatch.execute(GetHierarchyParentsAction.create(projectId, entity, hierarchyDescriptor.get()),
                result -> view.setEntityParents(result.getParents())));
    }

    @Nonnull
    public EditParentsView getView() {
        return view;
    }

    public void setHierarchyDescriptor(@Nonnull HierarchyDescriptor hierarchyDescriptor) {
        this.hierarchyDescriptor = Optional.of(hierarchyDescriptor);
    }

    private void handleHierarchyChange(ModalCloser closer) {
        if (view.isReasonForChangeSet()) {

            ImmutableSet<OWLClass> parentsSet = view.getNewParentList().stream()
                    .map(owlPrimitiveData -> owlPrimitiveData.asEntity().get().asOWLClass())
                    .collect(toImmutableSet());
            dispatch.execute(ChangeEntityParentsAction.create(projectId, parentsSet, entity.asOWLClass(), view.getReasonForChange()),
                    changeEntityParentsResult -> {
                        if (isResultValid(changeEntityParentsResult)) {
                            view.clearClassesWithCycle();
                            view.clearClassesWithRetiredParents();
                            dispatch.execute(
                                    CreateEntityDiscussionThreadAction.create(projectId, entity, view.getReasonForChange()),
                                    threadActionResult -> closer.closeModal());
                            return;
                        }

                        if(hasClassesWithRetiredParents(changeEntityParentsResult)){
                            view.clearClassesWithRetiredParents();
                            Set<OWLEntityData> classesWithRetiredParents = changeEntityParentsResult.getClassesWithRetiredParents();
                            view.markClassesWithRetiredParents(classesWithRetiredParents);
                        }

                        if(hasClassesWithCycles(changeEntityParentsResult)){
                            view.clearClassesWithCycle();
                            Set<OWLEntityData> classesWithCycles = changeEntityParentsResult.getClassesWithCycle();
                            view.markClassesWithCycles(classesWithCycles);
                        }
                    });
        }
    }

    private boolean isResultValid(ChangeEntityParentsResult changeEntityParentsResult) {
        return !hasClassesWithCycles(changeEntityParentsResult) && !hasClassesWithRetiredParents(changeEntityParentsResult);
    }

    private boolean hasClassesWithCycles(ChangeEntityParentsResult changeEntityParentsResult) {
        return !changeEntityParentsResult.getClassesWithCycle().isEmpty();
    }

    private boolean hasClassesWithRetiredParents(ChangeEntityParentsResult changeEntityParentsResult) {
        return !changeEntityParentsResult.getClassesWithRetiredParents().isEmpty();
    }
}

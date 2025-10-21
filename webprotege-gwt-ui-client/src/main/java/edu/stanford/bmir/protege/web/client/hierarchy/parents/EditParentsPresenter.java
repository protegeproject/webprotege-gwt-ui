package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.*;
import edu.stanford.bmir.protege.web.client.tooltip.Tooltip;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.directparents.GetLinearizationPathParentsAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.issues.CreateEntityDiscussionThreadAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.*;

import javax.annotation.*;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    private final UuidV4Provider uuidV4Provider;

    @Nullable
    private OWLEntity entity;

    private OWLEntityData entityAsEntityData;

    private Optional<ClassHierarchyDescriptor> classHierarchyDescriptor = Optional.empty();

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
                                @Nonnull Messages messages,
                                @Nonnull UuidV4Provider uuidV4Provider) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatch = checkNotNull(dispatch);
        this.modalManager = checkNotNull(modalManager);
        this.messages = checkNotNull(messages);
        this.uuidV4Provider = uuidV4Provider;
    }

    public void start(@Nonnull OWLEntity entity) {
        if (!entity.isOWLClass()) {
            return;
        }
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
                result -> {
                    this.entityAsEntityData = result.getEntityData();
                    view.setOwlEntityData(result.getEntityData());
                });

        classHierarchyDescriptor.ifPresent(id -> dispatch.execute(GetClassHierarchyParentsByAxiomTypeAction.create(projectId, entity.asOWLClass(), classHierarchyDescriptor.get()),
                result -> {
                    dispatch.execute(GetLinearizationPathParentsAction.create(entity.getIRI(), new HashSet<>(), projectId), (parentsResult -> {
                        view.setLinearizationPathParents(parentsResult.getExistingLinearizationParents());
                        view.setEntityParents(result.getParentsBySubclassOf());
                        view.setParentsFromEquivalentClasses(result.getParentsByEquivalentClass());
                    }));

                }));
        this.setHelpText(view.getHelpTooltip(), messages.hierarchy_editParents_equivalentClassParent());
    }

    @Nonnull
    public EditParentsView getView() {
        return view;
    }

    public void setHierarchyDescriptor(@Nonnull ClassHierarchyDescriptor classHierarchyDescriptor) {
        this.classHierarchyDescriptor = Optional.of(classHierarchyDescriptor);
    }

    private void handleHierarchyChange(ModalCloser closer) {
        if (!view.isValid()) {
            return;
        }

        ImmutableSet<OWLClass> parentsSet = view.getNewParentList().stream()
                .map(owlPrimitiveData -> owlPrimitiveData.asEntity().get().asOWLClass())
                .collect(toImmutableSet());
        dispatch.execute(ChangeEntityParentsAction.create(ChangeRequestId.get(uuidV4Provider.get()),
                        projectId, parentsSet, entity.asOWLClass(), view.getReasonForChange()),
                changeEntityParentsResult -> {
                    if (changeEntityParentsResult.isSuccess()) {
                        view.clearClassesWithCycleErrors();
                        view.clearClassesWithRetiredParentsErrors();
                        dispatch.execute(
                                CreateEntityDiscussionThreadAction.create(projectId, entity, view.getReasonForChange()),
                                threadActionResult -> {
                                    // Update local history with the commit message
                                    if (view instanceof EditParentsViewImpl) {
                                        ((EditParentsViewImpl) view).updateLocalHistory();
                                    }
                                    closer.closeModal();
                                });
                        return;
                    }

                    if (changeEntityParentsResult.hasClassesWithRetiredParents()) {
                        view.clearClassesWithRetiredParentsErrors();
                        Set<OWLEntityData> classesWithRetiredParents = changeEntityParentsResult.getClassesWithRetiredParents();
                        view.markClassesWithRetiredParents(classesWithRetiredParents);
                    }

                    if (changeEntityParentsResult.getReleasedChildrenValidationMessage() != null && !changeEntityParentsResult.getReleasedChildrenValidationMessage().isEmpty()) {
                        view.clearReleasedChildrenError();
                        view.displayReleasedChildrenError(entityAsEntityData.getBrowserText(), changeEntityParentsResult.getReleasedChildrenValidationMessage());
                    }

                    if (changeEntityParentsResult.hasClassesWithCycle()) {
                        view.clearClassesWithCycleErrors();
                        Set<OWLEntityData> classesWithCycles = changeEntityParentsResult.getClassesWithCycle();
                        view.markClassesWithCycles(classesWithCycles);
                    }

                    if (changeEntityParentsResult.hasOldParentAsLinearizationPathParent()) {
                        view.clearLinearizationPathParentErrors();
                        String parents = changeEntityParentsResult.getOldParentsThatAreLinearizationPathParents()
                                .stream()
                                .map(OWLEntityData::getBrowserText)
                                .collect(Collectors.joining(", "));
                        view.markLinearizationPathParent(parents);
                    }
                });
    }


    private void setHelpText(IsWidget tooltipWrapper, String text) {
        Tooltip helpTooltip = Tooltip.create(tooltipWrapper, text);
        helpTooltip.updateTitleContent(text);
    }
}

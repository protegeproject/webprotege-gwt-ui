package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameSettingsManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.AbstractCreateEntityResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateAnnotationPropertiesAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateClassesAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateDataPropertiesAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateNamedIndividualsAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateObjectPropertiesAction;
import edu.stanford.bmir.protege.web.shared.issues.CreateEntityDiscussionThreadAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-29
 */
public class WhoCreateClassPresenter {


    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final WhoCreateClassDialogView view;

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final ActiveProjectManager activeProjectManager;

    @Nonnull
    private final DisplayNameSettingsManager displayNameSettingsManager;

    @Nonnull
    private final DuplicateEntityPresenter duplicateEntityPresenter;

    @Nonnull
    private final SelectionModel selectionModel;


    @Nonnull
    private final Messages messages;

    private Optional<String> currentLangTag = Optional.empty();

    private final static Logger logger = Logger.getLogger(WhoCreateClassPresenter.class.getName());

    @Inject
    public WhoCreateClassPresenter(@Nonnull DispatchServiceManager dispatchServiceManager,
                                   @Nonnull ProjectId projectId,
                                   @Nonnull WhoCreateClassDialogView view,
                                   @Nonnull ModalManager modalManager,
                                   @Nonnull ActiveProjectManager activeProjectManager,
                                   @Nonnull DisplayNameSettingsManager displayNameSettingsManager,
                                   @Nonnull DuplicateEntityPresenter duplicateEntityPresenter, @Nonnull SelectionModel selectionModel, @Nonnull Messages messages) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        this.view = view;
        this.modalManager = modalManager;
        this.activeProjectManager = activeProjectManager;
        this.displayNameSettingsManager = displayNameSettingsManager;
        this.duplicateEntityPresenter = duplicateEntityPresenter;
        this.selectionModel = selectionModel;
        this.messages = messages;
    }

    public void createEntities(@Nonnull EntityType<?> entityType,
                               @Nonnull Optional<? extends OWLEntity> parentEntity,
                               @Nonnull CreateEntityPresenter.EntitiesCreatedHandler entitiesCreatedHandler) {

        view.clear();
        view.setEntityType(entityType);
        view.setResetLangTagHandler(this::resetLangTag);
        view.setLangTagChangedHandler(this::handleLangTagChanged);
        duplicateEntityPresenter.start(view.getDuplicateEntityResultsContainer());
        duplicateEntityPresenter.setEntityTypes(entityType);
        view.setEntitiesStringChangedHandler(duplicateEntityPresenter::handleEntitiesStringChanged);

        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(messages.create() + " " + entityType.getPluralPrintName());
        modalPresenter.setView(view);
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(DialogButton.CREATE);
        modalPresenter.setButtonHandler(DialogButton.CREATE, closer -> {
            if (view.isReasonForChangeSet()) {
                handleCreateEntities(entityType, view.getText(), view.getReasonForChange(), parentEntity, entitiesCreatedHandler);
                closer.closeModal();
            }
        });
        duplicateEntityPresenter.setHierarchySelectionHandler(selection -> {
            logger.info("avem selectie: "+selection);
            selectionModel.setSelection(selection.getEntity());
            modalPresenter.closeModal();
        });
        modalManager.showModal(modalPresenter);
        displayCurrentLangTagOrProjectDefaultLangTag();



    }

    private void resetLangTag() {
        currentLangTag = Optional.empty();
        displayCurrentLangTagOrProjectDefaultLangTag();
    }

    private void handleLangTagChanged() {
        String langTag = view.getLangTag();
        view.setNoDisplayLanguageForLangTagVisible(false);
        if (displayNameSettingsManager.getLocalDisplayNameSettings().hasDisplayNameLanguageForLangTag(langTag)) {
            return;
        }
        activeProjectManager.getActiveProjectDetails(details -> {
            details.ifPresent(d -> {
                if (!d.getDefaultDisplayNameSettings().hasDisplayNameLanguageForLangTag(langTag)) {
                    view.setNoDisplayLanguageForLangTagVisible(true);
                }
            });
        });

        this.duplicateEntityPresenter.setLangTag(langTag);
    }

    private void displayCurrentLangTagOrProjectDefaultLangTag() {
        activeProjectManager.getActiveProjectDetails(details -> details.ifPresent(d -> {
            currentLangTag.ifPresent(view::setLangTag);
            if (!currentLangTag.isPresent()) {
                String defaultLangTag = d.getDefaultDictionaryLanguage().getLang();
                currentLangTag = Optional.of(defaultLangTag);
                view.setLangTag(defaultLangTag);
                handleLangTagChanged();
            }
        }));
    }

    private <E extends OWLEntity> void handleCreateEntities(@Nonnull EntityType<?> entityType,
                                                            @Nonnull String enteredText,
                                                            @Nonnull String reasonForChange,
                                                            @Nonnull Optional<? extends OWLEntity> parent,
                                                            @Nonnull CreateEntityPresenter.EntitiesCreatedHandler entitiesCreatedHandler) {

        GWT.log("[CreateEntityPresenter] handleCreateEntities.  Lang: " + view.getLangTag());
        currentLangTag = Optional.of(view.getLangTag());
        ProjectAction<? extends AbstractCreateEntityResult<?>> action = getAction(entityType,
                parent,
                enteredText);
        dispatchServiceManager.execute(action,
                result -> {
                    dispatchServiceManager.execute(
                            CreateEntityDiscussionThreadAction.create(projectId, result.getEntities().stream().findFirst().get().getEntity(), reasonForChange),
                            threadActionResult -> {
                                entitiesCreatedHandler.handleEntitiesCreated(result.getEntities());
                            });
                });

    }

    private ProjectAction<? extends AbstractCreateEntityResult<?>> getAction(EntityType<?> entityType,
                                                                             Optional<? extends OWLEntity> parent,
                                                                             String enteredText) {
        if (entityType.equals(EntityType.CLASS)) {
            ImmutableSet<OWLClass> parentClses = getParents(parent, DataFactory::getOWLThing);
            return CreateClassesAction.create(projectId, enteredText, view.getLangTag(), parentClses);
        } else if (entityType.equals(EntityType.OBJECT_PROPERTY)) {
            ImmutableSet<OWLObjectProperty> parentProperties = getParents(parent, () -> DataFactory.get().getOWLTopObjectProperty());
            return CreateObjectPropertiesAction.create(projectId, enteredText, view.getLangTag(), parentProperties);
        } else if (entityType.equals(EntityType.DATA_PROPERTY)) {
            ImmutableSet<OWLDataProperty> parentProperties = getParents(parent, () -> DataFactory.get().getOWLTopDataProperty());
            return CreateDataPropertiesAction.create(projectId, enteredText, view.getLangTag(), parentProperties);
        } else if (entityType.equals(EntityType.ANNOTATION_PROPERTY)) {
            ImmutableSet<OWLAnnotationProperty> parentProperties = getParentsSet(parent, ImmutableSet::of);
            return CreateAnnotationPropertiesAction.create(projectId, enteredText, view.getLangTag(), parentProperties);
        } else if (entityType.equals(EntityType.NAMED_INDIVIDUAL)) {
            ImmutableSet<OWLClass> parentClses = getParents(parent, DataFactory::getOWLThing);
            return CreateNamedIndividualsAction.create(projectId, enteredText, view.getLangTag(), parentClses);
        } else {
            throw new RuntimeException("Unsupported entity type: " + entityType);
        }
    }

    private static <E extends OWLEntity> ImmutableSet<E> getParents(Optional<? extends OWLEntity> parent,
                                                                    Supplier<E> defaultSupplier) {
        return getParentsSet(parent, () -> ImmutableSet.of(defaultSupplier.get()));
    }

    private static <E extends OWLEntity> ImmutableSet<E> getParentsSet(Optional<? extends OWLEntity> parent,
                                                                       Supplier<ImmutableSet<E>> defaultSupplier) {
        return parent.map(p -> ImmutableSet.of((E) p)).orElseGet(defaultSupplier);
    }


    private void selectChosenEntity() {
        duplicateEntityPresenter.getSelectedSearchResult()
                .ifPresent(sel -> selectionModel.setSelection(sel.getEntity()));
    }


}

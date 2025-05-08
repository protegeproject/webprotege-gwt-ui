package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.entity.DeprecateEntityModal;
import edu.stanford.bmir.protege.web.client.lang.LangTagFilterPresenter;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.tab.SelectedTabIdStash;
import edu.stanford.bmir.protege.web.client.ui.DisplayContextManager;
import edu.stanford.bmir.protege.web.client.ui.HasDisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.DisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.access.BuiltInCapability;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrdering;
import edu.stanford.bmir.protege.web.shared.lang.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-23
 */
public class EntityFormStackPresenter implements HasDisplayContextBuilder {

    private final static java.util.logging.Logger logger = Logger.getLogger("EntityFormStackPresenter");

    private Optional<OWLEntity> currentEntity = Optional.empty();

    private PristineFormDataManager pristineDataManager = new PristineFormDataManager();

    private HasBusy hasBusy = (busy) -> {
    };

    private Optional<FormLanguageFilterStash> formLanguageFilterStash = Optional.empty();

    private FormMode mode = FormMode.READ_ONLY_MODE;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityFormStackView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final FormStackPresenter formStackPresenter;

    @Nonnull
    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    @Nonnull
    private final LangTagFilterPresenter langTagFilterPresenter;

    @Nonnull
    private final DeprecateEntityModal deprecateEntityModal;

    @Nonnull
    private EntityDisplay entityDisplay = entityData -> {
    };

    private DisplayContextManager displayContextManager;

    @Inject
    public EntityFormStackPresenter(@Nonnull ProjectId projectId,
                                    @Nonnull EntityFormStackView view,
                                    @Nonnull DispatchServiceManager dispatch,
                                    @Nonnull FormStackPresenter formStackPresenter,
                                    @Nonnull LoggedInUserProjectCapabilityChecker capabilityChecker,
                                    @Nonnull LangTagFilterPresenter langTagFilterPresenter,
                                    @Nonnull DeprecateEntityModal deprecateEntityModal) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatch = checkNotNull(dispatch);
        this.formStackPresenter = checkNotNull(formStackPresenter);
        this.capabilityChecker = checkNotNull(capabilityChecker);
        this.langTagFilterPresenter = checkNotNull(langTagFilterPresenter);
        this.deprecateEntityModal = deprecateEntityModal;
        this.displayContextManager = new DisplayContextManager(this::fillDisplayContext);
    }

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        formStackPresenter.start(view.getFormStackContainer());
        formStackPresenter.setFormRegionPageChangedHandler(this::handlePageChange);
        formStackPresenter.setFormRegionOrderingChangedHandler(this::handleGridOrderByChanged);
        formStackPresenter.setFormRegionFilterChangedHandler(this::handleFormRegionFilterChanged);
        formStackPresenter.setSelectedFormChangedHandler(this::handleSelectedFormChanged);
        langTagFilterPresenter.start(view.getLangTagFilterContainer());
        langTagFilterPresenter.setLangTagFilterChangedHandler(this::handleLangTagFilterChanged);
        view.setEnterEditModeHandler(this::handleEnterEditMode);
        view.setApplyEditsHandler(this::handleApplyEdits);
        view.setDeprecateEntityHandler(this::handleDeprecateEntity);
        view.setCancelEditsHandler(this::handleCancelEdits);

        capabilityChecker.hasCapability(BuiltInCapability.EDIT_ONTOLOGY, view::setEditButtonVisible);
        setMode(FormMode.READ_ONLY_MODE);
    }

    private void handleSelectedFormChanged() {
        ImmutableList<FormId> selectedForms = formStackPresenter.getSelectedForms();
        ImmutableList<FormId> selectedFormsToUpdate = selectedForms.stream()
                                                                   .filter(formId -> !pristineDataManager.containsPristineFormData(formId))
                                                                   .collect(toImmutableList());
        if (selectedFormsToUpdate.isEmpty()) {
            return;
        }
        GWT.log("[EntityFormStackPresenter] Updating forms: " + selectedForms);
        updateFormsForCurrentEntity(selectedFormsToUpdate);
    }

    private void handleFormRegionFilterChanged(FormRegionFilterChangedEvent event) {
        updateFormsForCurrentEntity(formStackPresenter.getSelectedForms());
    }

    private void handleGridOrderByChanged() {
        updateFormsForCurrentEntity(formStackPresenter.getSelectedForms());
    }

    public void setHasBusy(HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    private void handleLangTagFilterChanged() {
        stashLanguagesFilter();
        updateFormsForCurrentEntity(formStackPresenter.getSelectedForms());
    }

    private void handlePageChange(FormRegionPageChangedEvent event) {
        updateFormsForCurrentEntity(ImmutableList.of(event.getFormId()));
    }

    public void setEntity(@Nonnull OWLEntity entity) {
        if (mode.equals(FormMode.EDIT_MODE)) {
            handleOutstandingEditsAndSwitchToEntity(entity);
        }
        else {
            switchToEntity(entity);
        }
    }

    private void handleOutstandingEditsAndSwitchToEntity(@Nonnull OWLEntity entity) {
        view.displayApplyOutstandingEditsConfirmation(() -> {
            currentEntity.ifPresent(this::applyEdits);
            switchToEntity(entity);
        }, () -> {
            handleCancelEdits();
            switchToEntity(entity);
        });
    }

    private void switchToEntity(@Nonnull OWLEntity entity) {
        this.currentEntity = Optional.of(entity);
        pristineDataManager.resetCurrentEntity(entity);
        updateFormsForCurrentEntity(formStackPresenter.getSelectedForms());
    }

    public void clear() {
        this.currentEntity = Optional.empty();
        pristineDataManager.clearCurrentEntity();
        updateFormsForCurrentEntity(formStackPresenter.getSelectedForms());
    }

    public void expandAllFields() {
        formStackPresenter.expandAllFields();
    }

    public void collapseAllFields() {
        formStackPresenter.collapseAllFields();
    }

    private void updateFormsForCurrentEntity(ImmutableList<FormId> formFilter) {
        currentEntity.ifPresent(entity -> {
            ImmutableList<FormPageRequest> pageRequests = formStackPresenter.getPageRequests();
            ImmutableSet<FormRegionOrdering> orderings = formStackPresenter.getGridControlOrderings();
            ImmutableSet<FormRegionFilter> filters = formStackPresenter.getRegionFilters();
            LangTagFilter langTagFilter = langTagFilterPresenter.getFilter();
            dispatch.execute(GetEntityFormsAction.create(projectId,
                                                      entity,
                                                      ImmutableSet.copyOf(pageRequests),
                                                      langTagFilter,
                                                      orderings,
                                                      ImmutableSet.of(),
                                                      //TODO avoid missing on setting to pristine forms, when fixed change to ImmutableSet.copyOf(formFilter),
                                                      filters), hasBusy, this::handleGetEntityFormsResult);
        });
        if (!currentEntity.isPresent()) {
            entityDisplay.setDisplayedEntity(Optional.empty());
            formStackPresenter.clearForms();
        }
    }

    private void handleGetEntityFormsResult(GetEntityFormsResult result) {
        entityDisplay.setDisplayedEntity(Optional.of(result.getEntityData()));
        /*
         ToDo:
          uncomment deprecate button bellow when we made it configurable.
          it is presumed that a user needs a specific role to make a form deprecated
         */
        view.setDeprecateButtonVisible(false);
//        view.setDeprecateButtonVisible(!result.getEntityData().isDeprecated());
        ImmutableList<FormDataDto> formData = result.getFormData();
        for (FormDataDto formDataDto : result.getFormData()) {
            pristineDataManager.updatePristineFormData(formDataDto.toFormData());
        }
        boolean replaceAllForms = result.getFilteredFormIds().isEmpty() || result.getFormData().isEmpty();
        // If we have a subset of the forms then just replace the ones that we have
        if (replaceAllForms) {
            formStackPresenter.setForms(formData, FormStackPresenter.FormUpdate.REPLACE);
        }
        else {
            formStackPresenter.setForms(formData, FormStackPresenter.FormUpdate.PATCH);
        }
    }

    private void handleEnterEditMode() {
        capabilityChecker.hasCapability(BuiltInCapability.EDIT_ONTOLOGY, canEdit -> setMode(FormMode.EDIT_MODE));
    }

    private void handleApplyEdits() {
        setMode(FormMode.READ_ONLY_MODE);
        currentEntity.ifPresent(this::applyEdits);
    }

    private void applyEdits(@Nonnull OWLEntity entity) {
        // TODO: Offer a commit message
        setMode(FormMode.READ_ONLY_MODE);
        capabilityChecker.hasCapability(BuiltInCapability.EDIT_ONTOLOGY, canEdit -> {
            if (canEdit) {
                commitEdits(entity);
            }
        });
    }

    private void handleDeprecateEntity() {
        currentEntity.ifPresent(entity -> deprecateEntityModal.showModal(entity,
                                                                         () -> view.setDeprecateButtonVisible(false),
                                                                         () -> {
                                                                         }));
    }

    private void handleCancelEdits() {
        setMode(FormMode.READ_ONLY_MODE);
        dropEdits();
    }

    private void setMode(@Nonnull FormMode mode) {
        this.mode = checkNotNull(mode);
        if (mode == FormMode.EDIT_MODE) {
            view.setEditButtonVisible(false);
            view.setApplyEditsButtonVisible(true);
            view.setCancelEditsButtonVisible(true);
        }
        else {
            view.setEditButtonVisible(true);
            view.setApplyEditsButtonVisible(false);
            view.setCancelEditsButtonVisible(false);
        }
        formStackPresenter.setEnabled(mode == FormMode.EDIT_MODE);
    }

    private void commitEdits(@Nonnull OWLEntity entity) {
        try {
            FormDataByFormId editedFormData = formStackPresenter.getFormData();
            Collection<FormId> editedFormIds = editedFormData.getFormIds();
            ImmutableMap<FormId, FormData> pristineFormData = pristineDataManager.getPristineFormData(editedFormIds);
            dispatch.execute(new SetEntityFormsDataAction(projectId, entity, "", pristineFormData, editedFormData),
                    // Refresh the pristine data to what was committed
                    result -> updateFormsForCurrentEntity(ImmutableList.copyOf(editedFormIds)));
        }catch (Exception e) {
            logger.info("An exception occured " + e);
        }
    }

    private void dropEdits() {
        currentEntity.ifPresent(e -> pristineDataManager.resetCurrentEntity(e));
        // Back to pristine data for the selected forms
        updateFormsForCurrentEntity(formStackPresenter.getSelectedForms());
    }

    public void setSelectedFormIdStash(@Nonnull SelectedTabIdStash<FormId> formIdStash) {
        formStackPresenter.setSelectedFormIdStash(formIdStash);
    }

    private void stashLanguagesFilter() {
        formLanguageFilterStash.ifPresent(stash -> {
            ImmutableSet<LangTag> filteringTags = langTagFilterPresenter.getFilter().getFilteringTags();
            stash.stashLanguage(filteringTags);
        });
    }

    public void setLanguageFilterStash(FormLanguageFilterStash formLanguageFilterStash) {
        this.formLanguageFilterStash = Optional.of(checkNotNull(formLanguageFilterStash));
        ImmutableSet<LangTag> stashedLangTags = ImmutableSet.copyOf(formLanguageFilterStash.getStashedLangTags());
        LangTagFilter langTagFilter = LangTagFilter.get(stashedLangTags);
        langTagFilterPresenter.setFilter(langTagFilter);
    }

    public void fillDisplayContext(DisplayContextBuilder displayContextBuilder) {
        // Nothing to do.  Handled by the FormStackPresenter
    }

    @Override
    public void setParentDisplayContextBuilder(HasDisplayContextBuilder parent) {
        displayContextManager.setParentDisplayContextBuilder(parent);
        formStackPresenter.setParentDisplayContextBuilder(this);
    }

    @Override
    public DisplayContextBuilder fillDisplayContextBuilder() {
        return displayContextManager.fillDisplayContextBuilder();
    }
}

package edu.stanford.bmir.protege.web.client.card;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.FormPresenter;
import edu.stanford.bmir.protege.web.client.form.FormRegionPageChangedEvent;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.selection.*;
import edu.stanford.bmir.protege.web.client.ui.*;
import edu.stanford.bmir.protege.web.shared.*;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataDto;
import edu.stanford.bmir.protege.web.shared.lang.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * An {@link EntityCardPresenter} that presents the content of a form.
 */
public class FormContentEntityCardPresenter implements EntityCardEditorPresenter, HasBusy {

    private static final Logger logger = Logger.getLogger("FormContentEntityCardPresenter");

    private final ProjectId projectId;

    private final FormId formId;

    private final FormPresenter formPresenter;

    private final DispatchServiceManager dispatch;

    private Optional<OWLEntity> renderedEntity = Optional.empty();

    private Optional<OWLEntity> entity;

    private Optional<FormData> pristineFormData = Optional.empty();

    private HandlerManager handlerManager = new HandlerManager(this);

    private final SelectedPathsModel selectedPathsModel;

    private final DisplayContextManager displayContextManager = new DisplayContextManager(context -> {});

    private ImmutableSet<Capability> capabilities = ImmutableSet.of();

    @AutoFactory
    @Inject
    public FormContentEntityCardPresenter(FormId formId,
                                          @Provided DispatchServiceManager dispatch,
                                          @Provided ProjectId projectId,
                                          @Provided FormPresenter formPresenter,
                                          @Provided SelectionModel selectionModel,
                                          @Provided SelectedPathsModel selectedPathsModel) {
        this.dispatch = dispatch;
        this.projectId = projectId;
        this.formId = formId;
        this.formPresenter = formPresenter;
        this.selectedPathsModel = selectedPathsModel;
    }

    @Override
    public boolean isEditor() {
        return true;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    public void start(EntityCardUi ui, WebProtegeEventBus eventBus) {
        formPresenter.start(ui);
        formPresenter.setEnabled(false);
        formPresenter.setFormDataChangedHandler(() -> {
            handlerManager.fireEvent(new DirtyChangedEvent());
        });
        formPresenter.setFormRegionPageChangedHandler(event -> {
            updateDisplayedForm();
        });
        formPresenter.setFormRegionFilterChangedHandler(event -> {
            updateDisplayedForm();
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public void setEntity(OWLEntity entity) {
        this.entity = Optional.of(entity);
    }

    @Override
    public void clearEntity() {
        entity = Optional.empty();
        renderedEntity = Optional.empty();
    }

    @Override
    public void setCapabilities(ImmutableSet<Capability> capabilities) {
        this.capabilities = capabilities;
    }

    private void setDisplayedEntity(Optional<OWLEntity> entity) {
        updateDisplayedForm();
    }

    public boolean isDirty() {
        Optional<FormData> formData = formPresenter.getFormData();
        logger.log(Level.FINE, "Pristine form data: " + pristineFormData);
        logger.log(Level.FINE, "Edited form data: " + formData);
        return !pristineFormData.equals(formData);
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return handlerManager.addHandler(DirtyChangedEvent.TYPE, handler);
    }

    private void saveChanges(String commitMessage) {
        if (!isDirty()) {
            return;
        }
        this.entity.ifPresent(owlEntity -> {
            Optional<FormData> formData = formPresenter.getFormData();
            formData.ifPresent(fd -> {
                Map<FormId, FormData> editedFormData = new HashMap<>();
                editedFormData.put(formId, fd);
                FormDataByFormId formDataByFormId = new FormDataByFormId(editedFormData);
                FormData pristine = pristineFormData.orElse(FormData.empty(owlEntity, formId));
                dispatch.execute(new SetEntityFormsDataAction(projectId, owlEntity,
                        commitMessage,
                        ImmutableMap.of(formId, pristine),
                        formDataByFormId), result -> updateDisplayedForm());
            });
        });
    }

    /**
     * Updates the displayed form based on the current entity.
     * This method retrieves the forms for the entity using the GetEntityFormsAction.
     * It then clears the existing form data, resets the pristine form data with the new data from the result,
     * and displays the form if there is only one form in the result.
     */
    private void updateDisplayedForm() {
        entity.ifPresent(e -> dispatch.execute(
                        GetEntityFormsAction.create(
                                projectId,
                                e,
                                ImmutableSet.copyOf(formPresenter.getPageRequest()),
                                LangTagFilter.get(ImmutableSet.of()),
                                ImmutableSet.copyOf(formPresenter.getOrderings().collect(Collectors.toList())),
                                ImmutableSet.of(formId),
                                formPresenter.getFilters()
                        ),
                        this::updateDisplayedForm
                )
        );
    }

    private void updateDisplayedForm(GetEntityFormsResult result) {
        formPresenter.clearData();
        ImmutableList<FormDataDto> formData = result.getFormData();
        Optional<FormDataDto> nextFormData = formData.stream()
                .filter(formDataDto -> formDataDto.getFormId().equals(formId))
                .findFirst();
        logger.fine("Retrieved form data: " + result.getFormData());

        nextFormData.ifPresent(formPresenter::displayForm);
        if(!nextFormData.isPresent()) {
            formPresenter.clear();
        }

        fireEvent(new DirtyChangedEvent());
    }

    @Override
    public void beginEditing() {
        formPresenter.setEnabled(true);
    }

    @Override
    public void cancelEditing() {
        if(formPresenter.isEnabled()) {
            formPresenter.setEnabled(false);
            updateDisplayedForm();
        }
    }

    @Override
    public void finishEditing(String commitMessage) {
        formPresenter.setEnabled(false);
        saveChanges(commitMessage);
    }

    @Override
    public void setBusy(boolean busy) {

    }

    @Override
    public void requestFocus() {
        if(!renderedEntity.isPresent()  || !renderedEntity.get().equals(entity.get())) {
            renderedEntity = entity;
            updateDisplayedForm();
            formPresenter.requestFocus();
        }
    }

    @Override
    public DisplayContextBuilder fillDisplayContextBuilder() {
        return displayContextManager.fillDisplayContextBuilder();
    }

    @Override
    public void setParentDisplayContextBuilder(HasDisplayContextBuilder parent) {
        logger.info("FormContentEntityCardPresenter: set parent displaycontextbuilder"+displayContextManager.fillDisplayContextBuilder());
        displayContextManager.setParentDisplayContextBuilder(parent);
        formPresenter.setParentDisplayContextBuilder(this);
    }
}


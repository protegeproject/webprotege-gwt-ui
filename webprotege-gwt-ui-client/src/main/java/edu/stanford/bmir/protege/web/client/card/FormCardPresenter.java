package edu.stanford.bmir.protege.web.client.card;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.FormPresenter;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.selection.EntitySelectionChangedEvent;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.card.CardId;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataDto;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.SelectionChangeEvent;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FormCardPresenter implements EntityCardPresenter, HasBusy {

    private final ProjectId projectId;

    private final FormId formId;

    private final FormPresenter formPresenter;
    private final SelectionModel selectionModel;

    private final CardId cardId;
    private final LanguageMap label;
    private final Color color;
    private final Color backgroundColor;
    private final DispatchServiceManager dispatch;


    private Optional<OWLEntity> entity;

    private Map<FormId, FormData> pristineFormData = new HashMap<>();

    @AutoFactory
    @Inject
    public FormCardPresenter(CardId cardId,
                             FormId formId,
                             LanguageMap label,
                             @Nullable Color color,
                             @Nullable Color backgroundColor,
                             @Provided DispatchServiceManager dispatch,
                             @Provided ProjectId projectId,
                             @Provided FormPresenter formPresenter,
                             @Provided SelectionModel selectionModel) {
        this.cardId = cardId;
        this.label = label;
        this.color = color;
        this.backgroundColor = backgroundColor;
        this.dispatch = dispatch;
        this.projectId = projectId;
        this.formId = formId;
        this.formPresenter = formPresenter;
        this.selectionModel = selectionModel;
    }

    @Override
    public CardId getCardId() {
        return cardId;
    }

    @Override
    public LanguageMap getLabel() {
        return label;
    }

    @Override
    public Optional<Color> getColor() {
        return Optional.ofNullable(color);
    }

    @Override
    public Optional<Color> getBackgroundColor() {
        return Optional.ofNullable(backgroundColor);
    }

    public void start(AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        formPresenter.start(container);
        eventBus.addApplicationEventHandler(EntitySelectionChangedEvent.getType(), event -> {
            setDisplayedEntity(selectionModel.getSelection());
        });
        setDisplayedEntity(selectionModel.getSelection());
    }

    @Override
    public boolean isEditor() {
        return true;
    }

    private void setDisplayedEntity(Optional<OWLEntity> entity) {
        this.entity = entity;
        updateDisplayedForm();
    }

    private boolean isDirty() {
        Optional<FormData> formData = formPresenter.getFormData();
        if(!formData.isPresent()) {
            return false;
        }
        Map<FormId, FormData> currentFormData = new HashMap<>();
        currentFormData.put(formId, formData.get());
        return !pristineFormData.equals(currentFormData);
    }

    private void saveChanges() {
        if(!isDirty()) {
            return;
        }
        this.entity.ifPresent(owlEntity -> {
            Optional<FormData> formData = formPresenter.getFormData();
            formData.ifPresent(fd -> {
                Map<FormId, FormData> editedFormData = new HashMap<>();
                editedFormData.put(formId, fd);
                    FormDataByFormId formDataByFormId = new FormDataByFormId(editedFormData);
                    dispatch.execute(new SetEntityFormsDataAction(projectId, owlEntity,
                            ImmutableMap.copyOf(pristineFormData),
                            formDataByFormId), result -> {

                    });
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
        entity.ifPresent(e -> {
            dispatch.execute(GetEntityFormsAction.create(projectId,
                            e,
                            ImmutableSet.copyOf(formPresenter.getPageRequest()),
                            LangTagFilter.get(ImmutableSet.of()),
                            ImmutableSet.copyOf(formPresenter.getOrderings().collect(Collectors.toList())),
                            ImmutableSet.of(formId),
                            formPresenter.getFilters()
                    ),
                    result -> {
                        formPresenter.clearData();
                        resetPristineFormData(result);
                        if (result.getFormData().size() == 1) {
                            formPresenter.displayForm(result.getFormData().get(0));
                        }
                    });
        });
    }

    /**
     * Resets the pristine form data by clearing the existing data and populating it with the new form data
     * from the provided result.
     *
     * @param result The result containing the new/pristing form data
     */
    private void resetPristineFormData(GetEntityFormsResult result) {
        pristineFormData.clear();
        result.getFormData()
                .stream()
                .map(FormDataDto::toFormData)
                .forEach(fd -> pristineFormData.put(fd.getFormId(), fd));
    }

    @Override
    public void setEditable(boolean editable) {
        formPresenter.setEnabled(editable);
    }

    @Override
    public void commitChanges() {
        saveChanges();
    }

    @Override
    public void cancelChanges() {
        updateDisplayedForm();
    }

    @Override
    public void setBusy(boolean busy) {

    }

    @Override
    public void requestFocus() {
        formPresenter.requestFocus();
    }
}


package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.FormRegionPageChangedEvent.FormRegionPageChangedHandler;
import edu.stanford.bmir.protege.web.client.ui.DisplayContextManager;
import edu.stanford.bmir.protege.web.client.ui.HasDisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.DisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 * <p>
 * Presents a form and its associated form data.
 */
public class FormPresenter implements HasFormRegionFilterChangedHandler, HasDisplayContextBuilder {

    @Nonnull
    private final FormView formView;

    @Nonnull
    private final NoFormView noFormView;

    private boolean displayNoFormViewIfEmpty = true;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    private final List<FormFieldPresenter> fieldPresenters = new ArrayList<>();

    @Nonnull
    private Optional<FormDescriptorDto> currentFormDescriptor = Optional.empty();

    private Optional<AcceptsOneWidget> container = Optional.empty();

    @Nonnull
    private Optional<FormSubjectDto> currentSubject = Optional.empty();

    private FormFieldPresenterFactory formFieldPresenterFactory;

    private Set<FormRegionId> collapsedFields = new HashSet<>();

    private FormRegionPageChangedHandler formRegionPageChangedHandler = formId -> {
    };

    private boolean enabled = true;

    @Nonnull
    private FormRegionOrderingChangedHandler orderByChangedHandler = () -> {
    };

    @Nonnull
    private Optional<FormId> formId = Optional.empty();

    @Nonnull
    private FormRegionFilterChangedHandler formRegionFilterChangeHandler = event -> {
    };

    @Nonnull
    private FormDataChangedHandler formDataChangedHandler = () -> {};

    private boolean fieldsCollapsible = true;

    private DisplayContextManager displayContextManager = new DisplayContextManager(this::fillDisplayContext);

    @AutoFactory
    @Inject
    public FormPresenter(@Nonnull @Provided FormView formView,
                         @Nonnull @Provided NoFormView noFormView,
                         @Nonnull @Provided DispatchServiceManager dispatchServiceManager,
                         @Nonnull FormFieldPresenterFactory formFieldPresenterFactory) {
        this.formView = checkNotNull(formView);
        this.noFormView = checkNotNull(noFormView);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.formFieldPresenterFactory = formFieldPresenterFactory;
    }

    public void clearData() {
        currentSubject = Optional.empty();
        fieldPresenters.forEach(FormFieldPresenter::clearValue);
    }

    public void clearStyle() {
        formView.clearStyle();
    }

    public void collapseAll() {
        fieldPresenters.forEach(p -> p.setExpansionState(ExpansionState.COLLAPSED));
    }

    /**
     * Displays the specified form and the specified form data.
     *
     * @param formData The form data to be shown in the form.
     */
    public void displayForm(@Nonnull FormDataDto formData) {
        checkNotNull(formData);
        saveExpansionState();
        currentSubject = formData.getSubject();
        this.formId = Optional.of(formData.getFormDescriptor().getFormId());
        if (isCurrentForm(formData)) {
            updateFormData(formData);
        }
        else {
            createFormAndSetFormData(formData);
        }
        if (formData.getFormDescriptor().getFields().isEmpty()) {
            container.ifPresent(c -> {
                if (displayNoFormViewIfEmpty) {
                    c.setWidget(noFormView);
                }
            });
        }
        else {
            container.ifPresent(c -> c.setWidget(formView));
        }
        currentFormDescriptor = Optional.of(formData.getFormDescriptor());
    }

    private boolean isCurrentForm(FormDataDto formData) {
        return currentFormDescriptor.equals(Optional.of(formData.getFormDescriptor()));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void saveExpansionState() {
        collapsedFields.clear();
        fieldPresenters.forEach(p -> {
            if (p.getExpansionState() == ExpansionState.COLLAPSED) {
                FormRegionId id = p.getValue().getFormFieldDescriptor().getId();
                collapsedFields.add(id);
            }
        });
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        fieldPresenters.forEach(formFieldPresenter -> formFieldPresenter.setEnabled(enabled));
    }

    public void setGridOrderByChangedHandler(FormRegionOrderingChangedHandler handler) {
        this.orderByChangedHandler = checkNotNull(handler);
    }

    public void setFormRegionPageChangedHandler(FormRegionPageChangedHandler handler) {
        this.formRegionPageChangedHandler = checkNotNull(handler);
        fieldPresenters.forEach(formFieldPresenter -> formFieldPresenter.setFormRegionPageChangedHandler(
                newRegionPageChangedHandler()));
    }

    private void updateFormData(@Nonnull FormDataDto formData) {
        GWT.log("[FormPresenter] Updating form data");
        this.currentSubject = formData.getSubject();
        dispatchServiceManager.beginBatch();
        ImmutableList<FormFieldDataDto> nextFormFieldData = formData.getFormFieldData();
        for (int i = 0; i < nextFormFieldData.size(); i++) {
            FormFieldDataDto fieldData = nextFormFieldData.get(i);
            FormFieldPresenter formFieldPresenter = fieldPresenters.get(i);
            formFieldPresenter.setValue(fieldData);
        }
        dispatchServiceManager.executeCurrentBatch();
    }

    /**
     * Creates the form from scratch and fills in the specified form data.
     *
     * @param formData The form data to be filled into the form.
     */
    private void createFormAndSetFormData(@Nonnull FormDataDto formData) {
        clear();
        FormDescriptorDto formDescriptor = formData.getFormDescriptor();
        currentFormDescriptor = Optional.of(formDescriptor);
        dispatchServiceManager.beginBatch();
        for (FormFieldDataDto fieldData : formData.getFormFieldData()) {
            addFormField(fieldData);
        }
        dispatchServiceManager.executeCurrentBatch();
    }

    public void clear() {
        saveExpansionState();
        fieldPresenters.clear();
        formView.clear();
        currentFormDescriptor = Optional.empty();
        container.ifPresent(c -> {
            if (displayNoFormViewIfEmpty) {
                c.setWidget(noFormView);
            }
        });
    }

    private void addFormField(@Nonnull FormFieldDataDto formFieldData) {
        FormFieldDescriptorDto formFieldDescriptor = formFieldData.getFormFieldDescriptor();
        if (formFieldDescriptor.getInitialExpansionState().equals(ExpansionState.COLLAPSED)) {
            collapsedFields.add(formFieldData.getFormFieldDescriptor().getId());
        }
        FormFieldPresenter presenter = formFieldPresenterFactory.create(formFieldDescriptor);
        presenter.setEnabled(enabled);
        presenter.setFormRegionPageChangedHandler(newRegionPageChangedHandler());
        presenter.setFormRegionFilterChangedHandler(formRegionFilterChangeHandler);
        presenter.setGridOrderByChangedHandler(orderByChangedHandler);
        presenter.setFormDataChangedHander(formDataChangedHandler);
        presenter.setCollapsible(fieldsCollapsible);
        presenter.start();
        presenter.setParentDisplayContextBuilder(this);
        fieldPresenters.add(presenter);
        if (fieldsCollapsible && collapsedFields.contains(formFieldData.getFormFieldDescriptor().getId())) {
            presenter.setExpansionState(ExpansionState.COLLAPSED);
        }
        // TODO : Change handler
        presenter.setValue(formFieldData);
        FormFieldView formFieldView = presenter.getFormFieldView();
        formView.addFormElementView(formFieldView, formFieldDescriptor.getFieldRun());
    }

    private RegionPageChangedHandler newRegionPageChangedHandler() {
        return () -> {
            formId.ifPresent(id -> {
                formRegionPageChangedHandler.handleFormRegionPageChanged(FormRegionPageChangedEvent.get(id));
            });
        };
    }

    public void expandAll() {
        fieldPresenters.forEach(p -> p.setExpansionState(ExpansionState.EXPANDED));
    }

    public void setFieldsCollapsible(boolean collapsible) {
        this.fieldsCollapsible = collapsible;
        fieldPresenters.forEach(presenter -> {
            presenter.setCollapsible(collapsible);
        });
    }

    /**
     * Gets the data that is held by the form being presented.  Only present values are
     * returned.
     *
     * @return The {@link FormData} entered into the form.
     */
    @Nonnull
    public Optional<FormData> getFormData() {
        return currentFormDescriptor.flatMap(formDescriptor -> {
            ImmutableList<FormFieldData> formFieldData = fieldPresenters.stream()
                                                                        .filter(FormFieldPresenter::isNonEmpty)
                                                                        .map(FormFieldPresenter::getValue)
                                                                        .collect(toImmutableList());

            if(formFieldData.isEmpty()) {
                return Optional.empty();
            }
            else {
                return Optional.of(FormData.get(currentSubject.map(FormSubjectDto::toFormSubject),
                                                formDescriptor.toFormDescriptor(),
                                                formFieldData));
            }
        });
    }

    @Nonnull
    public ImmutableList<FormPageRequest> getPageRequest() {
        return currentFormDescriptor.map(formDescriptor -> currentSubject.map(subject -> fieldPresenters.stream()
                                                                                                        .map(formFieldPresenter -> formFieldPresenter
                                                                                                                .getPageRequests(
                                                                                                                        subject.toFormSubject()))
                                                                                                        .flatMap(
                                                                                                                ImmutableList::stream)
                                                                                                        .map(pr -> FormPageRequest
                                                                                                                .get(formDescriptor
                                                                                                                             .getFormId(),
                                                                                                                     subject.toFormSubject(),
                                                                                                                     pr.getFieldId(),
                                                                                                                     pr.getSourceType(),
                                                                                                                     pr.getPageRequest()))
                                                                                                        .collect(
                                                                                                                toImmutableList()))
                                                                         .orElse(ImmutableList.of()))
                                    .orElse(ImmutableList.of());
    }

    public IsWidget getView() {
        return formView;
    }

    public void requestFocus() {
        formView.requestFocus();
    }

    public void setFormDataChangedHandler(FormDataChangedHandler formDataChangedHandler) {
        this.formDataChangedHandler = checkNotNull(formDataChangedHandler);
        fieldPresenters.forEach(presenter -> presenter.setFormDataChangedHander(formDataChangedHandler));
    }

    /**
     * Starts the form presenter.  The form will be placed into the specified listContainer.
     *
     * @param container The listContainer.
     */
    public void start(@Nonnull AcceptsOneWidget container) {
        this.container = Optional.of(container);
        if (displayNoFormViewIfEmpty) {
            container.setWidget(noFormView);
        }
    }

    public Stream<FormRegionOrdering> getOrderings() {
        return fieldPresenters.stream().flatMap(FormFieldPresenter::getOrderings);
    }

    public ImmutableSet<FormRegionFilter> getFilters() {
        return fieldPresenters.stream()
                              .flatMap(formFieldPresenter -> formFieldPresenter.getFilters().stream())
                              .collect(toImmutableSet());
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {
        formRegionFilterChangeHandler = checkNotNull(handler);
        fieldPresenters.forEach(formFieldPresenter -> {
            formFieldPresenter.setFormRegionFilterChangedHandler(handler);
        });
    }

    @Inject
    public ValidationStatus getValidationStatus() {
        return fieldPresenters.stream()
                              .map(FormFieldPresenter::getValidationStatus)
                              .filter(ValidationStatus::isInvalid)
                              .findFirst()
                              .orElse(ValidationStatus.VALID);
    }

    @Override
    public void setParentDisplayContextBuilder(HasDisplayContextBuilder parent) {
        this.displayContextManager.setParentDisplayContextBuilder(parent);
    }

    private void fillDisplayContext(DisplayContextBuilder context) {
        currentFormDescriptor.ifPresent(fd -> context.addFormId(fd.getFormId()));
    }

    @Override
    public DisplayContextBuilder fillDisplayContextBuilder() {
        return displayContextManager.fillDisplayContextBuilder();
    }

    public void setDisplayNoFormViewIfEmpty(boolean b) {
        displayNoFormViewIfEmpty = b;
    }
}

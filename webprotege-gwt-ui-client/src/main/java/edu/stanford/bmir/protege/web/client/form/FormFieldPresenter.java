package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import edu.stanford.bmir.protege.web.client.ui.DisplayContextManager;
import edu.stanford.bmir.protege.web.client.ui.HasDisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.DisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.RegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.ValidationStatus;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.form.field.Optionality.REQUIRED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
public class FormFieldPresenter implements FormRegionPresenter, HasFormRegionFilterChangedHandler, HasDisplayContextBuilder {

    private boolean enabled = true;

    private FormFieldValueChangedHandler formFieldValueChangedHandler = () -> {};

    private boolean collapsibile = true;

    private DisplayContextManager displayContextManager = new DisplayContextManager(this::fillDisplayContext);

    private void fillDisplayContext(DisplayContextBuilder context) {
        context.setFormFieldId(getFormRegionId());
    }

    private void handleFormControlValueChanged(ValueChangeEvent<List<FormControlData>> event) {
        updateRequiredValuePresent();
    }

    @Nonnull
    private final FormFieldView view;

    @Nonnull
    private final FormFieldDescriptorDto formFieldDescriptor;

    private ExpansionState expansionState = ExpansionState.EXPANDED;

    private Optional<FormFieldDataDto> currentValue = Optional.empty();

    private final FormControlStackPresenter stackPresenter;

    @Nonnull
    private Runnable beforeExpandRunner = () -> {};

    @Nonnull
    private final LanguageMapCurrentLocaleMapper languageMapCurrentLocaleMapper;

    public FormFieldPresenter(@Nonnull FormFieldView view,
                              @Nonnull FormFieldDescriptorDto formFieldDescriptor,
                              @Nonnull FormControlStackPresenter formControlStackPresenter,
                              @Nonnull LanguageMapCurrentLocaleMapper languageMapCurrentLocaleMapper) {
        this.view = checkNotNull(view);
        this.formFieldDescriptor = checkNotNull(formFieldDescriptor);
        this.stackPresenter = checkNotNull(formControlStackPresenter);
        this.languageMapCurrentLocaleMapper = checkNotNull(languageMapCurrentLocaleMapper);
        stackPresenter.addValueChangeHandler(event -> formFieldValueChangedHandler.handleFormFieldValueChanged());
    }

    @Nonnull
    @Override
    public FormRegionId getFormRegionId() {
        return formFieldDescriptor.getId();
    }

    @Nonnull
    public FormFieldView getFormFieldView() {
        return view;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        LanguageMap label = formFieldDescriptor.getLabel();
        boolean hasLabel = !label.asMap().isEmpty();
        view.setEditableIconVisible(hasLabel && enabled && formFieldDescriptor.isReadWrite());
        propagateEnabled();
    }

    protected FormFieldView start() {
        view.setId(formFieldDescriptor.getId());
        view.setFormLabel(languageMapCurrentLocaleMapper.getValueForCurrentLocale(formFieldDescriptor.getLabel()));
        view.setRequired(formFieldDescriptor.getOptionality());
        view.setHelpText(languageMapCurrentLocaleMapper.getValueForCurrentLocale(formFieldDescriptor.getHelp()));
        stackPresenter.start(view.getFormStackContainer());
        propagateEnabled();
        view.setHeaderClickedHandler(this::toggleExpansionState);
        // Update the required value missing display when the value changes
        stackPresenter.addValueChangeHandler(this::handleFormControlValueChanged);

        updateRequiredValuePresent();
        return view;
    }

    private void propagateEnabled() {
        if(formFieldDescriptor.isReadOnly() || formFieldDescriptor.getAccessMode().isReadOnly()) {
            stackPresenter.setEnabled(false);
        }
        else {
            stackPresenter.setEnabled(enabled);
        }
    }

    @Nonnull
    public ExpansionState getExpansionState() {
        return expansionState;
    }

    public void toggleExpansionState() {
        if(!collapsibile) {
            return;
        }
        if(expansionState == ExpansionState.EXPANDED) {
            setExpansionState(ExpansionState.COLLAPSED);
        }
        else {
            setExpansionState(ExpansionState.EXPANDED);
        }
    }

    public void setExpansionState(ExpansionState expansionState) {
        this.expansionState = expansionState;
        if(expansionState == ExpansionState.EXPANDED) {
            runBeforeExpand();
            view.expand();
        }
        else {
            view.collapse();
        }

    }

    @Nonnull
    public ImmutableList<FormRegionPageRequest> getPageRequests(@Nonnull FormSubject formSubject) {
        int pageSize = formFieldDescriptor.getPageSize();
        return stackPresenter.getPageRequests(formSubject, formFieldDescriptor.getId());
    }

    public Stream<FormRegionOrdering> getOrderings() {
        List<FormRegionOrdering> orderings = new ArrayList<>();
        stackPresenter.forEachFormControl(formControl -> {
            if(formControl instanceof GridControl) {
                ImmutableList<FormRegionOrdering> ordering = ((GridControl) formControl).getOrdering();
                orderings.addAll(ordering);
            }
        });
        return orderings.stream();
    }

    public FormFieldData getValue() {
        if(stackPresenter == null) {
            return FormFieldData.get(formFieldDescriptor.toFormFieldDescriptor(), Page.emptyPage());
        }
        runBeforeExpand();
        ImmutableList<FormControlData> formControlData = stackPresenter.getValue();

        Page<FormControlData> controlDataPage = Page.create(1, 1, formControlData, formControlData.size());
        return FormFieldData.get(formFieldDescriptor.toFormFieldDescriptor(), controlDataPage);
    }

    private void runBeforeExpand() {
        beforeExpandRunner.run();
        beforeExpandRunner = () -> {
        };
    }

    public void setValue(@Nonnull FormFieldDataDto formFieldData) {
        checkNotNull(formFieldData);
        if(currentValue.equals(Optional.of(formFieldData))) {
            return;
        }
        currentValue = Optional.of(formFieldData);
        if(!formFieldData.getFormFieldDescriptor().equals(formFieldDescriptor)) {
            throw new RuntimeException("FormFieldDescriptor mismatch for field: " + formFieldDescriptor.getId());
        }
        Runnable setValuesRunnable = () -> {
            Page<FormControlDataDto> page = formFieldData.getFormControlData();
            stackPresenter.setValue(page);
            stackPresenter.setPageCount(page.getPageCount());
            stackPresenter.setPageNumber(page.getPageNumber());
            updateRequiredValuePresent();
        };
        if(view.isExpanded()) {
            setValuesRunnable.run();
        }
        else {
            beforeExpandRunner = setValuesRunnable;
        }
    }

    public void clearValue() {
        currentValue = Optional.empty();
        stackPresenter.clearValue();
        updateRequiredValuePresent();
    }

    /**
     * Updates the specified view so that there is a visual indication if the value is required but not present.
     */
    private void updateRequiredValuePresent() {
        if (isValueRequired()) {
            boolean requiredValueNotPresent = !stackPresenter.isNonEmpty();
            view.setRequiredValueNotPresentVisible(requiredValueNotPresent);
        }
        else {
            view.setRequiredValueNotPresentVisible(false);
        }
    }

    private boolean isValueRequired() {
        return formFieldDescriptor.getOptionality() == REQUIRED;
    }

    public void setFormRegionPageChangedHandler(RegionPageChangedHandler regionPageChangedHandler) {
        stackPresenter.setRegionPageChangedHandler(regionPageChangedHandler);
    }


    public void setGridOrderByChangedHandler(FormRegionOrderingChangedHandler orderByChangedHandler) {
        stackPresenter.forEachFormControl(formControl -> {
            if(formControl instanceof GridControl) {
                ((GridControl) formControl).setGridOrderByChangedHandler(orderByChangedHandler);
            }
        });
    }

    @Nonnull
    public ImmutableSet<FormRegionFilter> getFilters() {
        // TODO: Filter for stack?

        ImmutableSet.Builder<FormRegionFilter> filters = ImmutableSet.builder();
        stackPresenter.forEachFormControl(formControl ->  {
            filters.addAll(formControl.getFilters());
        });
        return filters.build();
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {
        stackPresenter.setFormRegionFilterChangedHandler(handler);
    }

    public void setFormFieldValueChangedHandler(@Nonnull FormFieldValueChangedHandler handler) {
        this.formFieldValueChangedHandler = checkNotNull(handler);
    }

    public ValidationStatus getValidationStatus() {
        ValidationStatus validationStatus = stackPresenter.getValidationStatus();
        if(validationStatus.isInvalid()) {
            return validationStatus;
        }
        if(isValueRequired() && stackPresenter.isEmpty()) {
            return ValidationStatus.INVALID;
        }
        return ValidationStatus.VALID;
    }

    public void setFormDataChangedHander(FormDataChangedHandler formDataChangedHandler) {
        this.stackPresenter.setFormDataChangedHandler(formDataChangedHandler);
    }

    public void setCollapsible(boolean collapsible) {
        this.collapsibile = collapsible;
        view.setCollapsible(collapsible);
        if(!collapsible) {
            setExpansionState(ExpansionState.EXPANDED);
        }
    }

    public boolean isEmpty() {
        return stackPresenter.isEmpty();
    }

    public boolean isNonEmpty() {
        return stackPresenter.isNonEmpty();
    }

    @Override
    public void setParentDisplayContextBuilder(HasDisplayContextBuilder parent) {
        this.displayContextManager.setParentDisplayContextBuilder(parent);
    }

    @Override
    public DisplayContextBuilder fillDisplayContextBuilder() {
        return displayContextManager.fillDisplayContextBuilder();
    }

    @Override
    public String toString() {
        return "FormFieldPresenter{" +
               "formRegionId=" + formFieldDescriptor.getId() +
               ", controlType=" + formFieldDescriptor.getFormControlDescriptor().toFormControlDescriptor().getAssociatedType() +
               '}';
    }
}

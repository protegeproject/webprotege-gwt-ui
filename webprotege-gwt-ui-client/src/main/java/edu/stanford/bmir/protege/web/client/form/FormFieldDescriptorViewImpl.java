package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.access.CapabilityId;
import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.FormRegionAccessRestriction;
import edu.stanford.bmir.protege.web.shared.form.field.FieldRun;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDeprecationStrategy;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class FormFieldDescriptorViewImpl extends Composite implements FormFieldDescriptorView {


    private Consumer<LanguageMap> labelChangedHander = label -> {};

    interface FormFieldDescriptorEditorViewImplUiBinder extends UiBinder<HTMLPanel, FormFieldDescriptorViewImpl> {

    }

    private static FormFieldDescriptorEditorViewImplUiBinder ourUiBinder = GWT.create(
            FormFieldDescriptorEditorViewImplUiBinder.class);

    @UiField
    RadioButton optionalRadio;

    @UiField
    RadioButton requiredRadio;

    @UiField(provided = true)
    LanguageMapEditor labelEditor;

    @UiField(provided = true)
    LanguageMapEditor helpEditor;


    @UiField
    RepeatabilityView repeatabilityView;

    @UiField
    RadioButton elementRunStartRadio;

    @UiField
    RadioButton elementRunContinueRadio;

    @UiField(provided = true)
    protected static Counter counter = new Counter();

    @UiField
    SimplePanel fieldViewContainer;

    @UiField
    SimplePanel bindingViewContainer;
    @UiField
    CheckBox readOnlyCheckBox;
    @UiField
    RadioButton initialExpansionStateExpanded;
    @UiField
    RadioButton initialExpansionStateCollapsed;

    @UiField
    RadioButton deprecationStrategyLeaveValuesIntactRadio;

    @UiField
    RadioButton deprecationStrategyDeleteValuesRadio;

    @UiField
    HTMLPanel deprecationStrategyView;

    @UiField(provided = true)
    ValueListEditor<RoleCriteriaBinding> viewAccessRestrictionsListEditor;

    @UiField(provided = true)
    ValueListEditor<RoleCriteriaBinding> editAccessRestrictionsListEditor;

    @Inject
    public FormFieldDescriptorViewImpl(LanguageMapEditor labelEditor,
                                       LanguageMapEditor helpEditor,
                                       Provider<FormRegionRoleCriteriaValueEditor> formRegionAccessRestrictionValueEditorProvider) {
        counter.increment();
        this.labelEditor = labelEditor;
        this.helpEditor = helpEditor;
        ValueEditorFactory<RoleCriteriaBinding> viewCapEditorFactory = formRegionAccessRestrictionValueEditorProvider::get;
        this.viewAccessRestrictionsListEditor = new ValueListFlexEditorImpl<>(viewCapEditorFactory);

        ValueEditorFactory<RoleCriteriaBinding> editCapEditorFactory = formRegionAccessRestrictionValueEditorProvider::get;
        this.editAccessRestrictionsListEditor = new ValueListFlexEditorImpl<>(editCapEditorFactory);
        this.editAccessRestrictionsListEditor.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        this.editAccessRestrictionsListEditor.setValue(new ArrayList<>());
        this.editAccessRestrictionsListEditor.setEnabled(true);
        initWidget(ourUiBinder.createAndBindUi(this));
        labelEditor.addValueChangeHandler(event -> {
            labelChangedHander.accept(getLabel());
        });

    }

    @Override
    public void requestFocus() {

    }

    @Override
    public void setHelp(@Nonnull LanguageMap help) {
        helpEditor.setValue(help);
    }

    @Nonnull
    @Override
    public LanguageMap getHelp() {
        return helpEditor.getValue().orElse(LanguageMap.empty());
    }

    @Override
    public void setLabel(@Nonnull LanguageMap label) {
        labelEditor.setValue(label);
    }

    @Nonnull
    @Override
    public LanguageMap getLabel() {
        return labelEditor.getValue().orElse(LanguageMap.empty());
    }

    @Override
    public void setFieldRun(@Nonnull FieldRun fieldRun) {
        if(fieldRun == FieldRun.CONTINUE) {
            elementRunContinueRadio.setValue(true);
        }
        else {
            elementRunStartRadio.setValue(true);
        }
    }

    @Nonnull
    @Override
    public FieldRun getFieldRun() {
        if(elementRunContinueRadio.getValue()) {
            return FieldRun.CONTINUE;
        }
        else {
            return FieldRun.START;
        }
    }

    @Override
    public void setOptionality(@Nonnull Optionality optionality) {
        GWT.log("[FormFieldDescriptorViewImpl] setOptionality");
        if(optionality == Optionality.OPTIONAL) {
            optionalRadio.setValue(true);
        }
        else {
            requiredRadio.setValue(true);
        }
    }

    @Nonnull
    @Override
    public Optionality getOptionality() {
        return optionalRadio.getValue() ? Optionality.OPTIONAL : Optionality.REQUIRED;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getOwlBindingViewContainer() {
        return bindingViewContainer;
    }

    @Override
    public void setRepeatability(@Nonnull Repeatability repeatability) {
        repeatabilityView.setRepeatability(repeatability);
    }

    @Nonnull
    @Override
    public Repeatability getRepeatability() {
        return repeatabilityView.getRepeatability();
    }

    @Override
    public void setLabelChangedHandler(@Nonnull Consumer<LanguageMap> runnable) {
        this.labelChangedHander = checkNotNull(runnable);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnlyCheckBox.setValue(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return readOnlyCheckBox.getValue();
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFieldDescriptorViewContainer() {
        return fieldViewContainer;
    }

    @Nonnull
    @Override
    public ExpansionState getInitialExpansionState() {
        if(initialExpansionStateExpanded.getValue()) {
            return ExpansionState.EXPANDED;
        }
        else {
            return ExpansionState.COLLAPSED;
        }
    }

    @Override
    public void setInitialExpansionState(@Nonnull ExpansionState expansionState) {
        if(expansionState.equals(ExpansionState.EXPANDED)) {
            initialExpansionStateExpanded.setValue(true);
        }
        else {
            initialExpansionStateCollapsed.setValue(true);
        }
    }

    @Override
    public void setDeprecationStrategyVisible(boolean visible) {
        deprecationStrategyView.setVisible(visible);
    }

    @Override
    public void setDeprecationStrategy(@Nonnull FormFieldDeprecationStrategy deprecationStrategy) {
        switch (deprecationStrategy) {
            case LEAVE_VALUES_INTACT:
                deprecationStrategyLeaveValuesIntactRadio.setValue(true);
                deprecationStrategyDeleteValuesRadio.setValue(false);
                break;
            case DELETE_VALUES:
                deprecationStrategyLeaveValuesIntactRadio.setValue(false);
                deprecationStrategyDeleteValuesRadio.setValue(true);
        }
    }

    @Nonnull
    @Override
    public FormFieldDeprecationStrategy getDeprecationStrategy() {
        if(!deprecationStrategyView.isVisible()) {
            return FormFieldDeprecationStrategy.LEAVE_VALUES_INTACT;
        }
        if(deprecationStrategyDeleteValuesRadio.getValue()) {
            return FormFieldDeprecationStrategy.DELETE_VALUES;
        }
        else {
            return FormFieldDeprecationStrategy.LEAVE_VALUES_INTACT;
        }
    }

    @Override
    public void setViewAccessRoles(List<RoleCriteriaBinding> restrictions) {
        viewAccessRestrictionsListEditor.setValue(restrictions);
    }

    @Override
    public List<RoleCriteriaBinding> getViewAccessRoles() {
        return viewAccessRestrictionsListEditor.getValue().orElse(Collections.emptyList());
    }

    @Override
    public void setEditAccessRoles(List<RoleCriteriaBinding> restrictions) {
        editAccessRestrictionsListEditor.setValue(restrictions);
    }

    @Override
    public List<RoleCriteriaBinding> getEditAccessRoles() {
        return editAccessRestrictionsListEditor.getValue().orElse(Collections.emptyList());
    }
}

package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.primitive.*;
import edu.stanford.bmir.protege.web.shared.*;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.ValidationStatus;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.LineMode;
import edu.stanford.bmir.protege.web.shared.form.field.StringType;
import edu.stanford.bmir.protege.web.shared.form.field.TextControlDescriptor;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class TextControl extends Composite implements FormControl {

    private StringType stringType = StringType.SIMPLE_STRING;

    private TextControlDescriptor descriptor = null;

    private ValidationStatus validationStatus = ValidationStatus.VALID;

    @Nonnull
    private FormDataChangedHandler formDataChangedHandler = () -> {};

    interface TextControlUiBinder extends UiBinder<HTMLPanel, TextControl> {

    }

    private static TextControlUiBinder ourUiBinder = GWT.create(TextControlUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl editor;

    @UiField(provided = true)
    DefaultLanguageEditor languageEditor;

    @UiField
    Label patternViolationErrorMessageLabel;

    private Optional<String> pattern = Optional.empty();

    private Optional<String> patternViolationErrorMessage = Optional.empty();

    private Optional<String> langPattern = Optional.empty();

    private Optional<String> langPatternViolationErrorMessage = Optional.empty();

    @Inject
    public TextControl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        this.editor = (PrimitiveDataEditorImpl) primitiveDataEditorProvider.get();
        this.languageEditor = (DefaultLanguageEditor) editor.getLanguageEditor();
        initWidget(ourUiBinder.createAndBindUi(this));
        editor.setAllowedTypes(Collections.singleton(PrimitiveType.LITERAL));
        editor.addValueChangeHandler(event -> {
            validateInput();
            formDataChangedHandler.handleFormDataChanged();
            ValueChangeEvent.fire(TextControl.this, getValue());
        });
        editor.setFreshEntitiesSuggestStrategy(new NullFreshEntitySuggestStrategy());
        editor.setShowLinksForEntities(false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        editor.setEnabled(enabled);
        languageEditor.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return editor.isEnabled();
    }

    public void setDescriptor(@Nonnull TextControlDescriptor descriptor) {
        this.descriptor = descriptor;
        String localeName = LocaleInfo.getCurrentLocale()
                                      .getLocaleName();
        setPlaceholder(descriptor.getPlaceholder().get(localeName));
        setLineMode(descriptor.getLineMode());
        setStringType(descriptor.getStringType());
        if(!descriptor.getPattern().isEmpty()) {
            setPattern(descriptor.getPattern());
            setPatternViolationErrorMessage(descriptor.getPatternViolationErrorMessage()
                                                                           .get(localeName));
        }
    }

    @Override
    public void requestFocus() {
        editor.requestFocus();
    }

    private void setPlaceholder(String placeholder) {
        editor.setPlaceholder(placeholder);
    }

    private void setPattern(String pattern) {
        this.pattern = Optional.of(checkNotNull(pattern));
    }

    private void setPatternViolationErrorMessage(String patternViolationErrorMessage) {
        this.patternViolationErrorMessage = Optional.of(checkNotNull(patternViolationErrorMessage));
    }

    private void validateInput() {
        validationStatus = ValidationStatus.VALID;
        if(pattern.isPresent()) {
            RegExp regExp = RegExp.compile(pattern.get());
            String value = editor.getText().trim();
            MatchResult mr = regExp.exec(value);
            if(mr == null) {
                patternViolationErrorMessage.ifPresent(s -> {
                    validationStatus = ValidationStatus.INVALID;
                    patternViolationErrorMessageLabel.setVisible(true);
                    patternViolationErrorMessageLabel.setText(s);
                });
                displayErrorBorder();
            }
            else {
                patternViolationErrorMessageLabel.setVisible(false);
                clearErrorBorder();
            }

        }
    }

    private void clearErrorBorder() {
        editor.removeStyleName(BUNDLE.style().errorBorder());
        editor.addStyleName(BUNDLE.style().noErrorBorder());
        patternViolationErrorMessageLabel.setVisible(false);
    }

    private void displayErrorBorder() {
        editor.removeStyleName(BUNDLE.style().noErrorBorder());
        editor.addStyleName(BUNDLE.style().errorBorder());
        patternViolationErrorMessageLabel.setVisible(true);
    }

    private void setStringType(StringType stringType) {
        if(stringType == StringType.SIMPLE_STRING  || stringType == StringType.SPECIFIC_LANG_STRING) {
            languageEditor.setVisible(false);
        }
        else {
            languageEditor.setVisible(true);
        }
        this.stringType = stringType;
    }

    private void setLineMode(LineMode lineMode) {
        if (lineMode == LineMode.MULTI_LINE) {
            editor.setMode(PrimitiveDataEditorView.Mode.MULTI_LINE);
        }
        else {
            editor.setMode(PrimitiveDataEditorView.Mode.SINGLE_LINE);
        }
    }

    @Override
    public void setValue(@Nonnull FormControlDataDto data) {
        if(data instanceof TextControlDataDto) {
            Optional<OWLLiteral> value = ((TextControlDataDto) data).getValue();
            if(!value.isPresent()) {
                clearValue();
            }
            else {
                editor.setValue(OWLLiteralData.get(value.get()));
                validateInput();
            }
        }
        else {
            clearValue();
        }

    }

    @Override
    public void clearValue() {
        editor.clearValue();
        clearErrorBorder();
        validationStatus = ValidationStatus.VALID;
    }

    @Nonnull
    @Override
    public ImmutableSet<FormRegionFilter> getFilters() {
        return ImmutableSet.of();
    }

    @Nonnull
    @Override
    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    @Override
    public void setFormDataChangedHandler(@Nonnull FormDataChangedHandler formDataChangedHandler) {
        this.formDataChangedHandler = checkNotNull(formDataChangedHandler);
    }

    @Override
    public Optional<FormControlData> getValue() {
        Optional<OWLPrimitiveData> editedValue = editor.getValue();
        if(!editedValue.isPresent()) {
            return Optional.empty();
        }
        OWLLiteralData literalData = (OWLLiteralData) editedValue.get();
        String lexicalValue = literalData.getLiteral().getLiteral().trim();
        if(lexicalValue.isEmpty()) {
            return Optional.empty();
        }
        if(stringType == StringType.SIMPLE_STRING) {
            // Preserve lang if one was originally set
            if(literalData.getLang().isEmpty()) {
                return Optional.of(TextControlData.get(descriptor, literalData.getLiteral()));
            }
            else {
                return Optional.of(TextControlData.get(descriptor, literalData.getLiteral()));
            }
        }
        else if(stringType == StringType.SPECIFIC_LANG_STRING) {
            String specificLangTag = descriptor.getSpecificLangTag().trim();
            OWLLiteral literalWithSepcificLang = DataFactory.getOWLLiteral(lexicalValue, specificLangTag);
            return Optional.of(TextControlData.get(descriptor, literalWithSepcificLang));
        }
        else {
            return Optional.of(TextControlData.get(descriptor, literalData.getLiteral()));
        }

    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormControlData>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {

    }
}

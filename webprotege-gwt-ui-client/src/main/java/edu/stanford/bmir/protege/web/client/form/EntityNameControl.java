package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.primitive.*;
import edu.stanford.bmir.protege.web.shared.DisplayContext;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.ValidationStatus;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.EntityNameControlDescriptorDto;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;

import javax.annotation.Nonnull;
import javax.inject.*;
import java.util.Optional;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/04/16
 */
public class EntityNameControl extends Composite implements FormControl, HasPlaceholder, ContextSensitiveControl {

    private final static java.util.logging.Logger logger = Logger.getLogger("EntityNameControl");

    private EntityNameControlDescriptorDto descriptor;

    private FormDataChangedHandler formDataChangedHandler = () -> {
    };

    interface EntityNameControlUiBinder extends UiBinder<HTMLPanel, EntityNameControl> {

    }

    private static EntityNameControlUiBinder ourUiBinder = GWT.create(EntityNameControlUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl editor;


    @Inject
    public EntityNameControl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        editor = (PrimitiveDataEditorImpl) primitiveDataEditorProvider.get();
        initWidget(ourUiBinder.createAndBindUi(this));
        editor.addValueChangeHandler(this::handleEditorValueChanged);
        editor.setAutoSelectSuggestions(true);
        editor.setClassesAllowed(true);
        editor.setNamedIndividualsAllowed(true);
        editor.setObjectPropertiesAllowed(true);
        editor.setDataPropertiesAllowed(true);
        editor.setAnnotationPropertiesAllowed(true);
        editor.setDatatypesAllowed(true);
    }

    public void setDescriptor(@Nonnull EntityNameControlDescriptorDto descriptor) {
        logger.info("geo entityNameControl " + descriptor);
        this.descriptor = checkNotNull(descriptor);
        descriptor.getMatchCriteria().ifPresent(c -> editor.setCriteria(c));
        LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
        editor.setPlaceholder(descriptor.getPlaceholder()
                .get(localeInfo.getLocaleName()));
    }

    private void handleEditorValueChanged(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
        formDataChangedHandler.handleFormDataChanged();
        ValueChangeEvent.fire(EntityNameControl.this, getValue());
    }

    @Override
    public void setValue(@Nonnull FormControlDataDto object) {
        if (object instanceof EntityNameControlDataDto) {
            EntityNameControlDataDto data = (EntityNameControlDataDto) object;
            data.getEntity().ifPresent(editor::setValue);
        } else {
            editor.clearValue();
        }

    }

    @Override
    public void requestFocus() {
        editor.requestFocus();
    }

    @Override
    public String getPlaceholder() {
        return editor.getPlaceholder();
    }

    @Override
    public void setPlaceholder(String placeholder) {
        editor.setPlaceholder(placeholder);
    }

    @Override
    public void clearValue() {
        editor.clearValue();
    }

    @Nonnull
    @Override
    public ImmutableSet<FormRegionFilter> getFilters() {
        return ImmutableSet.of();
    }

    @Nonnull
    @Override
    public ValidationStatus getValidationStatus() {
        return ValidationStatus.VALID;
    }

    @Override
    public void setFormDataChangedHandler(@Nonnull FormDataChangedHandler formDataChangedHandler) {
        this.formDataChangedHandler = checkNotNull(formDataChangedHandler);
    }

    @Override
    public Optional<FormControlData> getValue() {
        return editor.getValue()
                .flatMap(OWLPrimitiveData::asEntity)
                .map(entity -> EntityNameControlData.get(descriptor.toFormControlDescriptor(), entity));
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormControlData>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void setEnabled(boolean enabled) {
        editor.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return editor.isEnabled();
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {

    }

    @Override
    public void updateContextSensitiveCriteria(DisplayContext context) {
        descriptor.getMatchCriteria().ifPresent(c -> {
            ImmutableList<RootCriteria> criteriaList = c.getRootCriteria()
                    .stream()
                    .map(rootCriteria -> {
                        logger.info("geo log: suntem in update contextSEnsitiveCriteria");
                                if (rootCriteria instanceof ContextSensitiveCriteria) {
                                    logger.info("geo log: inainte de getEffectiveCriteria :"+context);
                                    return ((ContextSensitiveCriteria) rootCriteria).getEffectiveCriteria(context);
                                }
                                return rootCriteria;
                            }
                    ).collect(ImmutableList.toImmutableList());
            CompositeRootCriteria newDynamicCriteria = CompositeRootCriteria.get(criteriaList, c.getMatchType());
            editor.clearCriteria();
            editor.setCriteria(newDynamicCriteria);
        });
    }
}

package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public class EntityRelationshipCriteriaViewImpl extends Composite implements EntityRelationshipCriteriaView {

    private Runnable valueMatchTypeChangedHandler = () -> {};

    interface EntityRelationshipCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, EntityRelationshipCriteriaViewImpl> {

    }

    private static EntityRelationshipCriteriaViewImplUiBinder ourUiBinder = GWT.create(
            EntityRelationshipCriteriaViewImplUiBinder.class);

    @UiField
    SimplePanel valueMatchCriteriaContainer;

    @UiField(provided = true)
    PrimitiveDataEditor propertyField;

    @Inject
    public EntityRelationshipCriteriaViewImpl(@Nonnull PrimitiveDataEditor propertyField) {
        this.propertyField = propertyField;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clearProperty() {
        propertyField.clearValue();
    }

    @Override
    public void setProperty(@Nonnull OWLPropertyData propertyData) {
        propertyField.setValue(propertyData);
    }

    @Nonnull
    @Override
    public Optional<OWLPropertyData> getProperty() {
        return propertyField.getValue()
                .filter(propertyData -> propertyData instanceof OWLPropertyData)
                .map(propertyData -> (OWLPropertyData) propertyData);
    }

    @Override
    public void setPropertyChangeHandler(@Nonnull Runnable handler) {
        propertyField.addValueChangeHandler(event -> handler.run());
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getValueCriteriaContainer() {
        return valueMatchCriteriaContainer;
    }
}

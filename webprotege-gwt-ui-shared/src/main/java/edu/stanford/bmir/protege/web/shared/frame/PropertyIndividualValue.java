package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("PropertyIndividualValue")
public abstract class PropertyIndividualValue extends ObjectPropertyValue {

    @JsonCreator
    @Nonnull
    public static PropertyIndividualValue get(@JsonProperty("property") @Nonnull OWLObjectPropertyData property,
                                              @JsonProperty("value") @Nonnull OWLNamedIndividualData value,
                                              @JsonProperty("state") @Nonnull State state) {
        return new AutoValue_PropertyIndividualValue(property,
                                                     value,
                                                     state);
    }

    @Override
    public abstract OWLObjectPropertyData getProperty();

    @Override
    public abstract OWLNamedIndividualData getValue();

    @Override
    public abstract State getState();

    @Override
    public boolean isValueMostSpecific() {
        return true;
    }

    @Override
    public boolean isAnnotation() {
        return false;
    }

    @Override
    public boolean isLogical() {
        return true;
    }

    @Override
    public <R, E extends Throwable> R accept(PropertyValueVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    protected PropertyValue duplicateWithState(State state) {
        return PropertyIndividualValue.get(getProperty(), getValue(), state);
    }

    @Nonnull
    @Override
    public PlainPropertyIndividualValue toPlainPropertyValue() {
        return PlainPropertyIndividualValue.get(
                getProperty().getEntity(),
                getValue().getEntity(),
                getState()
        );
    }
}

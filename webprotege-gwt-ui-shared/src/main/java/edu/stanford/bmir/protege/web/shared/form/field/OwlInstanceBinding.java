package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-26
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(OwlInstanceBinding.TYPE)
public abstract class OwlInstanceBinding implements OwlBinding {

    protected static final String TYPE = "INSTANCE";

    @JsonCreator
    @Nonnull
    public static OwlInstanceBinding get(@JsonProperty(PropertyNames.CRITERIA) @Nullable EntityMatchCriteria criteria) {
        return new AutoValue_OwlInstanceBinding(criteria);
    }

    @JsonProperty(PropertyNames.CRITERIA)
    @Nullable
    public abstract EntityMatchCriteria getCriteria();

    @Nonnull
    public static OwlInstanceBinding get() {
        return get(null);
    }

    @Nonnull
    @Override
    public Optional<OWLProperty> getOwlProperty() {
        return Optional.empty();
    }

}

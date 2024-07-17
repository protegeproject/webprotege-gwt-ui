package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class LiteralFormControlDataMatchCriteria implements PrimitiveFormControlDataMatchCriteria {

    @JsonCreator
    @Nonnull
    public static LiteralFormControlDataMatchCriteria get(@JsonProperty(PropertyNames.CRITERIA) @Nonnull LiteralCriteria literalCriteria) {
        return new AutoValue_LiteralFormControlDataMatchCriteria(literalCriteria);
    }

    @JsonProperty(PropertyNames.CRITERIA)
    @Nonnull
    public abstract LiteralCriteria getLexicalValueCriteria();

    @Override
    public <R> R accept(@Nonnull PrimitiveFormControlDataMatchCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

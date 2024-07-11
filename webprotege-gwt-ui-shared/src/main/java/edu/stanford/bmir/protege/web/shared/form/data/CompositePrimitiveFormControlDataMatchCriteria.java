package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class CompositePrimitiveFormControlDataMatchCriteria implements PrimitiveFormControlDataMatchCriteria {

    @Nonnull
    public static CompositePrimitiveFormControlDataMatchCriteria get(@JsonProperty(PropertyNames.CRITERIA) @Nonnull ImmutableList<? extends PrimitiveFormControlDataMatchCriteria> criteria,
                                                                     @JsonProperty(PropertyNames.MATCH_TYPE) @Nonnull MultiMatchType matchType) {
        return new AutoValue_CompositePrimitiveFormControlDataMatchCriteria(matchType, ImmutableList.copyOf(criteria));
    }

    @Nonnull
    public abstract MultiMatchType getMultiMatchType();

    @Nonnull
    public abstract ImmutableList<PrimitiveFormControlDataMatchCriteria> getCriteria();

    @Override
    public <R> R accept(@Nonnull PrimitiveFormControlDataMatchCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

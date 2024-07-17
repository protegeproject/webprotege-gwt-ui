package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(DynamicChoiceListSourceDescriptor.TYPE)
public abstract class DynamicChoiceListSourceDescriptor implements ChoiceListSourceDescriptor {

    public static final String TYPE = "Dynamic";

    @JsonCreator
    public static DynamicChoiceListSourceDescriptor get(@JsonProperty(PropertyNames.CRITERIA) @Nonnull RootCriteria criteria) {
        if (criteria instanceof CompositeRootCriteria) {
            return new AutoValue_DynamicChoiceListSourceDescriptor((CompositeRootCriteria) criteria);
        }
        else {
            CompositeRootCriteria wrapped = CompositeRootCriteria.get(
                    ImmutableList.of(criteria),
                    MultiMatchType.ALL
            );
            return new AutoValue_DynamicChoiceListSourceDescriptor(wrapped);
        }
    }

    @JsonProperty(PropertyNames.CRITERIA)
    @Nonnull
    public abstract CompositeRootCriteria getCriteria();

}

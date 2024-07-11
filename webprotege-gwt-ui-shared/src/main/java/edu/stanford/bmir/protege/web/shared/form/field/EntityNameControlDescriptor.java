package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityTypeIsOneOfCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeName(EntityNameControlDescriptor.TYPE)
@AutoValue
@GwtCompatible(serializable = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class EntityNameControlDescriptor implements FormControlDescriptor {

    protected static final String TYPE = "ENTITY_NAME";

    @JsonCreator
    @Nonnull
    public static EntityNameControlDescriptor get(@Nullable @JsonProperty(PropertyNames.PLACEHOLDER) LanguageMap languageMap,
                                                  @Nullable @JsonProperty(PropertyNames.CRITERIA) CompositeRootCriteria criteria) {
        return new AutoValue_EntityNameControlDescriptor(languageMap == null ? LanguageMap.empty() : languageMap,
                                                         criteria);
    }

    @Nonnull
    public static EntityNameControlDescriptor getDefault() {
        return new AutoValue_EntityNameControlDescriptor(LanguageMap.empty(),
                                                         getDefaultEntityMatchCriteria());
    }

    public static CompositeRootCriteria getDefaultEntityMatchCriteria() {
        return CompositeRootCriteria.get(
                ImmutableList.of(EntityTypeIsOneOfCriteria.get(
                        ImmutableSet.of(EntityType.CLASS)
                )),
                MultiMatchType.ALL
        );
    }

    public static String getFieldTypeId() {
        return TYPE;
    }

    @Nonnull
    @Override
    @JsonIgnore
    public String getAssociatedType() {
        return TYPE;
    }

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @JsonProperty(PropertyNames.PLACEHOLDER)
    public abstract LanguageMap getPlaceholder();

    @JsonProperty(PropertyNames.CRITERIA)
    @Nullable
    protected abstract CompositeRootCriteria getMatchCriteriaInternal();

    @Nonnull
    @JsonIgnore
    public Optional<CompositeRootCriteria> getMatchCriteria() {
        return Optional.ofNullable(getMatchCriteriaInternal());
    }
}

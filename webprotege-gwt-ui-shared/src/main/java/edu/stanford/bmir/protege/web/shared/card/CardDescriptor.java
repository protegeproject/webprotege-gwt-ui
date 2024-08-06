package edu.stanford.bmir.protege.web.shared.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import java.util.Set;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class CardDescriptor {

    @JsonProperty("id")
    public abstract CardId getId();

    @Nonnull
    @JsonProperty("label")
    public abstract LanguageMap getLabel();

    @Nonnull
    @JsonProperty("content")
    public abstract CardContentDescriptor getContentDescriptor();

    @Nonnull
    @JsonProperty("requiredActions")
    public abstract Set<ActionId> getRequiredActions();

    @Nonnull
    @JsonProperty("visibilityCriteria")
    public abstract CompositeRootCriteria getVisibilityCriteria();
}

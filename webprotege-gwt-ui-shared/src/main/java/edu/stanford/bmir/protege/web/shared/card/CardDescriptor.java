package edu.stanford.bmir.protege.web.shared.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class CardDescriptor {

    @JsonCreator
    public static CardDescriptor create(@JsonProperty("id") CardId cardId,
                                        @JsonProperty("label") LanguageMap label,
                                        @JsonProperty("color") Color color,
                                        @JsonProperty("backgroundColor") Color backgroundColor,
                                        @JsonProperty("content") CardContentDescriptor contentDescriptor,
                                        @JsonProperty("requiredReadActions") Set<ActionId> requiredReadActions,
                                        @JsonProperty("requiredWriteActions") Set<ActionId> requiredWriteActions,
                                        @JsonProperty("visibilityCriteria") CompositeRootCriteria criteria) {
        return new AutoValue_CardDescriptor(cardId, label, color, backgroundColor, contentDescriptor, requiredReadActions, requiredWriteActions, criteria);
    }

    @JsonProperty("id")
    public abstract CardId getId();

    @Nonnull
    @JsonProperty("label")
    public abstract LanguageMap getLabel();

    @Nullable
    @JsonProperty("color")
    public abstract Color getColorInternal();

    /**
     * Retrieves the color of the card.
     *
     * @return An Optional object containing the (foreground) color of the card, if it is present.
     *         Returns an empty Optional if the color is not set.  This is the same
     */
    @Nonnull
    @JsonIgnore
    public Optional<Color> getColor() {
        return Optional.ofNullable(getColorInternal());
    }

    /**
     * Retrieves the background color of the card.
     *
     * @return An Optional object containing the background color of the card, if it is present.
     *         Returns an empty Optional if the background color is not set.
     */
    @Nonnull
    @JsonIgnore
    public Optional<Color> getBackgroundColor() {
        return Optional.ofNullable(getBackgroundColorInternal());
    }

    @Nullable
    @JsonProperty("backgroundColor")
    public abstract Color getBackgroundColorInternal();

    @Nonnull
    @JsonProperty("content")
    public abstract CardContentDescriptor getContentDescriptor();

    @Nonnull
    @JsonProperty("requiredReadActions")
    public abstract Set<ActionId> getRequiredReadActions();

    @Nonnull
    @JsonProperty("requiredWriteActions")
    public abstract Set<ActionId> getRequiredWriteActions();

    @Nonnull
    @JsonProperty("visibilityCriteria")
    public abstract CompositeRootCriteria getVisibilityCriteria();
}

package edu.stanford.bmir.protege.web.client.card;

import edu.stanford.bmir.protege.web.client.tab.TabKeySerializer;
import edu.stanford.bmir.protege.web.shared.card.CardId;

import javax.annotation.Nonnull;

public class CardIdSerializer implements TabKeySerializer<CardId> {

    /**
     * Serializes a CardId object to a string representation.
     *
     * @param id the id to be serialized
     * @return a string representation of the serialized id
     * @throws NullPointerException if the provided id is null
     */
    @Nonnull
    @Override
    public String serialize(@Nonnull CardId id) {
        return id.value();
    }

    /**
     * Deserializes a string value to a CardId object.
     *
     * @param value the string value to be deserialized
     * @return the deserialized CardId object
     * @throws NullPointerException if the provided value is null
     */
    @Nonnull
    @Override
    public CardId deserialize(@Nonnull String value) {
        return CardId.create(value);
    }
}

package edu.stanford.bmir.protege.web.client.tab;

import javax.annotation.Nonnull;

/**
 * This interface represents a serializer/deserializer for tab keys.
 *
 * @param <K> the type of the key
 */
public interface TabKeySerializer<K> {

    /**
     * Serializes the given key to a string representation.
     *
     * @param key the key to be serialized
     * @return a string representation of the serialized key
     * @throws NullPointerException if the provided key is null
     */
    @Nonnull
    String serialize(@Nonnull K key);

    /**
     * Deserializes the given string key to its corresponding object representation.
     *
     * @param key the string key to be deserialized
     * @return the deserialized object key
     * @throws NullPointerException if the provided key is null
     */
    @Nonnull
    K deserialize(@Nonnull String key);
}

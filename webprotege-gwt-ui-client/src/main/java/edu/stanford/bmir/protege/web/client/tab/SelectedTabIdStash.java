package edu.stanford.bmir.protege.web.client.tab;

import edu.stanford.bmir.protege.web.client.portlet.HasNodeProperties;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class represents a stash for storing and retrieving selected tab keys.  The underlying functionality
 * will serialize the selected key to a node properties object.
 *
 * @param <K> the type of the key for the selected tab
 */
public class SelectedTabIdStash<K> {

    private static final String KEY = "forms-selected-form";

    @Nonnull
    private final HasNodeProperties nodeProperties;

    private final TabKeySerializer<K> tabKeySerializer;

    /**
     * Constreuct a {@link SelectedTabIdStash} that will serialize and deserialize a key for a selected tab to the
     * specified node properties object.
     */
    public SelectedTabIdStash(@Nonnull HasNodeProperties nodeProperties, TabKeySerializer<K> tabKeySerializer) {
        this.nodeProperties = checkNotNull(nodeProperties);
        this.tabKeySerializer = checkNotNull(tabKeySerializer);
    }

    public void stashSelectedTabKey(@Nonnull K key) {
        checkNotNull(key);
        String serializedKey = tabKeySerializer.serialize(key);
        nodeProperties.setNodeProperty(KEY, serializedKey);
    }

    @Nonnull
    public Optional<K> getSelectedKey() {
        return Optional.ofNullable(nodeProperties.getNodeProperty(KEY, null))
                .map(tabKeySerializer::deserialize);

    }
}

package edu.stanford.bmir.protege.web.shared.crud;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.HasDisplayName;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.bmir.protege.web.shared.crud.icatx.IcatxSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixKit;
import org.semanticweb.owlapi.model.IRI;

import java.io.Serializable;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 * <p>
 *     An {@code EntityCrudKit} is used by the system when creating and updating entities.  Each kit provides a human
 *     readable name, an editor for viewing and altering settings for the kit and a back end implementation that actually
 *     generates the ontology changes required to enact high level changes such as "creating" a fresh entity, or updating
 *     the display name.
 * </p>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(OBOIdSuffixKit.class),
        @JsonSubTypes.Type(UuidSuffixKit.class),
        @JsonSubTypes.Type(IcatxSuffixKit.class),
        @JsonSubTypes.Type(SuppliedNameSuffixKit.class)
})
public abstract class EntityCrudKit implements HasKitId, HasDisplayName, Serializable, IsSerializable {

    private EntityCrudKitId kitId;

    private String displayName;

    /**
     * For serialization purposes only!
     */
    protected EntityCrudKit() {
    }

    /**
     * Creates a descriptor for the specified information.
     * @param kitId The id of the kit. Not {@code null}.
     * @param displayName The display name for the kit.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public EntityCrudKit(EntityCrudKitId kitId, String displayName) {
        this.kitId = checkNotNull(kitId);
        this.displayName = checkNotNull(displayName);
    }

    /**
     * Gets the id for this kit.
     * @return The id.  Not {@code null}.
     */
    @Override
    public EntityCrudKitId getKitId() {
        return kitId;
    }

    /**
     * Gets the human readable display name for this kit.
     * @return The name.  Not {@code null}.
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

//    /**
//     * Gets an editor for viewing and altering the suffix settings.
//     * This method may only be called on the client side.
//     * @return An editor.  Not {@code null}.
//     */
//    public abstract EntityCrudKitSuffixSettingsEditor<S> getSuffixSettingsEditor();

    public abstract EntityCrudKitPrefixSettings getDefaultPrefixSettings();

    public abstract EntityCrudKitSuffixSettings getDefaultSuffixSettings();

    public abstract Optional<String> getPrefixValidationMessage(String prefix);

    public abstract IRI generateExample(EntityCrudKitPrefixSettings prefixSettings, EntityCrudKitSuffixSettings suffixSettings);


    @Override
    public String toString() {
        return toStringHelper("EntityCrudKit")
                .addValue(kitId)
                .add("displayName", displayName)
                .toString();
    }

    public abstract GeneratedAnnotationsSettings getDefaultGeneratedAnnotationsSettings();
}

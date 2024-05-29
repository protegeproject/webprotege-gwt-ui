package edu.stanford.bmir.protege.web.shared.crud.uuid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.semanticweb.owlapi.model.IRI;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
@ApplicationSingleton
@JsonTypeName("Uuid")
public class UuidSuffixKit extends EntityCrudKit implements Serializable, IsSerializable {

    private static final String EXAMPLE_SUFFIX = "RtvBaCCEyk09YwGRQljc2z";

    @Inject
    public UuidSuffixKit() {
        super(EntityCrudKitId.get("UUID"), "Auto-generated Universally Unique Id (UUID)");
    }

    @JsonCreator
    public static UuidSuffixKit get() {
        return new UuidSuffixKit();
    }

    public static EntityCrudKitId getId() {
        return EntityCrudKitId.get("UUID");
    }

    @Override
    public EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return EntityCrudKitPrefixSettings.get();
    }

    @Override
    public UuidSuffixSettings getDefaultSuffixSettings() {
        return UuidSuffixSettings.get();
    }

    @Override
    public Optional<String> getPrefixValidationMessage(String prefix) {
        if(!(prefix.endsWith("#") || prefix.endsWith("/"))) {
            return Optional.of("It is recommended that your prefix ends with a forward slash i.e. <b>/</b> (or a #)");
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public IRI generateExample(EntityCrudKitPrefixSettings prefixSettings, EntityCrudKitSuffixSettings suffixSettings) {
        return IRI.create(URL.encode(prefixSettings.getIRIPrefix()), EXAMPLE_SUFFIX);
    }

    @Override
    public GeneratedAnnotationsSettings getDefaultGeneratedAnnotationsSettings() {
        return GeneratedAnnotationsSettings.empty();
    }
}

package edu.stanford.bmir.protege.web.shared.crud.supplied;

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
 * Date: 14/08/2013
 */
@ApplicationSingleton
@JsonTypeName("SuppliedName")
public class SuppliedNameSuffixKit extends EntityCrudKit implements Serializable, IsSerializable {

    private static final EntityCrudKitId ID = EntityCrudKitId.get("SuppliedNameSuffix");

    @Inject
    public SuppliedNameSuffixKit() {
        super(ID, "Supplied name");
    }

    public static EntityCrudKitId getId() {
        return ID;
    }

    @Override
    public EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return EntityCrudKitPrefixSettings.get();
    }

    @Override
    public SuppliedNameSuffixSettings getDefaultSuffixSettings() {
        return SuppliedNameSuffixSettings.get();
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
        return IRI.create(URL.encode(prefixSettings.getIRIPrefix()), "Person");
    }

    @Override
    public GeneratedAnnotationsSettings getDefaultGeneratedAnnotationsSettings() {
        return GeneratedAnnotationsSettings.empty();
    }
}

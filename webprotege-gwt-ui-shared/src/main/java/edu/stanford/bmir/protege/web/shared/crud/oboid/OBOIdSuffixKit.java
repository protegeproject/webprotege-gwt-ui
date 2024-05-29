package edu.stanford.bmir.protege.web.shared.crud.oboid;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableList;
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
@JsonTypeName("OboId")
public class OBOIdSuffixKit extends EntityCrudKit implements Serializable, IsSerializable {

    public static final String DEFAULT_PREFIX = "http://purl.obolibrary.org/obo/ONT_";

    private static final EntityCrudKitId ID = EntityCrudKitId.get("OBO");

    @Inject
    public OBOIdSuffixKit() {
        super(ID, "Auto-generated  OBO Style Id");
    }

    public static EntityCrudKitId getId() {
        return ID;
    }

    @Override
    public EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return EntityCrudKitPrefixSettings.get(DEFAULT_PREFIX, ImmutableList.of());
    }

    @Override
    public OboIdSuffixSettings getDefaultSuffixSettings() {
        return OboIdSuffixSettings.get();
    }

    @Override
    public Optional<String> getPrefixValidationMessage(String prefix) {
        if(prefix.endsWith(DEFAULT_PREFIX)) {
            return Optional.of("The default prefix is specified.  You should change this to suit your ontology.");
        }
        else if(!prefix.endsWith("_")) {
            return Optional.of("OBO IRI prefixes should end with an underscore");
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public IRI generateExample(EntityCrudKitPrefixSettings prefixSettings, EntityCrudKitSuffixSettings suffixSettings) {
        StringBuilder sb = new StringBuilder();
        OboIdSuffixSettings oboIdSuffixSettings = (OboIdSuffixSettings) suffixSettings;
        for(int i = 0; i < oboIdSuffixSettings.getTotalDigits() - 1; i++) {
            sb.append("0");
        }
        sb.append("1");
        return IRI.create(URL.encode(prefixSettings.getIRIPrefix()), sb.toString());
    }

    @Override
    public GeneratedAnnotationsSettings getDefaultGeneratedAnnotationsSettings() {
        return GeneratedAnnotationsSettings.empty();
    }
}

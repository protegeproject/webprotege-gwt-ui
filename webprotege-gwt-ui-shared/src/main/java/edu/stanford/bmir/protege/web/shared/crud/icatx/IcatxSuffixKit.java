package edu.stanford.bmir.protege.web.shared.crud.icatx;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.semanticweb.owlapi.model.IRI;

import java.util.Optional;

@ApplicationSingleton
@JsonTypeName("IcatX")
public class IcatxSuffixKit  extends EntityCrudKit {
    public static final String DEFAULT_PREFIX = "http://who.int/whofic/";

    private static final EntityCrudKitId ID = EntityCrudKitId.get("ICATX");
    @Override
    public EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return EntityCrudKitPrefixSettings.get(DEFAULT_PREFIX, ImmutableList.of());
    }

    public static EntityCrudKitId getId() {
        return ID;
    }

    @Override
    public EntityCrudKitSuffixSettings getDefaultSuffixSettings() {
        return IcatxSuffixSettings.get();
    }

    @Override
    public Optional<String> getPrefixValidationMessage(String prefix) {
        if(prefix.endsWith(DEFAULT_PREFIX)) {
            return Optional.of("The default prefix is specified.  You should change this to suit your ontology.");
        }
        return Optional.empty();
    }

    @Override
    public IRI generateExample(EntityCrudKitPrefixSettings prefixSettings, EntityCrudKitSuffixSettings suffixSettings) {
        return IRI.create(prefixSettings.getIRIPrefix(), "1897656051");
    }

    @Override
    public GeneratedAnnotationsSettings getDefaultGeneratedAnnotationsSettings() {
        return GeneratedAnnotationsSettings.empty();
    }
}

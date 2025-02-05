package edu.stanford.bmir.protege.web.shared.crud.icatx;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;

import static edu.stanford.bmir.protege.web.shared.crud.icatx.IcatxSuffixSettings.TYPE_ID;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(TYPE_ID)
public class IcatxSuffixSettings extends EntityCrudKitSuffixSettings {

    public final static String TYPE_ID = "IcatXId";

    public static IcatxSuffixSettings get() {
        return new IcatxSuffixSettings();
    }


    @Override
    public EntityCrudKitId getKitId() {
        return IcatxSuffixKit.getId();
    }
}

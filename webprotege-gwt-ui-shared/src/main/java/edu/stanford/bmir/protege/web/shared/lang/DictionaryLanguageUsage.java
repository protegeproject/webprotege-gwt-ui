package edu.stanford.bmir.protege.web.shared.lang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Aug 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class DictionaryLanguageUsage {

    @JsonCreator
    public static DictionaryLanguageUsage get(@Nonnull @JsonProperty("dictionaryLanguage") DictionaryLanguage dictionaryLanguage,
                                              @JsonProperty("referenceCount") int referenceCount) {
        return new AutoValue_DictionaryLanguageUsage(dictionaryLanguage, referenceCount);
    }

    @Nonnull
    public abstract DictionaryLanguage getDictionaryLanguage();

    public abstract int getReferenceCount();
}

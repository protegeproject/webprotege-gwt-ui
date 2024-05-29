package edu.stanford.bmir.protege.web.shared.crud;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationsSettings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityCrudKitSettings implements Serializable {

    public static final String PREFIX_SETTINGS = "prefixSettings";

    public static final String SUFFIX_SETTINGS = "suffixSettings";

    public static final String GENERATED_ANNOTATIONS_SETTINGS = "generatedAnnotationsSettings";

    /**
     * Constructs an {@link EntityCrudKitSettings} object for the specified {@link EntityCrudKitPrefixSettings} and
     * {@link EntityCrudKitSuffixSettings}.
     *
     * @param prefixSettings The {@link EntityCrudKitPrefixSettings}. Not {@code null}.
     * @param suffixSettings The {@link EntityCrudKitSuffixSettings}. Not {@code null}.
     * @throws NullPointerException if any argument is {@code null}.
     */
    public static EntityCrudKitSettings get(@JsonProperty(PREFIX_SETTINGS) @Nonnull EntityCrudKitPrefixSettings prefixSettings,
                                                                                       @JsonProperty(SUFFIX_SETTINGS) EntityCrudKitSuffixSettings suffixSettings,
                                                                                       @JsonProperty(GENERATED_ANNOTATIONS_SETTINGS) @Nonnull GeneratedAnnotationsSettings generatedAnnotationsSettings) {
        return new AutoValue_EntityCrudKitSettings(prefixSettings, suffixSettings, generatedAnnotationsSettings);
    }

    @JsonCreator
    public static EntityCrudKitSettings fromJson(@JsonProperty(PREFIX_SETTINGS) @Nonnull EntityCrudKitPrefixSettings prefixSettings,
                                                                                            @JsonProperty(SUFFIX_SETTINGS) EntityCrudKitSuffixSettings suffixSettings,
                                                                                            @JsonProperty(GENERATED_ANNOTATIONS_SETTINGS) @Nullable GeneratedAnnotationsSettings generatedAnnotationsSettings) {
        return get(prefixSettings,
                   suffixSettings,
                   generatedAnnotationsSettings == null ? GeneratedAnnotationsSettings.empty() : generatedAnnotationsSettings);
    }

    /**
     * Gets the prefix settings for this settings object.
     *
     * @return The prefix settings.  Not {@code null}.
     */
    @JsonProperty(PREFIX_SETTINGS)
    public abstract EntityCrudKitPrefixSettings getPrefixSettings();

    /**
     * Gets the suffix settings for this object.
     *
     * @return The suffix settings. Not {@code null}.
     */
    @JsonProperty(SUFFIX_SETTINGS)
    public abstract EntityCrudKitSuffixSettings getSuffixSettings();

    @JsonProperty(GENERATED_ANNOTATIONS_SETTINGS)
    @Nonnull
    public abstract GeneratedAnnotationsSettings getGeneratedAnnotationsSettings();
}

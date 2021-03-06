package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;
import edu.stanford.bmir.protege.web.shared.shortform.ShortForm;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.protege.gwt.graphtree.shared.tree.HasTextRendering;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityNode implements IsSerializable, Serializable, Comparable<EntityNode>, HasTextRendering {

    private static final boolean NOT_DEPRECATED = false;

    private static final ImmutableSet<Watch> NO_WATCHES = ImmutableSet.of();

    private static final int NO_OPEN_COMMENTS = 0;

    private static final ImmutableSet<Tag> NO_ENTITY_TAGS = ImmutableSet.of();

    @Nonnull
    public static EntityNode get(@Nonnull OWLEntity entity,
                                 @Nonnull String browserText,
                                 @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms,
                                 boolean deprecated,
                                 @Nonnull Set<Watch> watches,
                                 int openCommentCount,
                                 Collection<Tag> tags) {
        return new AutoValue_EntityNode(entity,
                                        browserText,
                                        ImmutableSet.copyOf(tags),
                                        deprecated,
                                        ImmutableSet.copyOf(watches),
                                        openCommentCount,
                                        shortForms);
    }

    @JsonCreator
    @Nonnull
    public static EntityNode get(@JsonProperty("entity") @Nonnull OWLEntity entity,
                                 @JsonProperty("browserText") @Nonnull String browserText,
                                 @JsonProperty("shortForms") @Nonnull ImmutableList<ShortForm> shortForms,
                                 @JsonProperty("deprecated") boolean deprecated,
                                 @JsonProperty("watches") @Nonnull Set<Watch> watches,
                                 @JsonProperty("openCommentCount") int openCommentCount,
                                 @JsonProperty("tags") Collection<Tag> tags) {
        ImmutableMap<DictionaryLanguage, String> map = shortForms.stream()
                                                        .collect(toImmutableMap(ShortForm::getDictionaryLanguage,
                                                                                ShortForm::getShortForm));

        return get(entity,
                   browserText,
                   map,
                   deprecated,
                   ImmutableSet.copyOf(watches),
                   openCommentCount,
                   ImmutableSet.copyOf(tags));
    }

    /**
     * Gets a basic {@link EntityNode} for the specified {@link OWLEntityData}.  The
     * node will be rendered without any indications of deprecation status,
     * watches, open comments count, and entity tags
     *
     * @param entityData The entity data
     * @return A basic rendering of the specified entity data
     */
    @Nonnull
    public static EntityNode getFromEntityData(@Nonnull OWLEntityData entityData) {
        return get(entityData.getEntity(),
                   entityData.getBrowserText(),
                   entityData.getShortForms(),
                   NOT_DEPRECATED,
                   NO_WATCHES,
                   NO_OPEN_COMMENTS,
                   NO_ENTITY_TAGS);
    }

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    public abstract String getBrowserText();

    @JsonIgnore
    @Nonnull
    public String getText() {
        return getBrowserText();
    }

    public String getText(@Nonnull DictionaryLanguageData prefLang) {
        return getText(Collections.singletonList(prefLang.getDictionaryLanguage()), getBrowserText());
    }

    public String getText(@Nonnull List<DictionaryLanguage> prefLang, String defaultText) {
        return prefLang.stream()
                       .map(language -> getShortForms().get(language))
                       .filter(Objects::nonNull)
                       .findFirst()
                       .orElse(defaultText);
    }

    @JsonIgnore
    public OWLEntityData getEntityData() {
        return DataFactory.getOWLEntityData(getEntity(), getShortForms(), isDeprecated());
    }

    public abstract ImmutableSet<Tag> getTags();

    public abstract boolean isDeprecated();

    public abstract ImmutableSet<Watch> getWatches();

    public abstract int getOpenCommentCount();

    @JsonIgnore
    @Nonnull
    public abstract ImmutableMap<DictionaryLanguage, String> getShortForms();

    @JsonProperty("shortForms")
    public ImmutableList<ShortForm> getShortFormsList() {
        return getShortForms().entrySet()
                       .stream()
                       .map(entry -> ShortForm.get(entry.getKey(), entry.getValue()))
                       .collect(ImmutableList.toImmutableList());
    }

    @Override
    public int compareTo(EntityNode o) {
        return this.getBrowserText().compareToIgnoreCase(o.getBrowserText());
    }


}

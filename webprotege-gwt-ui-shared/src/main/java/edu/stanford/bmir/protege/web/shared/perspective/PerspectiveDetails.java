package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.protege.widgetmap.shared.node.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PerspectiveDetails implements IsSerializable, Serializable {


    @Nonnull
    @JsonCreator
    public static PerspectiveDetails get(@Nonnull @JsonProperty("perspectiveId") PerspectiveId perspectiveId,
                                         @Nonnull @JsonProperty("label") LanguageMap label,
                                         @JsonProperty("favorite") boolean favorite,
                                         @Nullable @JsonProperty("layout") Node layout) {
        return new AutoValue_PerspectiveDetails(perspectiveId, label, favorite, layout);
    }

    @Nonnull
    public abstract PerspectiveId getPerspectiveId();

    @Nonnull
    public abstract LanguageMap getLabel();

    public abstract boolean isFavorite();

    @Nullable
    protected abstract Node getLayoutInternal();

    @Nonnull
    public Optional<Node> getLayout() {
        return Optional.ofNullable(getLayoutInternal());
    }

    public PerspectiveDescriptor toPerspectiveDescriptor() {
        return PerspectiveDescriptor.get(getPerspectiveId(),
                                         getLabel(),
                                         isFavorite());
    }
}

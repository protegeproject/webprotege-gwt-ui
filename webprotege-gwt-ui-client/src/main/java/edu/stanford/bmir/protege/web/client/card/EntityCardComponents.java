package edu.stanford.bmir.protege.web.client.card;

import edu.stanford.bmir.protege.web.shared.card.CardDescriptor;
import edu.stanford.bmir.protege.web.shared.card.CardId;

import javax.annotation.Nonnull;
import java.util.Objects;

public class EntityCardComponents {

    private final CardId cardId;

    private final CardDescriptor descriptor;

    private final EntityCardPresenter presenter;

    private final EntityCardUi ui;

    public static EntityCardComponents get(@Nonnull CardDescriptor descriptor,
                                           @Nonnull EntityCardPresenter presenter,
                                           @Nonnull EntityCardUi ui) {
        return new EntityCardComponents(descriptor.getId(), descriptor, presenter, ui);
    }

    public EntityCardComponents(@Nonnull CardId cardId,
                                @Nonnull CardDescriptor descriptor,
                                @Nonnull EntityCardPresenter presenter,
                                @Nonnull EntityCardUi ui) {
        this.cardId = Objects.requireNonNull(cardId);
        this.descriptor = Objects.requireNonNull(descriptor);
        this.presenter = Objects.requireNonNull(presenter);
        this.ui = Objects.requireNonNull(ui);
    }

    @Nonnull
    public CardId getCardId() {
        return cardId;
    }

    @Nonnull
    public CardDescriptor getDescriptor() {
        return descriptor;
    }

    @Nonnull
    public EntityCardPresenter getPresenter() {
        return presenter;
    }

    @Nonnull
    public EntityCardUi getUi() {
        return ui;
    }
}

package edu.stanford.bmir.protege.web.client.card;

import edu.stanford.bmir.protege.web.shared.card.CardId;
import edu.stanford.bmir.protege.web.shared.card.CustomContentId;

import java.util.Optional;

public interface CustomContentEntityCardPresenterFactory {

    Optional<EntityCardPresenter> create(CardId cardId,
                                         CustomContentId customContentId);
}

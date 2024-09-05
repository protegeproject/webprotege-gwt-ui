package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.card.CardId;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import java.util.Optional;


public interface EntityCardPresenter {

    CardId getCardId();

    LanguageMap getLabel();

    Optional<Color> getColor();

    Optional<Color> getBackgroundColor();

    void start(AcceptsOneWidget container, WebProtegeEventBus eventBus);

    boolean isEditor();

    void setEditable(boolean editable);

    void commitChanges();

    void cancelChanges();

    void requestFocus();
}

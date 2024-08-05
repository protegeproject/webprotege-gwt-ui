package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;


public interface EntityCardPresenter {

    LanguageMap getLabel();

    void start(AcceptsOneWidget container, WebProtegeEventBus eventBus);
}

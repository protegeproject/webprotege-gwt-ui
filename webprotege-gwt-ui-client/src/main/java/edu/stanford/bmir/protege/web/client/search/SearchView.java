package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.search.DeprecatedEntitiesTreatment;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public interface SearchView extends HasBusy, IsWidget, HasInitialFocusable {

    interface IncrementSelectionHandler {
        void handleIncrementSelection();
    }

    interface DecrementSelectionHandler {
        void handleDecrementSelection();
    }

    void setIncrementSelectionHandler(@Nonnull IncrementSelectionHandler handler);

    void setDecrementSelectionHandler(@Nonnull DecrementSelectionHandler handler);

    void setAcceptKeyHandler(@Nonnull AcceptKeyHandler acceptKeyHandler);

    String getSearchString();

    void setSearchString(String text);

    void setSearchStringChangedHandler(SearchStringChangedHandler handler);

    void setLangTagFilterVisible(boolean visible);

    void setDeprecatedEntitiesTreatment(DeprecatedEntitiesTreatment deprecatedEntitiesTreatment);

    DeprecatedEntitiesTreatment getDeprecatedEntitiesTreatment();

    @Nonnull
    AcceptsOneWidget getLangTagFilterContainer();

    @Nonnull
    AcceptsOneWidget getSearchResultsContainer();

    void setSearchFilterVisible(boolean visible);

    @Nonnull
    AcceptsOneWidget getSearchFilterContainer();
}

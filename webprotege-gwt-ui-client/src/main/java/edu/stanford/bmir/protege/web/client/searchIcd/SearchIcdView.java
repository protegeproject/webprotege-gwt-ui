package edu.stanford.bmir.protege.web.client.searchIcd;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.search.SearchStringChangedHandler;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public interface SearchIcdView extends HasBusy, IsWidget, HasInitialFocusable {


    void setAcceptKeyHandler(@Nonnull AcceptKeyHandler acceptKeyHandler);

    void setSearchStringChangedHandler(SearchStringChangedHandler handler);

    String getSelectedURI();

    public void setSubtreeFilter(String icdSearchFilter);
}

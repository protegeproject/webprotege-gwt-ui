package edu.stanford.bmir.protege.web.client.searchIcd;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.search.SearchStringChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public interface SearchIcdView extends HasBusy, IsWidget, HasInitialFocusable {


    void setSearchStringChangedHandler(SearchStringChangedHandler handler);

    String getSelectedURI();

    void setSubtreeFilterText(EntityNode icdSearchFilter);


    String getInputFieldValue();

    void setInputFieldValue(String newValue);
}

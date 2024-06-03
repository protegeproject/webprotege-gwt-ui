package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Dec 2017
 */
public interface WhoCreateClassDialogView extends IsWidget, HasInitialFocusable {

    void setEntityType(@Nonnull EntityType<?> entityType);

    @Nonnull
    String getText();

    @Nonnull
    String getReasonForChange();

    void clear();

    @Nonnull
    AcceptsOneWidget getDuplicateEntityResultsContainer();

    void setEntitiesStringChangedHandler(EntitiesStringChangedHandler handler);

    boolean isReasonForChangeSet();
}

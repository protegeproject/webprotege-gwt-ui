package edu.stanford.bmir.protege.web.client.hierarchy.selectionModal;

import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;

import javax.annotation.Nonnull;

public interface HierarchySelectionModalView extends IsWidget, HasBusy {
    @Nonnull
    AcceptsOneWidget getHierarchyContainer();

    @Nonnull
    AcceptsOneWidget getEditorField();
}

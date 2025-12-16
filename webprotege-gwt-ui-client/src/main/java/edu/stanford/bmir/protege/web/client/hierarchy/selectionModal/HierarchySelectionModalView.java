package edu.stanford.bmir.protege.web.client.hierarchy.selectionModal;

import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface HierarchySelectionModalView extends IsWidget, HasBusy {
    @Nonnull
    AcceptsOneWidget getHierarchyContainer();

    @Nonnull
    AcceptsOneWidget getEditorField();

    void setSelectionHint(@Nullable String hint);
}

package edu.stanford.bmir.protege.web.client.hierarchy.selectionModal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.logging.Logger;

public class HierarchySelectionModalViewImpl extends Composite implements HierarchySelectionModalView {


    private static final Logger logger = Logger.getLogger(HierarchySelectionModalViewImpl.class.getName());

    @UiField
    public SimplePanel hierarchyContainer;

    @UiField
    public SimplePanel editorField;

    @UiField
    public Label selectionHint;

    interface HierarchySelectionModalViewImplUiBinder extends UiBinder<HTMLPanel, HierarchySelectionModalViewImpl> {
    }

    private static final HierarchySelectionModalViewImpl.HierarchySelectionModalViewImplUiBinder ourUiBinder = GWT
            .create(HierarchySelectionModalViewImpl.HierarchySelectionModalViewImplUiBinder.class);

    @Inject
    public HierarchySelectionModalViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setBusy(boolean busy) {

    }


    @Override
    protected void onAttach() {
        super.onAttach();
        com.google.gwt.dom.client.Element parent = getElement().getParentElement();
        if (parent != null && parent.hasClassName("wp-modal__content")) {
            parent.getStyle().setOverflow(com.google.gwt.dom.client.Style.Overflow.VISIBLE);
        }
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getHierarchyContainer() {
        return hierarchyContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getEditorField() {
        return editorField;
    }

    @Override
    public void setSelectionHint(@Nullable String hint) {
        if(hint == null) {
            selectionHint.setVisible(false);
            selectionHint.setText("");
            return;
        }
        selectionHint.setText(hint);
        selectionHint.setVisible(true);
    }
}

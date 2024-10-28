package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.scaleValueSelectionModal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.logging.Logger;

public class ScaleValueSelectionViewImpl extends Composite implements ScaleValueSelectionView {

    private final Messages messages;

    private static final Logger logger = Logger.getLogger(ScaleValueSelectionViewImpl.class.getName());

    @UiField
    public SimplePanel hierarchyContainer;

    interface ScaleValueSelectionModalImplUiBinder extends UiBinder<HTMLPanel, ScaleValueSelectionViewImpl> {
    }

    private static ScaleValueSelectionViewImpl.ScaleValueSelectionModalImplUiBinder ourUiBinder = GWT
            .create(ScaleValueSelectionModalImplUiBinder.class);

    @Inject
    public ScaleValueSelectionViewImpl(@Nonnull Messages messages) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.messages = messages;
    }

    @Override
    public void setBusy(boolean busy) {

    }


    @Override
    protected void onAttach() {
        super.onAttach();
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getHierarchyContainer() {
        return hierarchyContainer;
    }
}

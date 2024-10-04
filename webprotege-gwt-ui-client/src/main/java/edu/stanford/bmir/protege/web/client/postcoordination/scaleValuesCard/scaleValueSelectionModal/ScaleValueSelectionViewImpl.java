package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.scaleValueSelectionModal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxImpl;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.ScaleAllowMultiValue;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ScaleValueSelectionViewImpl extends Composite implements ScaleValueSelectionView {

    @UiField
    ExpandingTextBoxImpl scaleValueSelection;

    private final Messages messages;

    private static final Logger logger = Logger.getLogger(ScaleValueSelectionViewImpl.class.getName());

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


    @Override
    public void clear() {
        scaleValueSelection.setText("");
    }

    @Override
    public List<String> getText() {
        return Arrays.stream(scaleValueSelection.getText().trim().split("\n")).collect(Collectors.toList());
    }

    @Override
    public void setTopClass(String scaleTopClass) {

    }
}

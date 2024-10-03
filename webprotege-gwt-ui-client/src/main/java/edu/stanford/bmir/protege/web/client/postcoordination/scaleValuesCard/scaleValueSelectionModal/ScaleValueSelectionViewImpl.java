package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.scaleValueSelectionModal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxImpl;
import edu.stanford.bmir.protege.web.client.primitive.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;

import javax.annotation.Nonnull;
import javax.inject.*;
import java.util.*;
import java.util.logging.*;

public class ScaleValueSelectionViewImpl extends Composite implements ScaleValueSelectionView {

    @UiField
    ExpandingTextBoxImpl textBox;

    private final Messages messages;

    private static final Logger logger = Logger.getLogger(ScaleValueSelectionViewImpl.class.getName());

    interface ScaleValueSelectionModalImplUiBinder extends UiBinder<HTMLPanel, ScaleValueSelectionViewImpl> {
    }

    private static ScaleValueSelectionViewImpl.ScaleValueSelectionModalImplUiBinder ourUiBinder = GWT
            .create(ScaleValueSelectionModalImplUiBinder.class);

    @Inject
    public ScaleValueSelectionViewImpl(@Nonnull Messages messages) {
        this.messages = messages;
        textBox.setEnabled(false);
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
        textBox.setText("");
    }
}

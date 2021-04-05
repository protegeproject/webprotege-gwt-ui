package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.LinkedHashMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public class VizViewImpl extends Composite implements VizView {

    interface VizViewImplUiBinder extends UiBinder<HTMLPanel, VizViewImpl> {

    }

    private static final double ZOOM_DELTA = 0.05;

    private static final TransformCoordinates DEFAULT_TRANSFORM = TransformCoordinates.get(0, 0, 0.75);

    private static VizViewImplUiBinder ourUiBinder = GWT.create(VizViewImplUiBinder.class);

    private final LinkedHashMap<OWLEntity, TransformCoordinates> previousTransforms = new LinkedHashMap<>();

    private Runnable displaySettingsHandler = () -> {};

    @Nonnull
    private Runnable loadHandler = () -> {
    };

    @UiField
    HTMLPanel viewViewContainer;

    @Inject
    public VizViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
    @Override
    protected void onLoad() {
        super.onLoad();
        loadHandler.run();
    }


    @Override
    public void setWidget(IsWidget w) {
        viewViewContainer.clear();
        viewViewContainer.add(w);
    }

    @Override
    public void setDisplaySettingsHandler(Runnable displaySettingsHandler) {

    }
}

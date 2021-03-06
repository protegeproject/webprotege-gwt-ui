package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridRowViewImpl extends Composite implements GridRowView {

    interface GridRowViewImplUiBinder extends UiBinder<HTMLPanel, GridRowViewImpl> {

    }

    private static GridRowViewImplUiBinder ourUiBinder = GWT.create(GridRowViewImplUiBinder.class);

    @UiField
    HTMLPanel rowContainer;

    private List<SimplePanel> cellContainers = new ArrayList<>();

    @Inject
    public GridRowViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public GridCellContainer addCell() {
        SimplePanel cellContainer = new SimplePanel();
        cellContainers.add(cellContainer);
        String generalColumnStyle = WebProtegeClientBundle.BUNDLE.style()
                                                     .formGridColumn();
        cellContainer.addStyleName(generalColumnStyle);
        rowContainer.add(cellContainer);
        return new GridCellContainerImpl(cellContainer);
    }

    @Override
    public void clear() {
        rowContainer.clear();
        cellContainers.clear();
    }

    @Override
    public void setEditable(boolean enabled) {
        if (enabled) {
            rowContainer.addStyleName(WebProtegeClientBundle.BUNDLE.style().panelFilled());
            rowContainer.addStyleName(WebProtegeClientBundle.BUNDLE.style().formGridRowEditable());
        }
        else {
            rowContainer.removeStyleName(WebProtegeClientBundle.BUNDLE.style().panelFilled());
            rowContainer.removeStyleName(WebProtegeClientBundle.BUNDLE.style().formGridRowEditable());
        }
    }

    @Override
    public void requestFocus() {
        if(!cellContainers.isEmpty()) {
            SimplePanel container = cellContainers.get(0);
            Widget w = container.getWidget();
            if(w instanceof HasRequestFocus) {
                ((HasRequestFocus) w).requestFocus();
            }
        }
    }
}

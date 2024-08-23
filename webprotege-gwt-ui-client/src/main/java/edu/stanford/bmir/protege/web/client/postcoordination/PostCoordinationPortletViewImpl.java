package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.ConfigurableCheckbox;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.ScaleValueCardView;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.logging.Logger;

public class PostCoordinationPortletViewImpl extends Composite implements PostCoordinationPortletView {


    @UiField
    HTMLPanel paneContainer;

    @UiField
    protected FlexTable flexTable;
    @UiField
    VerticalPanel scaleValueCardList;

    private Map<String, PostCoordinationTableAxisLabel> labels;
    private Map<String, LinearizationDefinition> definitionMap;

    private final DispatchServiceManager dispatch;

    private static PostCoordinationTableResourceBundle.PostCoordinationTableCss style = PostCoordinationTableResourceBundle.INSTANCE.style();

    private static PostCoordinationPortletViewImpl.PostCoordinationPortletViewImplUiBinder ourUiBinder = GWT.create(PostCoordinationPortletViewImpl.PostCoordinationPortletViewImplUiBinder.class);

    @Inject
    public PostCoordinationPortletViewImpl(DispatchServiceManager dispatch) {
        initWidget(ourUiBinder.createAndBindUi(this));

        this.dispatch = dispatch;
        style.ensureInjected();
    }

    @Override
    public void setProjectId(ProjectId projectId) {

    }

    @Override
    public void setLabels(Map<String, PostCoordinationTableAxisLabel> labels) {
        this.labels = labels;
    }

    @Override
    public void setLinearizationDefinitonMap(Map<String, LinearizationDefinition> linearizationDefinitonMap) {
        this.definitionMap = linearizationDefinitonMap;
    }

    @Override
    public void setPostCoordinationEntity() {
        initializeTableHeader();
        initializeTableContent();
    }

    @Override
    public void setScaleValueCards(List<ScaleValueCardView> scaleValueCards) {
        scaleValueCards.forEach(scaleValue -> scaleValueCardList.add(scaleValue));
    }

    @Override
    public void setWidget(IsWidget w) {

    }

    @Override
    public void dispose() {

    }

    private void initializeTableHeader() {
        flexTable.setWidget(0, 0, new Label("Linearization / Use"));
        flexTable.getCellFormatter().addStyleName(0, 0, style.getHeaderLabel());
        List<PostCoordinationTableAxisLabel> labelList = new ArrayList<>(this.labels.values());
        for (int i = 0; i < labelList.size(); i++) {
            addHeaderCell(labelList.get(i).getTableLabel(), i + 1);
        }
        flexTable.getCellFormatter().addStyleName(0, 0, style.getPostCoordinationHeader());
        flexTable.setWidget(0, labelList.size() + 1, new Label("Linearization / Use"));
        flexTable.getCellFormatter().addStyleName(0, labelList.size() + 1, style.getHeaderLabel());
        flexTable.getCellFormatter().addStyleName(0, labelList.size() + 1, style.getPostCoordinationHeader());
        flexTable.addStyleName(style.getPostCoordinationTable());
        flexTable.getRowFormatter().addStyleName(0, style.getHeaderLabelRow());
    }

    private void initializeTableContent() {
        List<LinearizationDefinition> definitions = new ArrayList<>(this.definitionMap.values());
        for (int i = 0; i < definitions.size(); i++) {
            addRowLabel(definitions.get(i).getDisplayLabel(), i + 1, 0);
            List<PostCoordinationTableAxisLabel> labelList = new ArrayList<>(this.labels.values());
            for (int j = 0; j < labelList.size(); j++) {
                ConfigurableCheckbox configurableCheckbox = new ConfigurableCheckbox(new PostCoordinationCheckboxConfig(), "UNKNOWN");
                configurableCheckbox.setReadOnly(false);
                configurableCheckbox.setEnabled(true);
                PostCoordinationTableCell cell = new PostCoordinationTableCell(configurableCheckbox, definitions.get(i), labelList.get(j));
                flexTable.setWidget(i + 1, j + 1, cell.asWidget());
            }
            addRowLabel(definitions.get(i).getDisplayLabel(), i + 1, labelList.size()+1);
        }
    }

    private void addRowLabel(String label, int row, int column) {
        Widget rowLabel = new Label(label);
        flexTable.setWidget(row, column, rowLabel);
        flexTable.getCellFormatter().addStyleName(row, column, style.getRowLabel());
        flexTable.getRowFormatter().addStyleName(row, style.getCustomRowStyle());
    }

    private void addHeaderCell(String label, int position) {
        Widget headerCell = new Label(label);
        flexTable.setWidget(0, position, headerCell);
        flexTable.getCellFormatter().addStyleName(0, position, style.getPostCoordinationHeader());
        flexTable.getCellFormatter().addStyleName(0, position, style.getRotatedHeader());
    }

    interface PostCoordinationPortletViewImplUiBinder extends UiBinder<HTMLPanel, PostCoordinationPortletViewImpl> {

    }
}

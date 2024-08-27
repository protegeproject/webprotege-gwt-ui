package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.ScaleValueCardView;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class PostCoordinationPortletViewImpl extends Composite implements PostCoordinationPortletView {


    @UiField
    HTMLPanel paneContainer;

    @UiField
    protected FlexTable flexTable;
    @UiField
    public VerticalPanel scaleValueCardList;

    private Map<String, PostCoordinationTableAxisLabel> labels;
    private Map<String, LinearizationDefinition> definitionMap;

    private List<PostCoordinationTableRow> tableRows = new ArrayList<>();
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
    public VerticalPanel getScaleValueCardsView() {
        return scaleValueCardList;
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
            PostCoordinationTableRow tableRow = new PostCoordinationTableRow(definitions.get(i));
            addRowLabel(tableRow.isDerived(), definitions.get(i).getDisplayLabel(), i + 1, 0);
            List<PostCoordinationTableAxisLabel> labelList = new ArrayList<>(this.labels.values());
            for (int j = 0; j < labelList.size(); j++) {

                PostCoordinationTableCell cell = new PostCoordinationTableCell(definitions.get(i), labelList.get(j), tableRow);
                cell.addValueChangeHandler(valueChanged -> {
                    updateTelescopicLinearizations(cell);
                });
                flexTable.setWidget(i + 1, j + 1, cell.asWidget());
                tableRow.addCell(cell);
            }
            addRowLabel(false, definitions.get(i).getDisplayLabel(), i + 1, labelList.size()+1);

            flexTable.getRowFormatter().addStyleName(i+1, style.getCustomRowStyle());
            if( (i + 1) % 2 == 1) {
                flexTable.getRowFormatter().addStyleName(i+1, style.getEvenRowStyle());
            }
            this.tableRows.add(tableRow);
        }

        for(PostCoordinationTableRow tableRow : tableRows) {
            tableRow.bindToParentRow(tableRows);
        }
    }

    private void updateTelescopicLinearizations(PostCoordinationTableCell cell) {
        for(PostCoordinationTableRow tableRow: this.tableRows) {
            tableRow.updateDerivedCell(cell);
        }
    }

    private void addRowLabel(boolean isDerived, String label, int row, int column) {
        String rowLabelString;
        if(isDerived) {
            rowLabelString = SVG + label;
        } else {
            rowLabelString = label;
        }
        Widget rowLabel = new Label();
        rowLabel.getElement().setInnerHTML(rowLabelString);
        rowLabel.addStyleName(style.getRowLabel());
        flexTable.setWidget(row, column, rowLabel);
        //flexTable.getCellFormatter().addStyleName(row, column, style.getRowLabel());
    }

    private void addHeaderCell(String label, int position) {
        Widget headerCell = new Label();
        headerCell.getElement().setInnerHTML(getHeaderLabelPadded(25, label));
        //headerCell.getElement().setInnerHTML(label);
        flexTable.setWidget(0, position, headerCell);
        flexTable.getCellFormatter().addStyleName(0, position, style.getPostCoordinationHeader());
        flexTable.getCellFormatter().addStyleName(0, position, style.getRotatedHeader());
    }

    private String getHeaderLabelPadded(int padding, String label) {

       StringBuilder result = new StringBuilder();
        int lastBreak = 0;

        for (int i = padding; i < label.length(); i += padding) {
            int spaceIndex = label.lastIndexOf(' ', i);

            if (spaceIndex > lastBreak) {
                result.append(label, lastBreak, spaceIndex).append("</br>");
                lastBreak = spaceIndex + 1;
            } else {
                result.append(label, lastBreak, i).append("</br>");
                lastBreak = i;
            }
        }

        result.append(label.substring(lastBreak));

        return result.toString();
    }
    private static String SVG = "<div style='width: 12px; height: 12px; margin-right:2px;' >" +

            "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M3 7V8.2C3 9.88016 3 10.7202 3.32698 11.362C3.6146 11.9265 4.07354 12.3854 4.63803 12.673C5.27976 13 6.11984 13 7.8 13H21M21 13L17 9M21 13L17 17\" stroke=\"#000000\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>" +
            "</div>";

    interface PostCoordinationPortletViewImplUiBinder extends UiBinder<HTMLPanel, PostCoordinationPortletViewImpl> {

    }
}

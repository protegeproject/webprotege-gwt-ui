package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.core.client.GWT;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckboxValue;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.ThreeStateCheckbox;
import edu.stanford.bmir.protege.web.client.library.text.PlaceholderTextBox;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LinearizationPortletViewImpl extends Composite implements LinearizationPortletView {
    Logger logger = java.util.logging.Logger.getLogger("LinearizationPortletViewImpl");

    private WhoficEntityLinearizationSpecification specification;
    @UiField
    HTMLPanel paneContainer;

    @UiField
    protected FlexTable flexTable;

    @UiField Button editValuesButton;
    @UiField Button cancelButton;

    @UiField Button saveValuesButton;

    @UiField(provided = true)
    ThreeStateCheckbox suppressResidual;

    @UiField
    PlaceholderTextBox residualTitle;

    String backupTitle;

    CheckboxValue backupSuppressResidualValue;


    private List<LinearizationTableRow> tableRowList = new ArrayList<>();
    private List<LinearizationTableRow> backupRows = new ArrayList<>();

    private Map<String, LinearizationDefinition> linearizationDefinitonMap = new HashMap<>();
    private Map<String, EntityNode> parentsMap = new HashMap<>();
    private static LinearizationPortletViewImplUiBinder ourUiBinder = GWT.create(LinearizationPortletViewImplUiBinder.class);

    LinearizationTableResourceBundle.LinearizationCss style;

    private LinearizationParentModal linearizationParentModal;
    private DispatchServiceManager dispatch;

    private  LinearizationCommentsModal commentsModal;

    private ProjectId projectId;

    boolean isReadOnly = true;

    @Inject
    public LinearizationPortletViewImpl(DispatchServiceManager dispatch,
                                        LinearizationCommentsModal commentsModal,
                                        LinearizationParentModal parentModal) {
        this.suppressResidual = new ThreeStateCheckbox(new LinearizationCheckboxConfig(), "");
        LinearizationTableResourceBundle.INSTANCE.style().ensureInjected();
        style = LinearizationTableResourceBundle.INSTANCE.style();
        initWidget(ourUiBinder.createAndBindUi(this));
        editValuesButton.addClickHandler(event -> setEditable());
        cancelButton.addClickHandler(event -> setReadOnly());
        this.commentsModal = commentsModal;
        this.linearizationParentModal = parentModal;

        saveValuesButton.addClickHandler(event -> saveValues());
        saveValuesButton.setVisible(false);
        cancelButton.setVisible(true);
        editValuesButton.setVisible(true);
        this.suppressResidual.setReadOnly(true);
        this.suppressResidual.setEnabled(false);
        this.residualTitle.setEnabled(false);

        this.dispatch = dispatch;
    }

    @Override
    public void setWidget(IsWidget w) {
        paneContainer.clear();
        paneContainer.add(w);
    }

    @Override
    public void dispose() {
        flexTable.removeAllRows();
        tableRowList = new ArrayList<>();
        backupRows = new ArrayList<>();
        isReadOnly = true;
        parentsMap = new HashMap<>();
        saveValuesButton.setVisible(false);
        cancelButton.setVisible(true);
        editValuesButton.setVisible(true);
    }

    @Override
    public void setWhoFicEntity(WhoficEntityLinearizationSpecification specification) {
        try {
            this.specification = specification;
            flexTable.setStyleName(style.getLinearizationTable());

            initializeTableHeader();

            if(specification != null){
                initializeTableRows();
                if(specification.getLinearizationResiduals() != null) {
                    this.suppressResidual.setValue(specification.getLinearizationResiduals().getSuppressSpecifiedResidual());
                    this.residualTitle.setValue(specification.getLinearizationResiduals().getUnspecifiedResidualTitle());
                }
                orderAndPopulateViewWithRows();

            }

        }catch (Exception e) {
            logger.log(Level.SEVERE, "Error while initializing the table " + e);
        }

    }

    @Override
    public void setLinearizationDefinitonMap(Map<String, LinearizationDefinition> linearizationDefinitonMap) {
        this.linearizationDefinitonMap = linearizationDefinitonMap;
    }

    @Override
    public void setLinearizationParentsMap(Map<String, EntityNode> linearizationParentsMap) {
        parentsMap = linearizationParentsMap;
    }

    @Override
    public void setProjectId(ProjectId projectId) {
        this.projectId = projectId;
    }

    private void setEditable() {
        if(isReadOnly) {
            this.backupRows = new ArrayList<>();
            for(LinearizationTableRow row : this.tableRowList) {
                this.backupRows.add(row.clone());
                row.setEnabled();
            }

            this.backupTitle = this.residualTitle.getValue();
            this.backupSuppressResidualValue = suppressResidual.getValue();

            this.isReadOnly = false;
            this.residualTitle.setEnabled(true);
            this.suppressResidual.setReadOnly(false);
            this.suppressResidual.setEnabled(true);
            toggleSaveButtons();
        }

    }

    private void setReadOnly() {
        if(!isReadOnly) {
            this.tableRowList = this.backupRows;
            flexTable.removeAllRows();
            for(LinearizationTableRow row : this.tableRowList) {
                row.setReadOnly();
            }
            initializeTableHeader();

            orderAndPopulateViewWithRows();
            this.suppressResidual.setValue(this.backupSuppressResidualValue);
            this.residualTitle.setValue(this.backupTitle);
            isReadOnly = true;
            this.residualTitle.setEnabled(false);
            this.suppressResidual.setReadOnly(true);
            this.suppressResidual.setEnabled(false);

            toggleSaveButtons();
        }

    }

    private void toggleSaveButtons() {
        editValuesButton.setVisible(isReadOnly);
        saveValuesButton.setVisible(!isReadOnly);
    }


    interface LinearizationPortletViewImplUiBinder extends UiBinder<HTMLPanel, LinearizationPortletViewImpl> {

    }


    private void addHeaderCell(String headerText, int column) {
        Widget headerCell = new Label(headerText);
        flexTable.setWidget(0, column, headerCell);
        flexTable.getCellFormatter().addStyleName(0, column, style.getTableText());
    }

    private void addWideHeaderCell(String headerText, int column) {
        addHeaderCell(headerText,column);
        flexTable.getCellFormatter().addStyleName(0, column, style.getWideColumn());
    }

    private void initializeTableHeader() {
        addHeaderCell("Linearization",  0);
        addHeaderCell("Is part of?",  1);
        addHeaderCell("Is grouping?",2);
        addHeaderCell("Aux.ax.child?", 3);
        addWideHeaderCell("Linearization parent", 4);
        addWideHeaderCell("Coding notes", 5);
        flexTable.getRowFormatter().addStyleName(0, style.getLinearizationHeader());
    }

    private void saveValues(){
        if(!isReadOnly) {
            List<LinearizationSpecification> specifications = this.tableRowList.stream()
                    .map(LinearizationTableRow::asLinearizationSpecification)
                    .collect(Collectors.toList());
            LinearizationResiduals residuals = new LinearizationResiduals(this.suppressResidual.getValue().getValue(), this.residualTitle.getValue());
            WhoficEntityLinearizationSpecification linearizationSpecification = new WhoficEntityLinearizationSpecification(specification.getEntityIRI(),
                    residuals,
                    specifications);

            this.isReadOnly = true;
            toggleSaveButtons();
            for(LinearizationTableRow row : this.tableRowList) {
                row.setReadOnly();
            }
            dispatch.execute(SaveEntityLinearizationAction.create(projectId, linearizationSpecification), (result) -> {
                this.backupRows = new ArrayList<>();
                for(LinearizationTableRow row : this.tableRowList) {
                    this.backupRows.add(row.clone());
                }
                this.backupTitle = this.residualTitle.getValue();
                this.backupSuppressResidualValue = suppressResidual.getValue();
            }) ;
        }
    }

    private void orderAndPopulateViewWithRows() {
        List<LinearizationTableRow> orderedRows = tableRowList.stream()
                .sorted((o1, o2) -> o1.getLinearizationDefinition().getSortingCode().compareToIgnoreCase(o2.getLinearizationDefinition().getSortingCode()))
                .collect(Collectors.toList());

        for(int i = 0; i < orderedRows.size(); i ++) {
            flexTable.getRowFormatter().addStyleName(i+1, style.customRowStyle());
            orderedRows.get(i).populateFlexTable(i+1, flexTable);
        }
    }

    private void initializeTableRows() {
        for(LinearizationSpecification linearizationSpecification: this.specification.getLinearizationSpecifications()){
            LinearizationTableRow row = new LinearizationTableRow(linearizationDefinitonMap,
                    linearizationSpecification,
                    parentsMap,
                    linearizationParentModal,
                    commentsModal);
            tableRowList.add(row);
        }
        for(LinearizationTableRow row : tableRowList) {
            row.refreshParents(tableRowList);
        }
    }




}
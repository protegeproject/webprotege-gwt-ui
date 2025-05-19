package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.*;
import edu.stanford.bmir.protege.web.client.library.text.PlaceholderTextBox;
import edu.stanford.bmir.protege.web.client.progress.*;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Collectors;

public class LinearizationPortletViewImpl extends Composite implements LinearizationPortletView, HasBusy {


    Logger logger = java.util.logging.Logger.getLogger("LinearizationPortletViewImpl");

    private WhoficEntityLinearizationSpecification specification;
    @UiField
    BusyView busyView;

    @UiField
    HTMLPanel paneContainer;

    @UiField
    protected FlexTable flexTable;

    @UiField
    Button editValuesButton;
    @UiField
    Button cancelButton;

    @UiField
    Button saveValuesButton;

    @UiField(provided = true)
    ConfigurableCheckbox suppressOthersSpecifiedResidual;

    @UiField(provided = true)
    ConfigurableCheckbox suppressUnspecifiedResidual;

    @UiField
    PlaceholderTextBox unspecifiedResidualTitle;

    @UiField
    PlaceholderTextBox otherSpecifiedResidualTitle;

    String backupUnspecifiedTitle;

    String backupOtherSpecifiedTitle;

    CheckboxValue backupSuppressOtherResidualValue;

    CheckboxValue backupSuppressUnspecifiedResidualValue;

    LinearizationChangeEventHandler linearizationChangeEventHandler = () -> {
    };


    private List<LinearizationTableRow> tableRowList = new ArrayList<>();
    private List<LinearizationTableRow> backupRows = new ArrayList<>();

    private Map<String, LinearizationDefinition> linearizationDefinitonMap = new HashMap<>();
    private Map<String, String> entityParentsMap = new HashMap<>();
    private static LinearizationPortletViewImplUiBinder ourUiBinder = GWT.create(LinearizationPortletViewImplUiBinder.class);

    LinearizationTableResourceBundle.LinearizationCss style;

    private DispatchServiceManager dispatch;

    private LinearizationCommentsModal commentsModal;

    private ProjectId projectId;

    private IRI entityIri;

    boolean isReadOnly = true;

    private final TableRefresh tableRefresh = (linearizationTableRow) -> {
        flexTable.removeAllRows();
        this.tableRowList = this.tableRowList.stream().map(row -> {
            if (row.getLinearizationDefinition().getLinearizationUri().equalsIgnoreCase(linearizationTableRow.getLinearizationDefinition().getLinearizationUri())) {
                return linearizationTableRow;
            }
            return row;
        }).collect(Collectors.toList());

        for (LinearizationTableRow row : tableRowList) {
            row.populateDerivedLinearizationParents(this.tableRowList);
        }
        initializeTableHeader();

        orderAndPopulateViewWithRows();
    };
    private boolean canEditResiduals;


    @Inject
    public LinearizationPortletViewImpl(DispatchServiceManager dispatch,
                                        LinearizationCommentsModal commentsModal) {
        this.suppressOthersSpecifiedResidual = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), "");
        this.suppressUnspecifiedResidual = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), "");
        LinearizationTableResourceBundle.INSTANCE.style().ensureInjected();
        style = LinearizationTableResourceBundle.INSTANCE.style();
        initWidget(ourUiBinder.createAndBindUi(this));
        editValuesButton.addClickHandler(event -> setEditable());
        cancelButton.addClickHandler(event -> setReadOnly());
        this.commentsModal = commentsModal;

        saveValuesButton.addClickHandler(event -> saveValues());
        saveValuesButton.setVisible(false);
        cancelButton.setVisible(false);
        editValuesButton.setVisible(true);

        disableResiduals();
        this.dispatch = dispatch;
    }

    private void disableResiduals() {
        this.suppressUnspecifiedResidual.setEnabled(false);
        this.suppressUnspecifiedResidual.setReadOnly(true);
        this.suppressOthersSpecifiedResidual.setReadOnly(true);
        this.suppressOthersSpecifiedResidual.setEnabled(false);

        this.unspecifiedResidualTitle.setEnabled(false);
        this.otherSpecifiedResidualTitle.setEnabled(false);
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
        entityParentsMap = new HashMap<>();
        saveValuesButton.setVisible(false);
        cancelButton.setVisible(false);
        editValuesButton.setVisible(true);
    }

    @Override
    public void setWhoFicEntity(WhoficEntityLinearizationSpecification specification) {
        try {
            this.specification = specification;
            flexTable.setStyleName(style.getLinearizationTable());
            initializeTableHeader();

            if (specification != null) {
                this.entityIri = specification.getEntityIRI();

                initializeTableRows();
                if (specification.getLinearizationResiduals() != null) {
                    this.suppressOthersSpecifiedResidual.setValue(specification.getLinearizationResiduals().getSuppressedOtherSpecifiedResiduals());
                    this.suppressUnspecifiedResidual.setValue(specification.getLinearizationResiduals().getSuppressUnspecifiedResiduals());
                    this.unspecifiedResidualTitle.setValue(specification.getLinearizationResiduals().getUnspecifiedResidualTitle());
                    this.otherSpecifiedResidualTitle.setValue(specification.getLinearizationResiduals().getOtherSpecifiedResidualTitle());
                }
                orderAndPopulateViewWithRows();
                disableResiduals();
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while initializing the table " + e);
        }

    }

    @Override
    public void setLinearizationDefinitonMap(Map<String, LinearizationDefinition> linearizationDefinitonMap) {
        this.linearizationDefinitonMap = linearizationDefinitonMap;
    }

    @Override
    public void setEntityParentsMap(Map<String, String> entityParentsMap) {
        this.entityParentsMap = entityParentsMap;
    }

    @Override
    public void setProjectId(ProjectId projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean isReadOnly() {
        return isReadOnly;
    }

    private void setEditable() {
        if (isReadOnly) {
            this.backupRows.clear();
            tableRowList.forEach(tableRow -> {
                this.backupRows.add(tableRow.clone());
                tableRow.setEnabled();
            });

            this.backupUnspecifiedTitle = this.unspecifiedResidualTitle.getValue();
            this.backupOtherSpecifiedTitle = this.otherSpecifiedResidualTitle.getValue();

            this.backupSuppressOtherResidualValue = suppressOthersSpecifiedResidual.getValue();
            this.backupSuppressUnspecifiedResidualValue = suppressUnspecifiedResidual.getValue();

            this.isReadOnly = false;

            if(canEditResiduals){

                this.unspecifiedResidualTitle.setEnabled(true);
                this.otherSpecifiedResidualTitle.setEnabled(true);

                this.suppressOthersSpecifiedResidual.setReadOnly(false);
                this.suppressOthersSpecifiedResidual.setEnabled(true);

                this.suppressUnspecifiedResidual.setReadOnly(false);
                this.suppressUnspecifiedResidual.setEnabled(true);
            }


            toggleSaveButtons();
        }

    }

    private void setReadOnly() {
        if (!isReadOnly) {
            this.tableRowList.clear();
            flexTable.removeAllRows();
            this.backupRows.forEach(backupRow -> this.tableRowList.add(backupRow.clone()));
            this.tableRowList.forEach(LinearizationTableRow::setReadOnly);
            initializeTableHeader();

            orderAndPopulateViewWithRows();
            this.suppressOthersSpecifiedResidual.setValue(this.backupSuppressOtherResidualValue);
            this.suppressUnspecifiedResidual.setValue(this.backupSuppressUnspecifiedResidualValue);

            this.unspecifiedResidualTitle.setValue(this.backupUnspecifiedTitle);
            this.otherSpecifiedResidualTitle.setValue(this.backupOtherSpecifiedTitle);

            disableResiduals();
            isReadOnly = true;
            this.backupRows.clear();
            toggleSaveButtons();
        }

    }

    private void toggleSaveButtons() {
        editValuesButton.setVisible(isReadOnly);
        cancelButton.setVisible(!isReadOnly);
        saveValuesButton.setVisible(!isReadOnly);
    }

    @Override
    public void setBusy(boolean busy) {
        this.busyView.setVisible(busy);
    }

    interface LinearizationPortletViewImplUiBinder extends UiBinder<HTMLPanel, LinearizationPortletViewImpl> {

    }

    private void addHeaderCell(String headerText, int column) {
        Widget headerCell = new Label(headerText);
        flexTable.setWidget(0, column, headerCell);
        flexTable.getCellFormatter().addStyleName(0, column, style.getTableText());
    }

    private void addCheckboxHeaderCell(String headerText, int column) {
        addHeaderCell(headerText, column);
        flexTable.getCellFormatter().addStyleName(0, column, style.getTableCheckboxHeader());
    }

    private void addWideHeaderCell(String headerText, int column) {
        addHeaderCell(headerText, column);
        flexTable.getCellFormatter().addStyleName(0, column, style.getWideColumn());
    }

    private void addCommentsHeader(String headerText, int column) {
        addHeaderCell(headerText, column);
        flexTable.getCellFormatter().addStyleName(0, column, style.getNotesColumn());
    }

    private void initializeTableHeader() {
        addHeaderCell("Linearization", 0);
        addCheckboxHeaderCell("Is Part Of?", 1);
        addCheckboxHeaderCell("Is Grouping?", 2);
        addCheckboxHeaderCell("Aux.Ax.Child?", 3);
        addWideHeaderCell("Linearization Path Parent", 4);
        addCommentsHeader("Coding Notes", 5);
        flexTable.getRowFormatter().addStyleName(0, style.getLinearizationHeader());
    }

    @Override
    public void saveValues() {
        if (!isReadOnly) {
            setBusy(true);
            List<LinearizationSpecification> specifications = this.tableRowList.stream()
                    .map(LinearizationTableRow::asLinearizationSpecification)
                    .collect(Collectors.toList());

            LinearizationResiduals residuals = new LinearizationResiduals(this.suppressOthersSpecifiedResidual.getValue().getValue(),
                    this.suppressUnspecifiedResidual.getValue().getValue(),
                    this.otherSpecifiedResidualTitle.getValue(),
                    this.unspecifiedResidualTitle.getValue());

            WhoficEntityLinearizationSpecification linearizationSpecification = new WhoficEntityLinearizationSpecification(specification.getEntityIRI(),
                    residuals,
                    specifications);

            this.isReadOnly = true;
            toggleSaveButtons();
            for (LinearizationTableRow row : this.tableRowList) {
                row.setReadOnly();
            }
            dispatch.execute(
                    SaveEntityLinearizationAction.create(projectId, linearizationSpecification),
                    this,
                    (result) -> {
                        this.backupRows.clear();
                        for (LinearizationTableRow row : this.tableRowList) {
                            this.backupRows.add(row.clone());
                        }
                        this.backupUnspecifiedTitle = this.unspecifiedResidualTitle.getValue();
                        this.backupOtherSpecifiedTitle = this.otherSpecifiedResidualTitle.getValue();

                        this.backupSuppressOtherResidualValue = suppressOthersSpecifiedResidual.getValue();
                        this.backupSuppressUnspecifiedResidualValue = suppressUnspecifiedResidual.getValue();
                        this.setBusy(false);
                    }
            );
        }
    }


    private void orderAndPopulateViewWithRows() {
        List<LinearizationTableRow> orderedRows = tableRowList.stream()
                .sorted((o1, o2) -> o1.getLinearizationDefinition().getSortingCode().compareToIgnoreCase(o2.getLinearizationDefinition().getSortingCode()))
                .collect(Collectors.toList());

        for (int i = 0; i < orderedRows.size(); i++) {
            flexTable.getRowFormatter().addStyleName(i + 1, style.customRowStyle());
            orderedRows.get(i).populateFlexTable(i + 1, flexTable);
        }
    }

    private void initializeTableRows() {
        for (LinearizationSpecification linearizationSpecification : this.specification.getLinearizationSpecifications()) {
            LinearizationTableRow row = new LinearizationTableRow(linearizationDefinitonMap,
                    linearizationSpecification,
                    entityParentsMap,
                    commentsModal,
                    tableRefresh,
                    linearizationChangeEventHandler);
            tableRowList.add(row);
        }
        for (LinearizationTableRow row : tableRowList) {
            row.populateDerivedLinearizationParents(tableRowList);
        }


    }

    @Override
    public void setLinearizationChangeEventHandler(LinearizationChangeEventHandler eventHandler) {
        this.linearizationChangeEventHandler = eventHandler;
    }

    @Override
    public void setCanEditResiduals(boolean canEditResiduals) {
        this.canEditResiduals = canEditResiduals;
    }
}

package edu.stanford.bmir.protege.web.client.card.linearization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.card.EditableIcon;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.*;
import edu.stanford.bmir.protege.web.client.library.text.PlaceholderTextBox;
import edu.stanford.bmir.protege.web.client.linearization.*;
import edu.stanford.bmir.protege.web.client.progress.*;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Collectors;

public class LinearizationCardViewImpl extends Composite implements LinearizationCardView, HasBusy {


    Logger logger = Logger.getLogger("LinearizationCardViewImpl");

    private WhoficEntityLinearizationSpecification specification;
    @UiField
    BusyView busyView;

    @UiField
    HTMLPanel paneContainer;

    @UiField
    HTMLPanel residualsCard;

    @UiField
    protected FlexTable flexTable;

    @UiField(provided = true)
    ConfigurableCheckbox suppressOthersSpecifiedResidual;

    @UiField(provided = true)
    ConfigurableCheckbox suppressUnspecifiedResidual;

    @UiField
    PlaceholderTextBox unspecifiedResidualTitle;

    @UiField
    PlaceholderTextBox otherSpecifiedResidualTitle;

    @UiField
    EditableIcon editableIconSuppOtherSpecRes;

    @UiField
    EditableIcon editableIconSuppUnspecifiedRes;

    @UiField
    EditableIcon editableIconOtherSpecRes;

    @UiField
    EditableIcon editableIconUnspecifiedRes;

    LinearizationChangeEventHandler linearizationChangeEventHandler = () -> {
    };


    private List<LinearizationTableRow> tableRowList = new ArrayList<>();

    private Map<String, LinearizationDefinition> linearizationDefinitonMap = new HashMap<>();
    private Map<String, String> entityParentsMap = new HashMap<>();
    private static LinearizationCardViewImplUiBinder ourUiBinder = GWT.create(LinearizationCardViewImplUiBinder.class);

    LinearizationTableResourceBundle.LinearizationCss style;

    private final DispatchServiceManager dispatch;

    private final LinearizationCommentsModal commentsModal;

    private ProjectId projectId;

    boolean isReadOnly = true;

    boolean canEditResiduals = false;
    boolean canViewResiduals = false;

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


    @Inject
    public LinearizationCardViewImpl(DispatchServiceManager dispatch,
                                     LinearizationCommentsModal commentsModal) {
        this.suppressOthersSpecifiedResidual = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), "");
        this.suppressUnspecifiedResidual = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), "");
        LinearizationTableResourceBundle.INSTANCE.style().ensureInjected();
        style = LinearizationTableResourceBundle.INSTANCE.style();
        initWidget(ourUiBinder.createAndBindUi(this));
        this.commentsModal = commentsModal;

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
        isReadOnly = true;
        entityParentsMap = new HashMap<>();
    }

    @Override
    public void setWhoFicEntity(WhoficEntityLinearizationSpecification specification) {
        try {
            this.specification = specification;
            flexTable.setStyleName(style.getLinearizationTable());
            initializeTableHeader();

            if (specification != null) {

                initializeTableRows();
                if (specification.getLinearizationResiduals() != null) {
                    this.suppressOthersSpecifiedResidual.setValue(specification.getLinearizationResiduals().getSuppressedOtherSpecifiedResiduals());
                    this.suppressOthersSpecifiedResidual.addValueChangeHandler((e) -> linearizationChangeEventHandler.handleLinearizationChangeEvent());
                    this.suppressUnspecifiedResidual.setValue(specification.getLinearizationResiduals().getSuppressUnspecifiedResiduals());
                    this.suppressUnspecifiedResidual.addValueChangeHandler((e) -> linearizationChangeEventHandler.handleLinearizationChangeEvent());
                    this.unspecifiedResidualTitle.setValue(specification.getLinearizationResiduals().getUnspecifiedResidualTitle());
                    this.unspecifiedResidualTitle.addValueChangeHandler((e) -> linearizationChangeEventHandler.handleLinearizationChangeEvent());
                    this.otherSpecifiedResidualTitle.setValue(specification.getLinearizationResiduals().getOtherSpecifiedResidualTitle());
                    this.otherSpecifiedResidualTitle.addValueChangeHandler(event -> linearizationChangeEventHandler.handleLinearizationChangeEvent());
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

    public void setEditable() {
        if (isReadOnly) {
            this.isReadOnly = false;
            tableRowList.forEach(LinearizationTableRow::setEnabled);

            if (canEditResiduals) {

                this.unspecifiedResidualTitle.setEnabled(true);
                this.otherSpecifiedResidualTitle.setEnabled(true);

                this.suppressOthersSpecifiedResidual.setReadOnly(false);
                this.suppressOthersSpecifiedResidual.setEnabled(true);

                this.suppressUnspecifiedResidual.setReadOnly(false);
                this.suppressUnspecifiedResidual.setEnabled(true);

                this.editableIconOtherSpecRes.setVisible(true);
                this.editableIconUnspecifiedRes.setVisible(true);
                this.editableIconSuppUnspecifiedRes.setVisible(true);
                this.editableIconSuppOtherSpecRes.setVisible(true);
            }
        }

    }

    public void setReadOnly() {

        this.tableRowList.forEach(LinearizationTableRow::setReadOnly);
        if (canEditResiduals) {

            this.editableIconOtherSpecRes.setVisible(false);
            this.editableIconUnspecifiedRes.setVisible(false);
            this.editableIconSuppUnspecifiedRes.setVisible(false);
            this.editableIconSuppOtherSpecRes.setVisible(false);
        }

        disableResiduals();
        isReadOnly = true;

    }

    @Override
    public void setBusy(boolean busy) {
        this.busyView.setVisible(busy);
    }

    interface LinearizationCardViewImplUiBinder extends UiBinder<HTMLPanel, LinearizationCardViewImpl> {

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
    public void saveValues(String commitMessage) {
        if (!isReadOnly) {
            setBusy(true);
            WhoficEntityLinearizationSpecification linearizationSpecification = getLinSpec();
            dispatch.execute(
                    SaveEntityLinearizationAction.create(projectId, linearizationSpecification, commitMessage),
                    this,
                    (onSuccess) -> {
                    },
                    this::setReadOnly
            );
        }
    }

    private void refreshChildrenValues() {
        for (LinearizationTableRow row : tableRowList) {
            row.populateDerivedLinearizationParents(this.tableRowList);
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
    public WhoficEntityLinearizationSpecification getLinSpec() {
        if (specification == null) {
            return null;
        }
        List<LinearizationSpecification> specifications = this.tableRowList.stream()
                .map(LinearizationTableRow::asLinearizationSpecification)
                .collect(Collectors.toList());

        LinearizationResiduals residuals = new LinearizationResiduals(this.suppressOthersSpecifiedResidual.getValue().getValue(),
                this.suppressUnspecifiedResidual.getValue().getValue(),
                this.otherSpecifiedResidualTitle.getValue(),
                this.unspecifiedResidualTitle.getValue());

        return new WhoficEntityLinearizationSpecification(specification.getEntityIRI(),
                residuals,
                specifications);
    }

    @Override
    public void setCanEditResiduals(boolean canEditResiduals) {
        this.canEditResiduals = canEditResiduals;
    }

    @Override
    public void setCanViewResiduals(boolean canViewResiduals) {
        this.canViewResiduals = canViewResiduals;
        this.residualsCard.setVisible(canViewResiduals);
    }
}

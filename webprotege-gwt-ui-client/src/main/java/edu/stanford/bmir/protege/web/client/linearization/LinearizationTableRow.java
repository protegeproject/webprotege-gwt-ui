package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.ConfigurableCheckbox;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationSpecification;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LinearizationTableRow {

    Logger logger = java.util.logging.Logger.getLogger("LinearizationTableRow");

    private final static String DEFAULT_COMMENTS_MESSAGE = "";

    private Widget linearizationDefinitionWidget;

    private ConfigurableCheckbox isPartOfCheckbox;

    private ConfigurableCheckbox isGroupingCheckbox;

    private ConfigurableCheckbox isAuxAxChildCheckbox;

    private Widget linearizationParent;

    private Widget codingNotes;

    private LinearizationComments commentsWidget;

    private LinearizationDefinition linearizationDefinition;

    private Map<String, EntityNode> parentsMap;

    private LinearizationSpecification linearizationSpecification;

    private LinearizationParentModal parentModal;

    private LinearizationCommentsModal linearizationCommentsModal;

    private ProjectId projectId;
    private IRI entityIri;

    private String parentIri;
    LinearizationParentLabel.ParentSelectedHandler parentSelectedHandler = this::handleParentSelected;
    LinearizationPortletViewImpl.TableRefresh tableRefresh;
    private LinearizationParentLabel linearizationParentLabel;

    private LinearizationTableRow(){

    }

    public LinearizationTableRow(Map<String, LinearizationDefinition> definitionMap,
                                 LinearizationSpecification linearizationSpecification,
                                 Map<String, EntityNode> parentsMap,
                                 LinearizationParentModal  modal,
                                 LinearizationCommentsModal commentsModal,
                                 IRI entityIri,
                                 ProjectId projectId,
                                 LinearizationPortletViewImpl.TableRefresh tableRefresh) {
        try {
            this.parentsMap = parentsMap;
            this.projectId = projectId;
            this.entityIri = entityIri;
            this.tableRefresh = tableRefresh;
            this.linearizationCommentsModal = commentsModal;
            this.linearizationDefinition = definitionMap.get(linearizationSpecification.getLinearizationView());

            if(linearizationDefinition == null) {
                throw new RuntimeException("ERROR finding definition for " + linearizationSpecification.getLinearizationView());
            }

            this.linearizationDefinitionWidget = new Label(linearizationDefinition.getDisplayLabel());
            this.linearizationSpecification = linearizationSpecification;

            this.isPartOfCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsIncludedInLinearization());
            this.isGroupingCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsGrouping());
            this.isAuxAxChildCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsAuxiliaryAxisChild());

            initCodingNotes(linearizationSpecification.getCodingNote());
            this.codingNotes = this.commentsWidget.asWidget();
            this.parentModal = modal;

            populateEditableLinearizationParent();


        } catch (Exception e) {
            logger.info("Error while initializing table row " + e);
        }
    }


    private void populateEditableLinearizationParent() {
        if(!isDerived()) {
            EntityNode linearizationParentEntityNode = this.parentsMap.get(linearizationSpecification.getLinearizationParent());
            String browserText = linearizationParentEntityNode != null ? linearizationParentEntityNode.getBrowserText() : linearizationSpecification.getLinearizationParent();
            this.linearizationParentLabel = new LinearizationParentLabel(browserText,
                    parentModal,
                    entityIri,
                    projectId,
                    parentSelectedHandler);
            this.linearizationParent = linearizationParentLabel.asWidget();
            this.parentIri = this.linearizationSpecification.getLinearizationParent();
        }
    }

    public void populateDerivedLinearizationParents(List<LinearizationTableRow> rows){
        if(isDerived()) {
            LinearizationTableRow mainRow = rows.stream()
                    .filter(linearizationRow -> linearizationRow.linearizationDefinition.getId().equalsIgnoreCase(this.linearizationDefinition.getCoreLinId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        logger.info("Couldn't find parent with id " + linearizationDefinition.getCoreLinId());
                        return new RuntimeException();
                    });
            Label label = new Label("[" + mainRow.linearizationParent.getElement().getInnerText() + "]");
            label.addStyleName(LinearizationTableResourceBundle.INSTANCE.style().getSecondaryParent());
            this.linearizationParent = label;
        }
    }

    public void setEnabled() {
        this.isPartOfCheckbox.setEnabled(true);
        this.isPartOfCheckbox.setReadOnly(false);


        this.isGroupingCheckbox.setEnabled(true);
        this.isGroupingCheckbox.setReadOnly(false);

        this.isAuxAxChildCheckbox.setEnabled(true);
        this.isAuxAxChildCheckbox.setReadOnly(false);

        this.commentsWidget.enable();
        if(linearizationParentLabel != null) {
            linearizationParentLabel.setReadOnly(false);
        }
    }



    public void setReadOnly() {
        this.isPartOfCheckbox.setEnabled(false);
        this.isPartOfCheckbox.setReadOnly(true);


        this.isGroupingCheckbox.setEnabled(false);
        this.isGroupingCheckbox.setReadOnly(true);

        this.isAuxAxChildCheckbox.setEnabled(false);
        this.isAuxAxChildCheckbox.setReadOnly(true);

        this.commentsWidget.disable();
        if(linearizationParentLabel != null) {
            linearizationParentLabel.setReadOnly(true);
        }
    }


    public LinearizationSpecification asLinearizationSpecification(){
        LinearizationSpecification response = new LinearizationSpecification(this.isAuxAxChildCheckbox.getValue().getValue(),
                this.isGroupingCheckbox.getValue().getValue(),
                this.isPartOfCheckbox.getValue().getValue(),
                this.parentIri,
                this.linearizationDefinition.getSortingCode(),
                this.linearizationDefinition.getWhoficEntityIri(),
                this.commentsWidget.getText()
                );
        logger.info("ALEX specification " + response);
        return response;
    }


    public void populateFlexTable(int index, FlexTable flexTable) {
        LinearizationTableResourceBundle.LinearizationCss style = LinearizationTableResourceBundle.INSTANCE.style();
        flexTable.setWidget(index, 0,  linearizationDefinitionWidget);
        flexTable.getCellFormatter().addStyleName(index, 0, style.getTableText());
        flexTable.getCellFormatter().addStyleName(index, 0, style.getLinearizationDefinition());

        flexTable.setWidget(index, 1, isPartOfCheckbox);
        flexTable.getCellFormatter().addStyleName(index, 1, style.getTableCheckBox());

        flexTable.setWidget(index, 2, isGroupingCheckbox);
        flexTable.getCellFormatter().addStyleName(index, 2, style.getTableCheckBox());

        flexTable.setWidget(index, 3, isAuxAxChildCheckbox);
        flexTable.getCellFormatter().addStyleName(index, 3, style.getTableCheckBox());

        flexTable.setWidget(index, 4, linearizationParent);
        flexTable.getCellFormatter().addStyleName(index, 4, style.getTableText());

        flexTable.setWidget(index, 5, codingNotes);
        flexTable.getCellFormatter().addStyleName(index, 5, style.getTableText());

    }

    public LinearizationDefinition getLinearizationDefinition() {
        return linearizationDefinition;
    }

    public LinearizationTableRow clone(){
        LinearizationTableRow clone = new LinearizationTableRow();
        clone.linearizationSpecification = linearizationSpecification;

        if(!isDerived()) {
            clone.linearizationParentLabel = new LinearizationParentLabel(this.linearizationParent.getElement().getInnerText(),
                    parentModal,
                    entityIri,
                    projectId,
                    parentSelectedHandler);
            clone.linearizationParent = clone.linearizationParentLabel.asWidget();
        } else {
            clone.linearizationParent = new Label(this.linearizationParent.getElement().getInnerText());
            clone.linearizationParent.addStyleName(LinearizationTableResourceBundle.INSTANCE.style().getSecondaryParent());

        }
        clone.parentIri = this.parentIri;
        clone.linearizationDefinition = this.linearizationDefinition;
        clone.linearizationDefinitionWidget = new Label(linearizationDefinition.getDisplayLabel());
        clone.isPartOfCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsIncludedInLinearization());
        clone.isGroupingCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsGrouping());
        clone.isAuxAxChildCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsAuxiliaryAxisChild());

        LinearizationComments commentsClone = new LinearizationComments(this.commentsWidget.getText(), linearizationCommentsModal);

        clone.codingNotes = commentsClone.asWidget();
        clone.commentsWidget = commentsClone;
        clone.parentsMap = this.parentsMap;
        return clone;
    }

    private void initCodingNotes(String value) {
        if(value != null && !value.isEmpty()) {
            this.commentsWidget = new LinearizationComments(value, linearizationCommentsModal);
        } else {
            this.commentsWidget = new LinearizationComments(DEFAULT_COMMENTS_MESSAGE, linearizationCommentsModal);
        }
    }

    private void handleParentSelected(OWLEntityData owlEntityData) {
        this.linearizationParentLabel = new LinearizationParentLabel(owlEntityData.getBrowserText(),
                parentModal,
                entityIri,
                projectId,
                parentSelectedHandler);
        this.linearizationParent = linearizationParentLabel.asWidget();
        this.parentIri = owlEntityData.getEntity().getIRI().toString();
        this.setEnabled();
        tableRefresh.refreshTable(this);
    }

    private boolean isDerived(){
        return linearizationDefinition.getCoreLinId() != null && !linearizationDefinition.getCoreLinId().isEmpty();
    }

    @Override
    public String toString() {
        return "LinearizationTableRow{" +
                "linearizationDefinitionWidget=" + linearizationDefinitionWidget +
                ", linearizationParent=" + linearizationParent.getElement().getInnerText() +
                ", parentIri='" + parentIri + '\'' +
                ", linearizationParentLabel=" + linearizationParentLabel.getText() +
                '}';
    }
}

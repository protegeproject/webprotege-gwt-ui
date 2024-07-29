package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.ThreeStateCheckbox;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationSpecification;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LinearizationTableRow {

    Logger logger = java.util.logging.Logger.getLogger("LinearizationTableRow");

    private final static String DEFAULT_COMMENTS_MESSAGE = "";

    private Widget linearizationDefinitionWidget;

    private ThreeStateCheckbox isPartOfCheckbox;

    private ThreeStateCheckbox isGroupingCheckbox;

    private ThreeStateCheckbox isAuxAxChildCheckbox;

    private Widget linearizationParent;

    private Widget codingNotes;

    private LinearizationComments commentsWidget;

    private LinearizationDefinition linearizationDefinition;

    private Map<String, EntityNode> parentsMap;

    private LinearizationSpecification linearizationSpecification;

    private LinearizationParentModal parentModal;

    private LinearizationCommentsModal linearizationCommentsModal;

    private LinearizationTableRow(){

    }

    public LinearizationTableRow(Map<String, LinearizationDefinition> definitionMap,
                                 LinearizationSpecification linearizationSpecification,
                                 Map<String, EntityNode> parentsMap,
                                 LinearizationParentModal  modal,
                                 LinearizationCommentsModal commentsModal) {
        try {
            this.parentsMap = parentsMap;
            this.linearizationCommentsModal = commentsModal;
            this.linearizationDefinition = definitionMap.get(linearizationSpecification.getLinearizationView());

            if(linearizationDefinition == null) {
                throw new RuntimeException("ERROR finding definition for " + linearizationSpecification.getLinearizationView());
            }

            this.linearizationDefinitionWidget = new Label(linearizationDefinition.getDisplayLabel());
            this.linearizationSpecification = linearizationSpecification;

            this.isPartOfCheckbox = new ThreeStateCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsIncludedInLinearization());
            this.isGroupingCheckbox = new ThreeStateCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsGrouping());
            this.isAuxAxChildCheckbox = new ThreeStateCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsAuxiliaryAxisChild());

            initCodingNotes(linearizationSpecification.getCodingNote());
            logger.info("ALEX din constructor " + this.commentsWidget);
            this.codingNotes = this.commentsWidget.asWidget();
            this.parentModal = modal;

        }catch (Exception e) {
            logger.info("Error while initializing table row " + e);
        }
    }


    public void refreshParents(List<LinearizationTableRow> rows) {

        boolean isDerived = linearizationDefinition.getCoreLinId() != null && !linearizationDefinition.getCoreLinId().isEmpty();
        EntityNode linearizationParentEntityNode;
        if (isDerived) {

            String actualParentId = rows.stream()
                            .filter(linearizationRow -> linearizationRow.linearizationDefinition.getId().equalsIgnoreCase(this.linearizationDefinition.getCoreLinId()))
                    .findFirst()
                    .map(linearizationTableRow -> linearizationTableRow.linearizationSpecification.getLinearizationParent())
                    .orElse("");

            linearizationParentEntityNode = this.parentsMap.get(actualParentId);
        } else {
            linearizationParentEntityNode = this.parentsMap.get(linearizationSpecification.getLinearizationParent());
        }

        if (linearizationParentEntityNode != null && linearizationParentEntityNode.getBrowserText() != null && !linearizationParentEntityNode.getBrowserText().isEmpty()) {
            if(isDerived) {
                Label label = new Label("[" + linearizationParentEntityNode.getBrowserText() + "]");
                label.addStyleName(LinearizationTableResourceBundle.INSTANCE.style().getSecondaryParent());
                this.linearizationParent = label;
            } else {
                this.linearizationParent = new LinearizationParentLabel(linearizationParentEntityNode.getBrowserText(), parentModal).asWidget();
            }
        } else {
            this.linearizationParent = new LinearizationParentLabel(linearizationSpecification.getLinearizationParent(), parentModal).asWidget();
        }

    }

    public void setEnabled() {
        this.isPartOfCheckbox.setEnabled(true);
        this.isPartOfCheckbox.setReadOnly(false);


        this.isGroupingCheckbox.setEnabled(true);
        this.isGroupingCheckbox.setReadOnly(false);

        this.isAuxAxChildCheckbox.setEnabled(true);
        this.isAuxAxChildCheckbox.setReadOnly(false);
        logger.info("ALEX din setEnabled " + this.commentsWidget);

        this.commentsWidget.enable();
    }

    public void setReadOnly() {
        this.isPartOfCheckbox.setEnabled(false);
        this.isPartOfCheckbox.setReadOnly(true);


        this.isGroupingCheckbox.setEnabled(false);
        this.isGroupingCheckbox.setReadOnly(true);

        this.isAuxAxChildCheckbox.setEnabled(false);
        this.isAuxAxChildCheckbox.setReadOnly(true);
        logger.info("ALEX din setReadOnly " + this.commentsWidget);

        this.commentsWidget.disable();
    }


    public LinearizationSpecification asLinearizationSpecification(){
        logger.info("ALEX din asLinearizationSpecification " + this.commentsWidget);

        return new LinearizationSpecification(this.isAuxAxChildCheckbox.getValue().getValue(),
                this.isGroupingCheckbox.getValue().getValue(),
                this.isPartOfCheckbox.getValue().getValue(),
                this.linearizationSpecification.getLinearizationParent(),
                this.linearizationDefinition.getSortingCode(),
                this.linearizationDefinition.getWhoficEntityIri(),
                this.commentsWidget.getText()
                );
    }


    public void populateFlexTable(int index, FlexTable flexTable) {
        LinearizationTableResourceBundle.LinearizationCss style = LinearizationTableResourceBundle.INSTANCE.style();
        flexTable.setWidget(index, 0,  linearizationDefinitionWidget);
        flexTable.getCellFormatter().addStyleName(index, 0, style.getTableText());
        flexTable.getCellFormatter().addStyleName(index, 0, style.getLinearizationDefinition());

        flexTable.setWidget(index, 1, isPartOfCheckbox);
        flexTable.getCellFormatter().addStyleName(index, 1, style.getTableText());

        flexTable.setWidget(index, 2, isGroupingCheckbox);
        flexTable.getCellFormatter().addStyleName(index, 2, style.getTableText());

        flexTable.setWidget(index, 3, isAuxAxChildCheckbox);
        flexTable.getCellFormatter().addStyleName(index, 3, style.getTableText());

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
        clone.linearizationParent = new Label(this.linearizationParent.getElement().getInnerText());
        clone.linearizationDefinition = this.linearizationDefinition;
        clone.linearizationDefinitionWidget = new Label(linearizationDefinition.getDisplayLabel());
        clone.isPartOfCheckbox = new ThreeStateCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsIncludedInLinearization());
        clone.isGroupingCheckbox = new ThreeStateCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsGrouping());
        clone.isAuxAxChildCheckbox = new ThreeStateCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsAuxiliaryAxisChild());
        logger.info("ALEX din clone " + this.commentsWidget);

        LinearizationComments commentsClone = new LinearizationComments(this.commentsWidget.getText(), linearizationCommentsModal);

        clone.codingNotes = commentsClone.asWidget();
        clone.commentsWidget = commentsClone;
        clone.parentsMap = this.parentsMap;
        return clone;
    }

    private void initCodingNotes(String value) {
        logger.info("ALEX din initCodingNotes " + this.commentsWidget);

        if(value != null && !value.isEmpty()) {
            this.commentsWidget = new LinearizationComments(value, linearizationCommentsModal);
        } else {
            this.commentsWidget = new LinearizationComments(DEFAULT_COMMENTS_MESSAGE, linearizationCommentsModal);
        }
    }
}

package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.ThreeStateCheckbox;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationSpecification;

import java.util.logging.Logger;

public class LinearizationTableRow {
    Logger logger = java.util.logging.Logger.getLogger("LinearizationTableRow");

    private Widget linearizationDefinitionWidget;

    private ThreeStateCheckbox isPartOfCheckbox;

    private ThreeStateCheckbox isGroupingCheckbox;

    private ThreeStateCheckbox isAuxAxChildCheckbox;

    private Widget linearizationParent;

    private Widget codingNotes;

    private LinearizationDefinition linearizationDefinition;

    private LinearizationSpecification linearizationSpecification;

    private EntityNode entityNode;

    public LinearizationTableRow(LinearizationDefinition linearizationDefinition, LinearizationSpecification linearizationSpecification, EntityNode entityNode) {
        try {
            this.entityNode = entityNode;
            this.linearizationDefinitionWidget = new Label(linearizationDefinition.getId());
            this.linearizationSpecification = linearizationSpecification;
            this.linearizationDefinition = linearizationDefinition;
            this.isPartOfCheckbox = new ThreeStateCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsIncludedInLinearization());
            this.isGroupingCheckbox = new ThreeStateCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsGrouping());
            this.isAuxAxChildCheckbox = new ThreeStateCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsAuxiliaryAxisChild());
            this.codingNotes = new Label(linearizationSpecification.getCodingNote());

            if (entityNode != null && entityNode.getBrowserText() != null && !entityNode.getBrowserText().isEmpty()) {
                this.linearizationParent = new Label(entityNode.getBrowserText());
            } else {
                this.linearizationParent = new Label(linearizationSpecification.getLinearizationParent());
            }

        }catch (Exception e) {
            logger.info("Error while initializing table row " + e);
        }
    }

    public void setEnabled() {
        this.isPartOfCheckbox.setEnabled(true);
        this.isPartOfCheckbox.setReadOnly(false);


        this.isGroupingCheckbox.setEnabled(true);
        this.isGroupingCheckbox.setReadOnly(false);

        this.isAuxAxChildCheckbox.setEnabled(true);
        this.isAuxAxChildCheckbox.setReadOnly(false);
    }

    public void setReadOnly() {
        this.isPartOfCheckbox.setEnabled(false);
        this.isPartOfCheckbox.setReadOnly(true);


        this.isGroupingCheckbox.setEnabled(false);
        this.isGroupingCheckbox.setReadOnly(true);

        this.isAuxAxChildCheckbox.setEnabled(false);
        this.isAuxAxChildCheckbox.setReadOnly(true);
    }




    public void populateFlexTable(int index, FlexTable flexTable) {
        LinearizationTableResourceBundle.LinearizationCss style = LinearizationTableResourceBundle.INSTANCE.style();
        flexTable.setWidget(index, 0,  linearizationDefinitionWidget);
        flexTable.getCellFormatter().addStyleName(index, 0, style.getTableText());

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
}

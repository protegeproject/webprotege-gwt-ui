package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.ConfigurableCheckbox;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;
import java.util.logging.Logger;

public class LinearizationTableRow {

    Logger logger = java.util.logging.Logger.getLogger("LinearizationTableRow");

    private final static String DEFAULT_COMMENTS_MESSAGE = "";

    private Widget linearizationDefinitionWidget;

    private ConfigurableCheckbox isPartOfCheckbox;

    private ConfigurableCheckbox isGroupingCheckbox;

    private ConfigurableCheckbox isAuxAxChildCheckbox;

    private Widget codingNotes;

    private LinearizationComments commentsWidget;

    private LinearizationDefinition linearizationDefinition;

    private Map<String, EntityNode> parentsMap;

    private LinearizationSpecification linearizationSpecification;

    private LinearizationCommentsModal linearizationCommentsModal;

    private ProjectId projectId;
    private IRI entityIri;

    private String parentIri;
    LinearizationPortletViewImpl.TableRefresh tableRefresh;

    ListBox linearizationParentSelector;
    String linearizationParent;

    private LinearizationTableRow() {

    }

    public LinearizationTableRow(Map<String, LinearizationDefinition> definitionMap,
                                 LinearizationSpecification linearizationSpecification,
                                 Map<String, EntityNode> parentsMap,
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

            if (linearizationDefinition == null) {
                throw new RuntimeException("ERROR finding definition for " + linearizationSpecification.getLinearizationView());
            }

            this.linearizationDefinitionWidget = new Label(linearizationDefinition.getDisplayLabel());
            this.linearizationSpecification = linearizationSpecification;
            this.linearizationParentSelector = new ListBox();
            this.linearizationParentSelector.setMultipleSelect(false);

            this.isPartOfCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsIncludedInLinearization());
            this.isGroupingCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsGrouping());
            this.isAuxAxChildCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsAuxiliaryAxisChild());

            initCodingNotes(linearizationSpecification.getCodingNote());
            this.codingNotes = this.commentsWidget.asWidget();

            populateEditableLinearizationParent();


        } catch (Exception e) {
            logger.info("Error while initializing table row " + e);
        }
    }


    private void populateEditableLinearizationParent() {
        if (!isDerived()) {
            this.parentIri = this.linearizationSpecification.getLinearizationParent();
            this.parentsMap.forEach((iri, entityNode) -> {
                String browserText = entityNode != null ? entityNode.getBrowserText() : parentIri;

                this.linearizationParentSelector.addItem(browserText, iri);
            });
            for (int i = 0; i < this.linearizationParentSelector.getItemCount(); i++) {
                if (this.linearizationParentSelector.getValue(i).equals(this.parentIri)) {
                    this.linearizationParentSelector.setSelectedIndex(i);
                    break;
                }
            }
            this.linearizationParentSelector.addItem("<Linearization parent not set>", "");
            this.linearizationParentSelector.addChangeHandler((event) -> this.handleParentSelected());
            this.linearizationParentSelector.setEnabled(false);
        }
    }

    public void populateDerivedLinearizationParents(List<LinearizationTableRow> rows) {
        if (isDerived()) {
            LinearizationTableRow mainRow = rows.stream()
                    .filter(linearizationRow -> linearizationRow.linearizationDefinition.getId().equalsIgnoreCase(this.linearizationDefinition.getCoreLinId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        logger.info("Couldn't find parent with id " + linearizationDefinition.getCoreLinId());
                        return new RuntimeException();
                    });
            this.linearizationParent = mainRow.linearizationParentSelector.getSelectedValue();
            this.linearizationParentSelector = getCopy(mainRow.linearizationParentSelector);
            this.linearizationParentSelector.setEnabled(false);
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
        if (linearizationParent == null || linearizationParent.equals("")) {
            linearizationParentSelector.setEnabled(true);
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
        linearizationParentSelector.setEnabled(false);
    }


    public LinearizationSpecification asLinearizationSpecification() {
        LinearizationSpecification response = new LinearizationSpecification(this.isAuxAxChildCheckbox.getValue().getValue(),
                this.isGroupingCheckbox.getValue().getValue(),
                this.isPartOfCheckbox.getValue().getValue(),
                this.parentIri,
                this.linearizationDefinition.getSortingCode(),
                this.linearizationDefinition.getWhoficEntityIri(),
                this.commentsWidget.getText()
        );
        return response;
    }


    public void populateFlexTable(int index, FlexTable flexTable) {
        LinearizationTableResourceBundle.LinearizationCss style = LinearizationTableResourceBundle.INSTANCE.style();
        flexTable.setWidget(index, 0, linearizationDefinitionWidget);
        flexTable.getCellFormatter().addStyleName(index, 0, style.getTableText());
        flexTable.getCellFormatter().addStyleName(index, 0, style.getLinearizationDefinition());

        flexTable.setWidget(index, 1, isPartOfCheckbox);
        flexTable.getCellFormatter().addStyleName(index, 1, style.getTableCheckBox());

        flexTable.setWidget(index, 2, isGroupingCheckbox);
        flexTable.getCellFormatter().addStyleName(index, 2, style.getTableCheckBox());

        flexTable.setWidget(index, 3, isAuxAxChildCheckbox);
        flexTable.getCellFormatter().addStyleName(index, 3, style.getTableCheckBox());

        flexTable.setWidget(index, 4, linearizationParentSelector);
        flexTable.getCellFormatter().addStyleName(index, 4, style.getTableText());

        flexTable.setWidget(index, 5, codingNotes);
        flexTable.getCellFormatter().addStyleName(index, 5, style.getTableText());

    }

    public LinearizationDefinition getLinearizationDefinition() {
        return linearizationDefinition;
    }

    public LinearizationTableRow clone() {
        LinearizationTableRow clone = new LinearizationTableRow();
        clone.linearizationSpecification = linearizationSpecification;

        if (!isDerived()) {
            clone.linearizationParentSelector = getCopy(linearizationParentSelector);
        } else {
            clone.linearizationParentSelector = getCopy(linearizationParentSelector);
            clone.linearizationParentSelector.setEnabled(false);
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
        if (value != null && !value.isEmpty()) {
            this.commentsWidget = new LinearizationComments(value, linearizationCommentsModal);
        } else {
            this.commentsWidget = new LinearizationComments(DEFAULT_COMMENTS_MESSAGE, linearizationCommentsModal);
        }
    }

    private void handleParentSelected() {
        int selectedIndex = linearizationParentSelector.getSelectedIndex();
        String selectedText = linearizationParentSelector.getItemText(selectedIndex);

        if ("<Linearization parent not set>".equals(selectedText) || "".equals(selectedText)) {
            linearizationParentSelector.setItemText(selectedIndex, "Select a parent");
        }

        this.parentIri = this.linearizationParentSelector.getValue(selectedIndex);
        this.linearizationParent = parentIri;
        this.setEnabled();
        tableRefresh.refreshTable(this);
    }

    private boolean isDerived() {
        return linearizationDefinition.getCoreLinId() != null && !linearizationDefinition.getCoreLinId().isEmpty();
    }

    @Override
    public String toString() {
        return "LinearizationTableRow{" +
                "linearizationDefinitionWidget=" + linearizationDefinitionWidget +
                ", linearizationParent=" + this.linearizationParentSelector.getSelectedValue() +
                ", parentIri='" + parentIri + '\'' +
                ", linearizationParentLabel=" + this.linearizationParentSelector.getSelectedItemText() +
                '}';
    }

    private ListBox getCopy(ListBox original) {
        ListBox copy = new ListBox();

        copy.setMultipleSelect(original.isMultipleSelect());

        for (int i = 0; i < original.getItemCount(); i++) {
            String itemText = original.getItemText(i);
            String itemValue = original.getValue(i);

            copy.addItem(itemText, itemValue);

            if (original.isItemSelected(i)) {
                copy.setItemSelected(i, true);
            }
        }

        return copy;
    }
}

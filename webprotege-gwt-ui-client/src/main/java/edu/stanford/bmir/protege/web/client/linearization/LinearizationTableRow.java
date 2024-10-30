package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.ConfigurableCheckbox;
import edu.stanford.bmir.protege.web.shared.linearization.*;

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

    private Map<String, String> baseEntityParentsMap;

    private LinearizationSpecification linearizationSpecification;

    private LinearizationCommentsModal linearizationCommentsModal;


    private String parentIri;
    LinearizationPortletViewImpl.TableRefresh tableRefresh;

    ListBox linearizationParentSelector;
    Label linearizationParentLabel;

    HorizontalPanel parentSelectionPanel;

    private LinearizationTableRow() {

    }

    public LinearizationTableRow(Map<String, LinearizationDefinition> definitionMap,
                                 LinearizationSpecification linearizationSpecification,
                                 Map<String, String> baseEntityParentsMap,
                                 LinearizationCommentsModal commentsModal,
                                 LinearizationPortletViewImpl.TableRefresh tableRefresh) {
        try {
            this.baseEntityParentsMap = baseEntityParentsMap;
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
            this.linearizationParentSelector.setVisible(false);
            this.linearizationParentSelector.setEnabled(false);
            this.linearizationParentLabel = new Label();
            this.linearizationParentLabel.setVisible(true);
            this.parentSelectionPanel = new HorizontalPanel();
            this.parentSelectionPanel.add(linearizationParentSelector);
            this.parentSelectionPanel.add(linearizationParentLabel);
            this.parentSelectionPanel.setVisible(true);

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
            this.parentIri = this.linearizationSpecification.getLinearizationParent() != null ? this.linearizationSpecification.getLinearizationParent() : "";
            this.baseEntityParentsMap.forEach(
                    (iri, parentsText) -> {
                        String browserText = parentsText != null && !parentsText.equals("") ? parentsText : iri;

                        this.linearizationParentSelector.addItem(browserText, iri);
                    }
            );
            this.linearizationParentSelector.addItem("<Linearization parent not set>", "");
            for (int i = 0; i < this.linearizationParentSelector.getItemCount(); i++) {
                if (this.linearizationParentSelector.getValue(i).equals(this.parentIri)) {
                    this.linearizationParentSelector.setSelectedIndex(i);
                    break;
                }
            }

            String selectedItemText = linearizationParentSelector.getSelectedValue().equals("") ? "Select a parent" : linearizationParentSelector.getSelectedItemText();
            this.linearizationParentLabel.setText(selectedItemText);

            this.linearizationParentSelector.addChangeHandler((event) -> this.handleParentSelected());
            this.linearizationParentSelector.setVisible(false);
            this.linearizationParentSelector.setEnabled(false);
            this.linearizationParentLabel.setVisible(true);

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
            this.parentIri = mainRow.linearizationParentSelector.getSelectedValue();
            this.linearizationParentSelector.setEnabled(false);
            this.linearizationParentSelector.setVisible(false);
            this.linearizationParentLabel.setText(mainRow.linearizationParentLabel.getText());
            this.linearizationParentLabel.addStyleName(LinearizationTableResourceBundle.INSTANCE.style().getSecondaryParent());
            this.linearizationParentLabel.setVisible(true);
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
        if (!isDerived()) {
            linearizationParentSelector.setEnabled(true);
            linearizationParentSelector.setVisible(true);
            linearizationParentLabel.setVisible(false);
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
        linearizationParentSelector.setVisible(false);
        linearizationParentLabel.setVisible(true);
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

        flexTable.setWidget(index, 4, parentSelectionPanel);
        flexTable.getCellFormatter().addStyleName(index, 4, style.getTableText());

        flexTable.setWidget(index, 5, codingNotes);
        flexTable.getCellFormatter().addStyleName(index, 5, style.getTableText());

    }

    public LinearizationDefinition getLinearizationDefinition() {
        return linearizationDefinition;
    }

    public LinearizationTableRow clone() {
        LinearizationTableRow clone = new LinearizationTableRow();
        clone.linearizationSpecification = this.asLinearizationSpecification();

        clone.linearizationParentSelector = getCopy(linearizationParentSelector);
        clone.linearizationParentLabel = getCopy(linearizationParentLabel);
        clone.parentSelectionPanel = new HorizontalPanel();
        clone.parentSelectionPanel.add(clone.linearizationParentSelector);
        clone.parentSelectionPanel.add(clone.linearizationParentLabel);

        clone.parentIri = this.parentIri;
        clone.linearizationDefinition = this.linearizationDefinition;
        clone.linearizationDefinitionWidget = new Label(linearizationDefinition.getDisplayLabel());
        clone.isPartOfCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), clone.linearizationSpecification.getIsIncludedInLinearization());
        clone.isGroupingCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), clone.linearizationSpecification.getIsGrouping());
        clone.isAuxAxChildCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), clone.linearizationSpecification.getIsAuxiliaryAxisChild());

        LinearizationComments commentsClone = new LinearizationComments(this.commentsWidget.getText(), linearizationCommentsModal);

        clone.codingNotes = commentsClone.asWidget();
        clone.commentsWidget = commentsClone;
        clone.baseEntityParentsMap = this.baseEntityParentsMap;
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

        if ("<Linearization parent not set>".equals(selectedText)) {
            this.linearizationParentLabel.setText("Select a parent");
        } else {
            this.linearizationParentLabel.setText(selectedText);
        }

        this.parentIri = this.linearizationParentSelector.getValue(selectedIndex);

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
        copy.setVisible(original.isVisible());
        copy.setEnabled(original.isEnabled());

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

    private Label getCopy(Label original) {
        Label copy = new Label();

        copy.setVisible(original.isVisible());
        copy.addStyleName(original.getStyleName());
        copy.setText(original.getText());

        return copy;
    }
}

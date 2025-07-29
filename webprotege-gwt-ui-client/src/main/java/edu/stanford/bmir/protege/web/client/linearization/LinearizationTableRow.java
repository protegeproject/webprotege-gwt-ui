package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.card.*;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.ConfigurableCheckbox;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
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
    TableRefresh tableRefresh;

    ListBox linearizationParentSelector;
    Label linearizationParentLabel;

    HorizontalPanel parentSelectionPanel;

    LinearizationTableResourceBundle.LinearizationCss linearizationCss = LinearizationTableResourceBundle.INSTANCE.style();

    LinearizationChangeEventHandler handler = () -> {
    };

    private EditableIcon editableIconDefinition;

    private static final WebProtegeClientBundle wpStyle = WebProtegeClientBundle.BUNDLE;

    private static final Messages MESSAGES = GWT.create(Messages.class);

    private LinearizationTableRow() {

    }

    public LinearizationTableRow(Map<String, LinearizationDefinition> definitionMap,
                                 LinearizationSpecification linearizationSpecification,
                                 Map<String, String> baseEntityParentsMap,
                                 LinearizationCommentsModal commentsModal,
                                 TableRefresh tableRefresh,
                                 LinearizationChangeEventHandler handler) {
        try {
            this.handler = handler;
            this.baseEntityParentsMap = baseEntityParentsMap;
            this.tableRefresh = tableRefresh;
            this.linearizationCommentsModal = commentsModal;
            this.linearizationDefinition = definitionMap.get(linearizationSpecification.getLinearizationView());

            if (linearizationDefinition == null) {
                throw new RuntimeException("ERROR finding definition for " + linearizationSpecification.getLinearizationView());
            }

            editableIconDefinition = new EditableIconImpl();
            editableIconDefinition.setVisible(false);
            editableIconDefinition.addStyleName(linearizationCss.size75());
            editableIconDefinition.addStyleName(linearizationCss.marginLeftAuto());

            FlowPanel defPanel = new FlowPanel();
            defPanel.setStyleName(linearizationCss.getLinearizationDefinition());
            if (isDerived()) {
                Image telescopic = new Image(wpStyle.svgTelescopicIcon().getSafeUri());
                telescopic.setPixelSize(16, 16);
                telescopic.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
                telescopic.setTitle(MESSAGES.linearization_telescopic(linearizationDefinition.getCoreLinId()));
                defPanel.add(telescopic);
            }
            InlineLabel defLabel = new InlineLabel(linearizationDefinition.getDisplayLabel());
            defLabel.getElement().getStyle().setProperty("flex", "1");
            defPanel.add(defLabel);
            defPanel.add(editableIconDefinition);

            this.linearizationDefinitionWidget = defPanel;

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
            this.isPartOfCheckbox.addValueChangeHandler((e) -> handler.handleLinearizationChangeEvent());
            this.isGroupingCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsGrouping());
            this.isGroupingCheckbox.addValueChangeHandler((e) -> handler.handleLinearizationChangeEvent());
            this.isAuxAxChildCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), linearizationSpecification.getIsAuxiliaryAxisChild());
            this.isAuxAxChildCheckbox.addValueChangeHandler((e) -> handler.handleLinearizationChangeEvent());

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
                        String browserText = parentsText != null && !parentsText.isEmpty() ? parentsText : iri;

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

            setLinearizationParentLabel();

            this.linearizationParentSelector.addChangeHandler((event) -> this.handleParentSelected());
            this.linearizationParentSelector.setVisible(false);
            this.linearizationParentSelector.setEnabled(false);
            this.linearizationParentLabel.setVisible(true);

        }
    }

    private void setLinearizationParentLabel() {
        String selectedItemText = "Select a parent";
        if (linearizationParentSelector.getSelectedValue().isEmpty() && baseEntityParentsMap.size() == 1) {
            Optional<String> keysOptional = baseEntityParentsMap.keySet().stream().findFirst();
            selectedItemText = "[" + baseEntityParentsMap.get(keysOptional.get()) + "]";
            this.linearizationParentLabel.addStyleName(linearizationCss.italic());
        } else if (!linearizationParentSelector.getSelectedValue().isEmpty()) {
            selectedItemText = linearizationParentSelector.getSelectedItemText();
            this.linearizationParentLabel.removeStyleName(linearizationCss.italic());
        }
        this.linearizationParentLabel.setText(selectedItemText);
    }

    public void populateDerivedLinearizationParents(List<LinearizationTableRow> rows) {
        if (isDerived()) {

            this.linearizationParentSelector.setEnabled(false);
            this.linearizationParentSelector.setVisible(false);
            String existingLinearizationParentLabel = null;
            if(linearizationSpecification.getLinearizationParent() != null && !linearizationSpecification.getLinearizationParent().isEmpty()){
                existingLinearizationParentLabel = this.baseEntityParentsMap.get(linearizationSpecification.getLinearizationParent());
            }
            if(existingLinearizationParentLabel != null) {
                this.linearizationParentLabel.setText(existingLinearizationParentLabel);
                this.linearizationParentLabel.addStyleName(this.linearizationParentLabel.getStyleName());
            } else {
                LinearizationTableRow mainRow = rows.stream()
                        .filter(linearizationRow -> linearizationRow.linearizationDefinition.getLinearizationId().equalsIgnoreCase(this.linearizationDefinition.getCoreLinId()))
                        .findFirst()
                        .orElseThrow(() -> {
                            logger.info("Couldn't find parent with id " + linearizationDefinition.getCoreLinId());
                            return new RuntimeException();
                        });
                this.linearizationParentLabel.setText(mainRow.linearizationParentLabel.getText());
                this.linearizationParentLabel.addStyleName(mainRow.linearizationParentLabel.getStyleName());
            }
            this.linearizationParentLabel.addStyleName(linearizationCss.getSecondaryParent());
            this.linearizationParentLabel.setVisible(true);

        }
    }

    public void setEnabled() {
        if (linearizationDefinition.getDefinitionAccessibility().equals(LinearizationDefinitionAccessibility.EDITABLE)) {
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

            this.editableIconDefinition.setVisible(true);
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

        editableIconDefinition.setVisible(false);
    }


    public LinearizationSpecification asLinearizationSpecification() {
        return new LinearizationSpecification(this.isAuxAxChildCheckbox.getValue().getValue(),
                this.isGroupingCheckbox.getValue().getValue(),
                this.isPartOfCheckbox.getValue().getValue(),
                this.parentIri,
                this.linearizationDefinition.getSortingCode(),
                this.linearizationDefinition.getLinearizationUri(),
                this.commentsWidget.getText()
        );
    }


    public void populateFlexTable(int index, FlexTable flexTable) {
        flexTable.setWidget(index, 0, linearizationDefinitionWidget);
        flexTable.getCellFormatter().addStyleName(index, 0, linearizationCss.getTableText());
        flexTable.getCellFormatter().addStyleName(index, 0, linearizationCss.getLinearizationDefinition());

        flexTable.setWidget(index, 1, isPartOfCheckbox);
        flexTable.getCellFormatter().addStyleName(index, 1, linearizationCss.getTableCheckBox());

        flexTable.setWidget(index, 2, isGroupingCheckbox);
        flexTable.getCellFormatter().addStyleName(index, 2, linearizationCss.getTableCheckBox());

        flexTable.setWidget(index, 3, isAuxAxChildCheckbox);
        flexTable.getCellFormatter().addStyleName(index, 3, linearizationCss.getTableCheckBox());

        flexTable.setWidget(index, 4, parentSelectionPanel);
        flexTable.getCellFormatter().addStyleName(index, 4, linearizationCss.getTableText());
        flexTable.getCellFormatter().addStyleName(index, 4, linearizationCss.getLinearizationDefinition());

        flexTable.setWidget(index, 5, codingNotes);
        flexTable.getCellFormatter().addStyleName(index, 5, linearizationCss.getTableText());

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
        clone.tableRefresh = this.tableRefresh;
        clone.handler = this.handler;
        clone.parentIri = this.parentIri;
        clone.linearizationDefinition = this.linearizationDefinition;
        clone.editableIconDefinition = new EditableIconImpl();
        clone.editableIconDefinition.setVisible(this.editableIconDefinition.isVisible());
        clone.editableIconDefinition.addStyleName(this.editableIconDefinition.asWidget().getStyleName());

        FlowPanel defPanel = new FlowPanel();
        if (isDerived()) {
            Image telescopic = new Image(wpStyle.svgTelescopicIcon().getSafeUri());
            telescopic.setPixelSize(16, 16);
            telescopic.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
            telescopic.setTitle(MESSAGES.linearization_telescopic(linearizationDefinition.getCoreLinId()));
            defPanel.add(telescopic);
        }
        defPanel.setStyleName(linearizationCss.getLinearizationDefinition());
        InlineLabel defLabel = new InlineLabel(linearizationDefinition.getDisplayLabel());
        defPanel.add(defLabel);
        defPanel.add(clone.editableIconDefinition);

        clone.linearizationDefinitionWidget = defPanel;
        clone.isPartOfCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), clone.linearizationSpecification.getIsIncludedInLinearization());
        clone.isPartOfCheckbox.addValueChangeHandler((e) -> handler.handleLinearizationChangeEvent());
        clone.isGroupingCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), clone.linearizationSpecification.getIsGrouping());
        clone.isGroupingCheckbox.addValueChangeHandler((e) -> handler.handleLinearizationChangeEvent());
        clone.isAuxAxChildCheckbox = new ConfigurableCheckbox(new LinearizationCheckboxConfig(), clone.linearizationSpecification.getIsAuxiliaryAxisChild());
        clone.isAuxAxChildCheckbox.addValueChangeHandler((e) -> handler.handleLinearizationChangeEvent());

        LinearizationComments commentsClone = new LinearizationComments(this.commentsWidget.getText(), linearizationCommentsModal, handler);

        clone.codingNotes = commentsClone.asWidget();
        clone.commentsWidget = commentsClone;
        clone.baseEntityParentsMap = this.baseEntityParentsMap;
        return clone;
    }

    private void initCodingNotes(String value) {
        if (value != null && !value.isEmpty()) {
            this.commentsWidget = new LinearizationComments(value, linearizationCommentsModal, handler);
        } else {
            this.commentsWidget = new LinearizationComments(DEFAULT_COMMENTS_MESSAGE, linearizationCommentsModal, handler);
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
        handler.handleLinearizationChangeEvent();
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

        copy.addChangeHandler((event) -> this.handleParentSelected());

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

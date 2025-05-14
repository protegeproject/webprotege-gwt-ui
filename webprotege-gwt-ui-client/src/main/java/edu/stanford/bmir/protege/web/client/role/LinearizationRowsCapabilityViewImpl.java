package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.button.DeleteButton;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link LinearizationRowsCapabilityView} that provides UI for managing
 * linearization row capabilities in WebProtege.
 */
public class LinearizationRowsCapabilityViewImpl extends Composite implements LinearizationRowsCapabilityView {
    
    private static final String VIEW_LINEARIZATION_ROW = "ViewLinearizationRow";
    private static final String EDIT_LINEARIZATION_ROW = "EditLinearizationRow";
    
    interface LinearizationRowsCapabilityViewImplUiBinder extends UiBinder<HTMLPanel, LinearizationRowsCapabilityViewImpl> {
    }

    @UiField
    SimplePanel contextCriteriaContainer;
    
    @UiField
    ListBox capabilityIdField;

    @UiField
    FlowPanel linearizationDefinitionsContainer;

    @UiField
    Button addLinearizationButton;

    private List<LinearizationDefinition> definitionList = Collections.emptyList();
    private List<String> linearizationIds = new ArrayList<>();
    private List<ListBox> linearizationDropdowns = new ArrayList<>();

    private static final LinearizationRowsCapabilityViewImpl.LinearizationRowsCapabilityViewImplUiBinder ourUiBinder = 
        GWT.create(LinearizationRowsCapabilityViewImpl.LinearizationRowsCapabilityViewImplUiBinder.class);

    @Inject
    public LinearizationRowsCapabilityViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        capabilityIdField.addItem(VIEW_LINEARIZATION_ROW);
        capabilityIdField.addItem(EDIT_LINEARIZATION_ROW);
    }

    @UiHandler("addLinearizationButton")
    protected void handleAddLinearization(ClickEvent event) {
        addLinearizationDropdown(null);
    }

    @Override
    public void setCapabilityId(@Nonnull String capabilityId) {
        Objects.requireNonNull(capabilityId, "capabilityId must not be null");
        
        boolean found = false;
        for(int i = 0; i < capabilityIdField.getItemCount(); i++) {
            if(capabilityIdField.getValue(i).equals(capabilityId)) {
                capabilityIdField.setSelectedIndex(i);
                found = true;
                break;
            }
        }
        
        if (!found) {
            capabilityIdField.setSelectedIndex(0);
        }
    }

    @Override
    public String getCapabilityId() {
        return capabilityIdField.getSelectedValue();
    }

    @Override
    public void setLinearizationDefinitions(@Nonnull List<LinearizationDefinition> linearizationDefinitions) {
        this.definitionList = Objects.requireNonNull(linearizationDefinitions, 
            "linearizationDefinitions must not be null");
        updateLinearizationDefinitionFields();
    }

    @Override
    public void setLinearizationIds(@Nonnull List<String> linearizationId) {
        this.linearizationIds = Objects.requireNonNull(linearizationId, 
            "linearizationId must not be null");
        updateLinearizationDefinitionFields();
    }

    @Override
    public List<String> getLinearizationIds() {
        return new ArrayList<>(this.linearizationDropdowns.stream()
                .map(ListBox::getSelectedValue).collect(Collectors.toSet()));
    }

    private void updateLinearizationDefinitionFields() {
        // Clear existing dropdowns
        linearizationDefinitionsContainer.clear();
        linearizationDropdowns.clear();
        
        // Create a dropdown for each linearizationId
        for (String linearizationId : linearizationIds) {
            addLinearizationDropdown(linearizationId);
        }
    }
    
    private void addLinearizationDropdown(String selectedLinearizationUri) {
        HTMLPanel formGroup = new HTMLPanel("");
        formGroup.addStyleName(WebProtegeClientBundle.BUNDLE.style().formTabBar());

        ListBox dropdown = new ListBox();
        populateDropdown(dropdown, selectedLinearizationUri);
        formGroup.add(dropdown);
        
        // Add remove button
        Button removeButton = new DeleteButton();
        removeButton.addClickHandler(event -> {
            linearizationDefinitionsContainer.remove(formGroup);
            linearizationDropdowns.remove(dropdown);
        });
        formGroup.add(removeButton);
        
        linearizationDefinitionsContainer.add(formGroup);
        linearizationDropdowns.add(dropdown);
    }
    
    private void populateDropdown(ListBox dropdown, String selectedLinearizationUri) {
        for (LinearizationDefinition definition : definitionList) {
            dropdown.addItem(definition.getDisplayLabel(), definition.getLinearizationUri());
            if (definition.getLinearizationUri().equals(selectedLinearizationUri)) {
                dropdown.setSelectedIndex(dropdown.getItemCount() - 1);
            }
        }
    }

    @Override
    public AcceptsOneWidget getContextCriteriaContainer() {
        return contextCriteriaContainer;
    }
}

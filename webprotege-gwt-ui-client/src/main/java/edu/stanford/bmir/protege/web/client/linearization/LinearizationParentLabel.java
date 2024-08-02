package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import java.util.logging.Logger;

public class LinearizationParentLabel implements ClickHandler {


    private Label label;

    private LinearizationParentModal linearizationParentModal;

    // Constructor
    public LinearizationParentLabel(String text, LinearizationParentModal linearizationParentModal) {
        label = new Label(text);
        this.linearizationParentModal = linearizationParentModal;
        label.addClickHandler(this); // Register this class as the ClickHandler for the Label
    }

    // Method to set the text of the label
    public void setText(String text) {
        label.setText(text);
    }

    // Method to get the text of the label
    public String getText() {
        return label.getText();
    }

    // Method to set the style of the label
    public void setStyleName(String style) {
        label.setStyleName(style);
    }

    // Method to get the underlying widget
    public Widget asWidget() {
        return label;
    }

    // Implement the onClick method from ClickHandler interface
    @Override
    public void onClick(ClickEvent event) {
        // Handle the click event here
        // For example, you can change the label's text on click
        this.linearizationParentModal.showModal();
    }

    // Method to add external ClickHandlers
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return label.addClickHandler(handler);
    }
}

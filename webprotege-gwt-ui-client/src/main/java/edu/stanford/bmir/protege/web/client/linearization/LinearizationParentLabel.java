package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

import java.util.logging.Logger;

public class LinearizationParentLabel implements ClickHandler {

    private final static java.util.logging.Logger logger = Logger.getLogger("LinearizationParentLabel");
    private Label label;

    private LinearizationParentModal linearizationParentModal;

    private IRI selectedEntityIri;


    private ProjectId projectId;

    private ParentSelectedHandler selectedHandler;

    private boolean readOnly = true;

    private LinearizationParentsResourceBundle.LinearizationParentCss style = LinearizationParentsResourceBundle.INSTANCE.style();

    // Constructor
    public LinearizationParentLabel(String text, LinearizationParentModal linearizationParentModal, IRI entityIri, ProjectId projectId, ParentSelectedHandler parentSelectedHandler) {
        label = new Label(text == null || text.isEmpty() ? "\u00A0" : text);
        label.setStyleName(style.getParentLabel());
        label.addStyleName(LinearizationTableResourceBundle.INSTANCE.style().getLinearizationParentCell());
        this.linearizationParentModal = linearizationParentModal;
        label.addClickHandler(this); // Register this class as the ClickHandler for the Label
        this.selectedEntityIri = entityIri;
        this.projectId = projectId;
        this.selectedHandler = parentSelectedHandler;
    }

    public void setText(String text) {
        label.setText(text);
    }

    public String getText() {
        return label.getText();
    }

    public Widget asWidget() {
        return label;
    }


    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public void onClick(ClickEvent event) {

        if(!readOnly) {
            this.linearizationParentModal.showModal(selectedEntityIri, projectId, selectedHandler);
        }
    }

    // Method to add external ClickHandlers
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return label.addClickHandler(handler);
    }


    public interface ParentSelectedHandler {
        void handleParentSelected(OWLEntityData selectedParent);
    }
}

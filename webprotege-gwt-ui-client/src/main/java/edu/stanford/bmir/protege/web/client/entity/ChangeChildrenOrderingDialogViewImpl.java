package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Composite;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import org.w3c.dom.html.HTMLElement;

import javax.inject.Inject;
import com.google.gwt.user.client.ui.TextArea;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

public class ChangeChildrenOrderingDialogViewImpl extends Composite implements ChangeChildrenOrderingDialogView {
    Logger logger = Logger.getLogger("ChangeChildrenOrderingDialogViewImpl");
    @UiField
    HTMLPanel sortableList;

    private final EntityNodeHtmlRenderer renderer;

    @UiField
    HTMLPanel description;
    @UiField
    TextArea commitMessage;

    @Inject
    public ChangeChildrenOrderingDialogViewImpl(EntityNodeHtmlRenderer renderer) {
        initWidget(ourUiBinder.createAndBindUi(this));
        sortableList.getElement().setTabIndex(0);
        this.renderer = renderer;
        commitMessage.setText("");
        commitMessage.getElement().setAttribute("placeholder", "Optional explanation for why the children were reordered");
    }

    @Override
    public void setChildren(List<EntityNode> children) {
        sortableList.clear();
        sortableList.getElement().setId("sortableList");
        sortableList.addStyleName(BUNDLE.dragAndDrop().draggableList());
        description.addStyleName(BUNDLE.dragAndDrop().title());
        for (EntityNode child : children) {
            StringBuilder sb = new StringBuilder();
            sb.append("<img style='padding-right: 10px; width: 15px; height: 15px;'  src='");
            sb.append(BUNDLE.draggableIcon().getSafeUri().asString());
            sb.append("'/>");
            sb.append(renderer.getHtmlRendering(child));
            HTMLPanel panel = new HTMLPanel(sb.toString());
            panel.addStyleName(BUNDLE.dragAndDrop().draggableItem());
            sortableList.add(panel);
        }

        if (isJQueryLoaded()) {
            makeSortable(sortableList.getElement().getId());
        } else {
            logger.info("Error: jQuery not loaded.");
        }
    }

    @Override
    public List<String> getOrderedChildren() {

        return Arrays.asList(getOrderedIds(sortableList.getElement().getId()).split(","));
    }

    @Override
    public void setEntityName(String browserText) {
        this.description.getElement().setInnerHTML("Use drag-n-drop to reorder the children of <b>" + browserText + "</b>");
    }

    @Override
    public String getCommitMessage() {
        return this.commitMessage.getText();
    }

    public native void makeSortable(String elementId) /*-{
        try {
            var $el = $wnd.$("#" + elementId);

            if ($el.length === 0) {
                console.error("Element not found: #" + elementId);
                return;
            }

            // Now, safely initialize sortable
            $el.sortable();

        } catch (e) {
            console.error("Error in makeSortable:", e.message, e.stack);
        }
    }-*/;

    public native boolean isJQueryLoaded() /*-{
        return (typeof $wnd.$ === "function");
    }-*/;
    public native String getOrderedIds(String panelId) /*-{
    var orderedIds = [];
    $wnd.$("#" + panelId).children().each(function() {
        orderedIds.push(this.id); // Assuming each child has an 'id'
    });
    return orderedIds.join(",");
}-*/;
    interface ChangeChildrenOrderingDialogViewImplUiBinder extends UiBinder<HTMLPanel, ChangeChildrenOrderingDialogViewImpl> {
    }

    private static ChangeChildrenOrderingDialogViewImplUiBinder ourUiBinder = GWT.create(ChangeChildrenOrderingDialogViewImplUiBinder.class);
}

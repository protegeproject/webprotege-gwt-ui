package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

public class ChangeChildrenOrderingDialogViewImpl extends Composite implements ChangeChildrenOrderingDialogView {
    Logger logger = Logger.getLogger("ChangeChildrenOrderingDialogViewImpl");
    @UiField
    HTMLPanel sortableList;

    @Inject
    public ChangeChildrenOrderingDialogViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        sortableList.getElement().setTabIndex(0);
    }

    @Override
    public void setChildren(List<EntityNode> children) {
        sortableList.clear();
        sortableList.getElement().setId("sortableList");
        sortableList.addStyleName(BUNDLE.dragAndDrop().draggableList());

        for (EntityNode child : children) {
            HTMLPanel liElement = new HTMLPanel("li", "");
            liElement.addStyleName("ui-state-default");
            liElement.getElement().setInnerHTML(child.getBrowserText());
            liElement.getElement().setId(child.getEntity().getIRI().toString());
            sortableList.add(liElement);
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

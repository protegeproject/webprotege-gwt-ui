package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;

public class SortableEntityChildren implements IsWidget {

    private final EntityNode entityNode;

    private final HTMLPanel liElement;

    public SortableEntityChildren(EntityNode entityNode) {
        this.entityNode = entityNode;
        liElement = new HTMLPanel("li", "");
        liElement.addStyleName("ui-state-default");
        liElement.getElement().setInnerHTML(entityNode.getBrowserText());
    }


    @Override
    public Widget asWidget() {
        return liElement;
    }
}

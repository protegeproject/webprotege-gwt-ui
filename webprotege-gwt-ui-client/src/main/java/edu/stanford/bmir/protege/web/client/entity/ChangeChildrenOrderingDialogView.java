package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;

import java.util.List;

public interface ChangeChildrenOrderingDialogView extends IsWidget {

    void setChildren(List<EntityNode> children);

    List<String> getOrderedChildren();
}

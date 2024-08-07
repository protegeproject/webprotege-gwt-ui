package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.icd.AncestorClassHierarchy;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class LinearizationParentViewImpl  extends Composite implements LinearizationParentView {

    Logger logger = java.util.logging.Logger.getLogger("LinearizationParentViewImpl");

    @UiField
    Tree tree;

    private OWLEntityData selectedEntity;
    LinearizationParentsResourceBundle.LinearizationParentCss style = LinearizationParentsResourceBundle.INSTANCE.style();

    public LinearizationParentViewImpl(AncestorClassHierarchy ancestorsTree) {
        style.ensureInjected();
        initWidget(ourUiBinder.createAndBindUi(this));

        String browserText = ancestorsTree.getCurrentNode().getBrowserText();
        browserText = browserText != null && !browserText.isEmpty() ? browserText : " ";
        TreeItem root =  new TreeItem(new Label(browserText));
        addTreeItems(root, ancestorsTree.getChildren());
        root.setState(true);
        tree.addItem(root);
        tree.setAnimationEnabled(true);

        tree.addSelectionHandler(event -> {
            Iterator<TreeItem> iter = tree.treeItemIterator();
            while(iter.hasNext()) {
                TreeItem item = iter.next();
                if(event.getSelectedItem().equals(item)) {
                    item.getWidget().addStyleName(style.getParentSelected());
                } else {
                    item.getWidget().removeStyleName(style.getParentSelected());
                }
            }
        });

    }


    @Override
    public void setAncestorsTree(AncestorClassHierarchy ancestorsTree) {


    }

    @Override
    public OWLEntityData getSelectedParent() {
        return selectedEntity;
    }


    private void addTreeItems(TreeItem parentItem, List<AncestorClassHierarchy> nodes) {
        for (AncestorClassHierarchy node : nodes) {
            String browserText = node.getCurrentNode().getBrowserText();
            browserText = browserText != null && !browserText.isEmpty() ? browserText : " ";
            Label label = new Label(browserText);

            TreeItem treeItem = new TreeItem(label);
            treeItem.setStyleName(style.getLinearizationParentLabel());
            label.addClickHandler(event -> {
                selectedEntity = node.getCurrentNode();
                tree.setSelectedItem(treeItem);
            });
            parentItem.addItem(treeItem);

            if (node.getChildren() != null) {
                addTreeItems(treeItem, node.getChildren());
            }
            treeItem.setState(true);

        }
    }
    @Override
    public void dispose() {
        tree.clear();
    }

    interface LinearizationParentViewImpllUiBinder extends UiBinder<HTMLPanel, LinearizationParentViewImpl> {

    }
    private static LinearizationParentViewImpllUiBinder ourUiBinder = GWT.create(LinearizationParentViewImpllUiBinder.class);

}

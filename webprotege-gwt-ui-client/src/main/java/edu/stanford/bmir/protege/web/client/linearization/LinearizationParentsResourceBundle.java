package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface LinearizationParentsResourceBundle extends ClientBundle {

    LinearizationParentsResourceBundle INSTANCE = GWT.create(LinearizationParentsResourceBundle.class);

    @Source("LinearizationParentTree.css")
    LinearizationParentCss style();


    interface LinearizationParentCss extends CssResource {

        @ClassName("linearizationParentLabel")
        String getLinearizationParentLabel();
        @ClassName("parentSelected")
        String getParentSelected();
    }
}

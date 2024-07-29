package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface LinearizationCommentsResourceBundle extends ClientBundle {

    LinearizationCommentsResourceBundle INSTANCE = GWT.create(LinearizationCommentsResourceBundle.class);

    @Source("LinearizationComments.css")
    LinearizationCommentsResourceBundle.LinearizationCommentsCss style();


    interface LinearizationCommentsCss extends CssResource {

        @ClassName("linearizationHelp")
        String getLinearizationHelp();

        @ClassName("linearizationEditor")
        String getLinearizationEditor();

        @ClassName("linearizationColumn")
        String getLinearizationColumn();
    }
}

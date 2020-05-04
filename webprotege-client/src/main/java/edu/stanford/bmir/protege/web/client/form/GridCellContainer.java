package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-03
 */
public interface GridCellContainer extends IsWidget, AcceptsOneWidget, HasVisibility {

    void setWeight(double weight);
}

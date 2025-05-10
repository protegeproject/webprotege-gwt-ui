package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface FormRegionRoleCriteriaView extends IsWidget {

    AcceptsOneWidget getRoleIdContainer();

    AcceptsOneWidget getContextCriteriaContainer();
}

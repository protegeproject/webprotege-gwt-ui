package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface CapabilityContextView extends IsWidget {

    AcceptsOneWidget getCriteriaContainer();

    boolean isForAnyEntity();

    void setForAnyEntity(boolean forAnyEntity);
}

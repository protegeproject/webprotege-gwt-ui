package edu.stanford.bmir.protege.web.client.role;


import com.google.gwt.user.client.ui.*;

public interface ContextAwareCapabilityView extends IsWidget {

    void setCapabilityId(String capabilityId);

    String getCapabilityId();

    AcceptsOneWidget getContextCriteriaContainer();

}

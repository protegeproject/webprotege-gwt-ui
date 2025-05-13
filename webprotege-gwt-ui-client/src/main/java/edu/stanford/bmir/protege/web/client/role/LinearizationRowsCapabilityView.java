package edu.stanford.bmir.protege.web.client.role;


import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;

import java.util.List;

public interface LinearizationRowsCapabilityView extends IsWidget {

    void setCapabilityId(String capabilityId);

    String getCapabilityId();

    void setLinearizationDefinitions(List<LinearizationDefinition> linearizationDefinitions);

    void setLinearizationIds(List<String> linearizationId);

    List<String> getLinearizationIds();

    AcceptsOneWidget getContextCriteriaContainer();

}

package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.linearization.WhoficEntityLinearizationSpecification;

public interface LinearizationPortletView extends AcceptsOneWidget, IsWidget, HasDispose {

    interface LinearizationPaneChangedHandler {
        void handleLinearizationPaneChanged();
    }


    void setWhoFicEntity(WhoficEntityLinearizationSpecification specification);

}

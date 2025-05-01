package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

public interface FormRegionCapabilityView extends IsWidget {

    void setCapabilityId(String capabilityId);

    void setFormRegionId(FormRegionId formRegionId);

    AcceptsOneWidget getContextCriteriaContainer();
}

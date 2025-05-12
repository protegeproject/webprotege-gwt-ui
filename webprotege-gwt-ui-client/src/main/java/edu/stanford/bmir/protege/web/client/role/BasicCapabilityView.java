package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface BasicCapabilityView extends CapabilityView {

    void setId(String id);

    String getId();

    void setAvailableIds(List<String> ids);
}

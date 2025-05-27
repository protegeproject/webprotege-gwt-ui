package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.user.client.ui.*;

public interface EditableIcon extends IsWidget, HasVisibility {

    void setVisible(boolean visible);

    void addStyleName(String styleName);
}

package edu.stanford.bmir.protege.web.client.crud.icatx;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.crud.icatx.IcatxSuffixSettings;

public interface IcatxNameSuffixSettingsView extends IsWidget {
    void clearView();

    void setSettings(IcatxSuffixSettings settings);

    IcatxSuffixSettings getSettings();
}

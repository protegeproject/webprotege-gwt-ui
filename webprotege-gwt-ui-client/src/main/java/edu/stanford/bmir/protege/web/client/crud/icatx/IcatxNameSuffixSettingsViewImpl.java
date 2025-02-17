package edu.stanford.bmir.protege.web.client.crud.icatx;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.shared.crud.icatx.IcatxSuffixSettings;

import javax.inject.Inject;

public class IcatxNameSuffixSettingsViewImpl extends Composite  implements IcatxNameSuffixSettingsView {

    private IcatxSuffixSettings settings = new IcatxSuffixSettings();

    interface IcatxNameSuffixSettingsViewImplUiBinder extends UiBinder<HTMLPanel, IcatxNameSuffixSettingsViewImpl> {

    }
    private static IcatxNameSuffixSettingsViewImpl.IcatxNameSuffixSettingsViewImplUiBinder ourUiBinder = GWT.create(
            IcatxNameSuffixSettingsViewImpl.IcatxNameSuffixSettingsViewImplUiBinder.class);


    @Inject
    public IcatxNameSuffixSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clearView() {

    }

    @Override
    public void setSettings(IcatxSuffixSettings settings) {
        this.settings = settings;
    }

    @Override
    public IcatxSuffixSettings getSettings() {
        return settings;
    }
}

package edu.stanford.bmir.protege.web.client.crud.icatx;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.crud.icatx.IcatxSuffixSettings;


import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class IcatxNameSuffixSettingsPresenter {

    @Nonnull
    private final IcatxNameSuffixSettingsView view;

    @Inject
    public IcatxNameSuffixSettingsPresenter(@Nonnull IcatxNameSuffixSettingsView view) {
        this.view = checkNotNull(view);
    }
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void clear() {
        view.clearView();
    }

    public IcatxSuffixSettings getSettings(){
        return view.getSettings();
    }

    public void setSettings(@Nonnull IcatxSuffixSettings settings) {
        view.setSettings(settings);
    }
}

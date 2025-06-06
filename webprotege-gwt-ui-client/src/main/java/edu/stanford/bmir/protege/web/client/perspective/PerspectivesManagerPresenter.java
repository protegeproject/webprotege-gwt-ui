package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.collect.ImmutableList;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.SimpleEventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.access.BuiltInCapability;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDetails;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
public class PerspectivesManagerPresenter {

    @Nonnull
    private final PerspectivesManagerView perspectivesListView;

    @Nonnull
    private final PerspectivesManagerAdminSettingsView adminSettingsView;

    @Nonnull
    private final PerspectiveDetailsListPresenter detailsListPresenter;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final PerspectivesManagerService perspectivesManagerService;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    private final MessageBox messageBox;

    @Inject
    public PerspectivesManagerPresenter(@Nonnull PerspectivesManagerView perspectivesListView,
                                        @Nonnull PerspectivesManagerAdminSettingsView adminSettingsView,
                                        @Nonnull PerspectiveDetailsListPresenter detailsListPresenter,
                                        @Nonnull SettingsPresenter settingsPresenter,
                                        @Nonnull PerspectivesManagerService perspectivesManagerService,
                                        @Nonnull Messages messages,
                                        @Nonnull LoggedInUserProjectCapabilityChecker capabilityChecker, MessageBox messageBox) {
        this.perspectivesListView = checkNotNull(perspectivesListView);
        this.adminSettingsView = checkNotNull(adminSettingsView);
        this.detailsListPresenter = checkNotNull(detailsListPresenter);
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.perspectivesManagerService = checkNotNull(perspectivesManagerService);
        this.messages = checkNotNull(messages);
        this.capabilityChecker = checkNotNull(capabilityChecker);
        this.messageBox = messageBox;
    }

    public void setNextPlace(@Nonnull Place nextPlace) {
        settingsPresenter.setNextPlace(Optional.of(nextPlace));
    }


    public void start(@Nonnull AcceptsOneWidget container) {
        settingsPresenter.start(container);
        settingsPresenter.setSettingsTitle(messages.perspective_tabs());
        settingsPresenter.setApplyButtonText(messages.dialog_ok());
        settingsPresenter.setApplySettingsHandler(this::handleApplySettings);
        settingsPresenter.setCancelSettingsHandler(this::handleCancelSettings);
        settingsPresenter.setBusy(true);

        AcceptsOneWidget perspectiveDetailsContainer = settingsPresenter.addSection(messages.perspective_tabs());
        perspectiveDetailsContainer.setWidget(perspectivesListView);
        detailsListPresenter.start(perspectivesListView.getPerspectivesListContainer(), new SimpleEventBus());

        perspectivesListView.setResetPerspectivesHandler(this::handleResetPerspectives);

        capabilityChecker.hasCapability(BuiltInCapability.EDIT_PROJECT_SETTINGS,
                                        canEditProjectSettings -> {
                                            if (canEditProjectSettings) {
                                                AcceptsOneWidget adminSettingsContainer = settingsPresenter.addSection("Tabs Admin");
                                                adminSettingsContainer.setWidget(adminSettingsView);
                                                adminSettingsView.setMakeDefaultTabsForProjectHandler(this::handleMakeDefaultTabsForProject);
                                            }
                                        });

        refill();
    }

    private void handleResetPerspectives() {
        perspectivesListView.displayResetPerspectivesConfirmation(() -> {
            settingsPresenter.setBusy(true);
            perspectivesManagerService.resetPerspectives(this::refill);
        });

    }

    private void handleMakeDefaultTabsForProject() {
        settingsPresenter.setBusy(true);
        ImmutableList<PerspectiveDescriptor> descriptors = detailsListPresenter.getValues()
                                                                               .stream()
                                                                               .map(PerspectiveDetails::toPerspectiveDescriptor)
                                                                               .collect(toImmutableList());
        perspectivesManagerService.savePerspectivesAsProjectDefaults(descriptors, () -> {
            settingsPresenter.setBusy(false);
            messageBox.showMessage(messages.perspective_projectDefault_save_message());
        });
    }

    private void handleApplySettings() {
        settingsPresenter.setBusy(true);
        ImmutableList<PerspectiveDescriptor> descriptors = detailsListPresenter.getValues()
                .stream()
                .map(PerspectiveDetails::toPerspectiveDescriptor)
                .collect(toImmutableList());
        perspectivesManagerService.savePerspectives(descriptors, () -> {
            settingsPresenter.setBusy(false);
            settingsPresenter.goToNextPlace();
        });
    }

    private void handleCancelSettings() {
        settingsPresenter.goToNextPlace();
    }

    private void refill() {
        perspectivesManagerService.getPerspectiveDetails(details -> {
            settingsPresenter.setBusy(false);
            detailsListPresenter.setValues(details);
        });
    }

}

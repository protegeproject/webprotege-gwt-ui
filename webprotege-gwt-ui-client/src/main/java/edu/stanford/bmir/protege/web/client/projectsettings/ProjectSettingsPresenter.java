package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.common.collect.ImmutableList;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.app.CapabilityScreener;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitSettingsPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DefaultDictionaryLanguageView;
import edu.stanford.bmir.protege.web.client.lang.DefaultDisplayNameSettingsView;
import edu.stanford.bmir.protege.web.client.renderer.AnnotationPropertyIriRenderer;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsAction;
import edu.stanford.bmir.protege.web.shared.crud.IRIPrefixUpdateStrategy;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.lang.DictionaryLanguageUsage;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.project.GetProjectInfoAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.*;
import edu.stanford.bmir.protege.web.shared.shortform.AnnotationAssertionDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageVisitor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.EDIT_PROJECT_SETTINGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2017
 */
public class ProjectSettingsPresenter {

    private final ProjectId projectId;

    private final CapabilityScreener capabilityScreener;

    private final DispatchServiceManager dispatchServiceManager;

    private final EventBus eventBus;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final GeneralSettingsView generalSettingsView;

    @Nonnull
    private final DefaultDictionaryLanguageView defaultDictionaryLanguageView;

    @Nonnull
    private final DefaultDisplayNameSettingsView defaultDisplayNameSettingsView;

    @Nonnull
    private final EntityCrudKitSettingsPresenter entityCrudKitSettingsPresenter;

    @Nonnull
    private final SlackWebhookSettingsView slackWebhookSettingsView;

    @Nonnull
    private final WebhookSettingsView webhookSettingsView;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final AnnotationPropertyIriRenderer annotationPropertyIriRenderer;

    @Nonnull
    private Optional<ProjectSettings> currentProjectSettings = Optional.empty();

    @Nonnull
    private Optional<ImmutableList<DictionaryLanguageUsage>> currentLanguageUsage = Optional.empty();

    @Nonnull
    private final ProjectSettingsService projectSettingsService;

    @Nonnull
    private final EntityDeprecationSettingsPresenter entityDeprecationSettingsPresenter;

    @Inject
    public ProjectSettingsPresenter(@Nonnull ProjectId projectId,
                                    @Nonnull CapabilityScreener capabilityScreener,
                                    @Nonnull DispatchServiceManager dispatchServiceManager,
                                    @Nonnull EventBus eventBus,
                                    @Nonnull SettingsPresenter settingsPresenter,
                                    @Nonnull ProjectSettingsHeaderSectionPresenter headerSectionPresenter,
                                    @Nonnull GeneralSettingsView generalSettingsView,
                                    @Nonnull DefaultDictionaryLanguageView defaultDictionaryLanguageView,
                                    @Nonnull DefaultDisplayNameSettingsView defaultDisplayNameSettingsView,
                                    @Nonnull EntityCrudKitSettingsPresenter entityCrudKitSettingsPresenter,
                                    @Nonnull SlackWebhookSettingsView slackWebhookSettingsView,
                                    @Nonnull WebhookSettingsView webhookSettingsView,
                                    @Nonnull Messages messages,
                                    @Nonnull AnnotationPropertyIriRenderer annotationPropertyIriRenderer,
                                    @Nonnull ProjectSettingsService projectSettingsService,
                                    @Nonnull EntityDeprecationSettingsPresenter entityDeprecationSettingsPresenter) {
        this.projectId = checkNotNull(projectId);
        this.capabilityScreener = checkNotNull(capabilityScreener);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.eventBus = checkNotNull(eventBus);
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.generalSettingsView = checkNotNull(generalSettingsView);
        this.defaultDictionaryLanguageView = checkNotNull(defaultDictionaryLanguageView);
        this.defaultDisplayNameSettingsView = checkNotNull(defaultDisplayNameSettingsView);
        this.entityCrudKitSettingsPresenter = entityCrudKitSettingsPresenter;
        this.slackWebhookSettingsView = checkNotNull(slackWebhookSettingsView);
        this.webhookSettingsView = checkNotNull(webhookSettingsView);
        this.messages = checkNotNull(messages);
        this.annotationPropertyIriRenderer = checkNotNull(annotationPropertyIriRenderer);
        this.projectSettingsService = checkNotNull(projectSettingsService);
        this.entityDeprecationSettingsPresenter = checkNotNull(entityDeprecationSettingsPresenter);
    }

    public ProjectId getProjectId() {
        return projectId;
    }


    public void start(AcceptsOneWidget container) {
        capabilityScreener.checkCapability(EDIT_PROJECT_SETTINGS.getCapability(),
                                           container,
                                           () -> displaySettings(container));

    }

    public void setNextPlace(@Nonnull Optional<Place> nextPlace) {
        settingsPresenter.setNextPlace(checkNotNull(nextPlace));
    }

    private void displaySettings(AcceptsOneWidget container) {
        settingsPresenter.setSettingsTitle(messages.projectSettings_title());
        settingsPresenter.start(container);
        settingsPresenter.setApplySettingsHandler(this::applySettings);
        settingsPresenter.setCancelSettingsHandler(this::handleCancel);

        settingsPresenter.addSection(messages.projectSettings_mainSettings()).setWidget(generalSettingsView);
        // TODO: Check that the user can do this
        AcceptsOneWidget newEntitySettingsContainer = settingsPresenter.addSection(messages.newEntitySettings());
        entityCrudKitSettingsPresenter.start(newEntitySettingsContainer);

        settingsPresenter.addSection(messages.language_defaultSettings_title()).setWidget(defaultDictionaryLanguageView);
        settingsPresenter.addSection(messages.displayName_settings_project_title()).setWidget(defaultDisplayNameSettingsView);


        AcceptsOneWidget entityDeprecationSettingsContainer = settingsPresenter.addSection(messages.entityDeprecationSettings_title());
        entityDeprecationSettingsPresenter.start(entityDeprecationSettingsContainer);



        settingsPresenter.addSection(messages.projectSettings_slackWebHookUrl()).setWidget(slackWebhookSettingsView);
        settingsPresenter.addSection(messages.projectSettings_payloadUrls()).setWidget(webhookSettingsView);

        defaultDisplayNameSettingsView.setResetLanguagesHandler(this::handleResetDisplayNameLanguages);
        reloadSettings();
    }

    private void handleProjectSettingsImported() {
        reloadSettings();
    }

    private void reloadSettings() {
        settingsPresenter.setBusy(true);
        dispatchServiceManager.execute(GetProjectInfoAction.create(projectId),
                                       result -> {
                                           ProjectSettings projectSettings = result.getProjectSettings();
                                           displayProjectSettings(projectSettings, result.getProjectLanguages());
                                       });
        dispatchServiceManager.execute(GetEntityCrudKitsAction.create(projectId),
                                       result -> {
                                            entityCrudKitSettingsPresenter.setSettings(result.getCurrentSettings());
                                       });
    }

    private void handleResetDisplayNameLanguages() {
        currentLanguageUsage.ifPresent(langUsage -> {
            ImmutableList<DictionaryLanguage> langUsageData = langUsage.stream()
                                                                           .map(DictionaryLanguageUsage::getDictionaryLanguage)
                                                                           .collect(toImmutableList());
            defaultDisplayNameSettingsView.setPrimaryLanguages(langUsageData);
        });
    }

    private void displayProjectSettings(@Nonnull ProjectSettings projectSettings,
                                        @Nonnull ImmutableList<DictionaryLanguageUsage> languages) {
        this.currentProjectSettings = Optional.of(projectSettings);
        this.currentLanguageUsage = Optional.of(languages);
        generalSettingsView.setDisplayName(projectSettings.getProjectDisplayName());
        generalSettingsView.setDescription(projectSettings.getProjectDescription());

        displayDefaultDictionaryLanguage(projectSettings.getDefaultLanguage());
        displayDefaultDisplayNameLanguages(projectSettings.getDefaultDisplayNameSettings(), languages);
        SlackIntegrationSettings slackIntegrationSettings = projectSettings.getSlackIntegrationSettings();
        slackWebhookSettingsView.setWebhookUrl(slackIntegrationSettings.getPayloadUrl());
        WebhookSettings webhookSettings = projectSettings.getWebhookSettings();
        webhookSettingsView.setWebhookUrls(webhookSettings.getWebhookSettings());
        entityDeprecationSettingsPresenter.setValue(projectSettings.getEntityDeprecationSettings());
        settingsPresenter.setBusy(false);
    }

    private void displayDefaultDictionaryLanguage(@Nonnull DictionaryLanguage defaultLanguage) {
        defaultLanguage.accept(new DictionaryLanguageVisitor<Object>() {
            @Override
            public Object getDefault() {
                defaultDictionaryLanguageView.clearAnnotationProperty();
                return null;
            }

            @Override
            public Object visit(@Nonnull AnnotationAssertionDictionaryLanguage language) {
                annotationPropertyIriRenderer.renderAnnotationPropertyIri(language.getAnnotationPropertyIri(),
                                                                          defaultDictionaryLanguageView::setAnnotationProperty);
                return null;
            }
        });
        defaultDictionaryLanguageView.setLanguageTag(defaultLanguage.getLang());
    }

    private void displayDefaultDisplayNameLanguages(@Nonnull DisplayNameSettings displayNameSettings,
                                                    @Nonnull ImmutableList<DictionaryLanguageUsage> languages) {
        ImmutableList<DictionaryLanguage> langList = displayNameSettings.getPrimaryDisplayNameLanguages()
                .stream()
                .collect(toImmutableList());
        if (!langList.isEmpty()) {
            defaultDisplayNameSettingsView.setPrimaryLanguages(langList);
        }
        else {
            ImmutableList<DictionaryLanguage> activeLanguages = languages.stream()
                                                                             .map(DictionaryLanguageUsage::getDictionaryLanguage)
                                                                             .collect(toImmutableList());
            defaultDisplayNameSettingsView.setPrimaryLanguages(activeLanguages);
        }
    }


    private void applySettings() {
        SlackIntegrationSettings slackIntegrationSettings = SlackIntegrationSettings.get(slackWebhookSettingsView.getWebhookrUrl());
        WebhookSettings webhookSettings = WebhookSettings.get(webhookSettingsView.getWebhookUrls());
        ProjectSettings projectSettings = ProjectSettings.get(
                projectId,
                generalSettingsView.getDisplayName(),
                generalSettingsView.getDescription(),
                getDefaultLanguage(),
                getDefaultDisplayNameSettings(),
                slackIntegrationSettings,
                webhookSettings,
                entityDeprecationSettingsPresenter.getValue()
        );
        dispatchServiceManager.execute(SetProjectSettingsAction.create(projectSettings), result -> {
            eventBus.fireEvent(new ProjectSettingsChangedEvent(projectSettings).asGWTEvent());
            settingsPresenter.goToNextPlace();
        });
        EntityCrudKitSettings settings = entityCrudKitSettingsPresenter.getSettings();
            dispatchServiceManager.execute(new SetEntityCrudKitSettingsAction(projectId,
                                                                              settings, settings,
                                                                              IRIPrefixUpdateStrategy.LEAVE_INTACT),
                                           result -> {});


    }

    private DictionaryLanguage getDefaultLanguage() {
        OWLAnnotationPropertyData property = defaultDictionaryLanguageView.getAnnotationProperty()
                                                                          .orElse(DataFactory.getRdfsLabelData());
        String langTag = defaultDictionaryLanguageView.getLanguageTag();
        return AnnotationAssertionDictionaryLanguage.get(property.getEntity().getIRI(), langTag);
    }

    private DisplayNameSettings getDefaultDisplayNameSettings() {
        return DisplayNameSettings.get(
                defaultDisplayNameSettingsView.getPrimaryLanguages(),
                ImmutableList.of()
        );
    }

    private void handleCancel() {

    }
}

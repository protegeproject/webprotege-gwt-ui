package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityTypeIsOneOfCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-20
 */
public class FormEditorPresenter implements Presenter {

    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final FormDescriptorPresenter formDescriptorPresenter;

    @Nonnull
    private final EntityFormSelectorPresenter entityFormSelectorPresenter;

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private final MessageBox messageBox;

    private Optional<Place> nextPlace = Optional.empty();

    @Nonnull
    private FormsMessages formsMessages;

    @Inject
    public FormEditorPresenter(ProjectId projectId,
                               @Nonnull DispatchServiceManager dispatch,
                               @Nonnull SettingsPresenter settingsPresenter,
                               @Nonnull FormDescriptorPresenter formDescriptorPresenter,
                               @Nonnull EntityFormSelectorPresenter entityFormSelectorPresenter,
                               @Nonnull PlaceController placeController,
                               @Nonnull MessageBox messageBox,
                               @Nonnull FormsMessages formsMessages) {
        this.projectId = checkNotNull(projectId);
        this.dispatch = checkNotNull(dispatch);
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.formDescriptorPresenter = checkNotNull(formDescriptorPresenter);
        this.entityFormSelectorPresenter = entityFormSelectorPresenter;
        this.placeController = checkNotNull(placeController);
        this.messageBox = messageBox;
        this.formsMessages = checkNotNull(formsMessages);
    }

    public void setFormId(@Nonnull FormId formId) {
        dispatch.execute(GetEntityFormDescriptorAction.create(projectId, formId),
                settingsPresenter,
                result -> {
                    formDescriptorPresenter.clear();
                    formDescriptorPresenter.setFormId(formId);
                    Optional<FormDescriptor> formDescriptor = result.getFormDescriptor();
                    formDescriptor.ifPresent(this::setDescriptor);
                    entityFormSelectorPresenter.clear();
                    Optional<CompositeRootCriteria> formSelectorCriteria = result.getFormSelectorCriteria();
                    formSelectorCriteria
                            .ifPresent(this::setSelector);
                    entityFormSelectorPresenter.setPurpose(result.getPurpose());
                    dispatch.execute(GetFormRegionAccessRestrictionsAction.get(projectId), result2 -> {
                        formDescriptorPresenter.getSubComponentPresenters()
                                .forEach(p -> p.setFormRegionAccessRestrictions(result2.getAccessRestrictions()));
                    });
                });


    }

    private void setSelector(CompositeRootCriteria selectorCriteria) {
        entityFormSelectorPresenter.setSelectorCriteria(selectorCriteria);
    }

    public void setNextPlace(Optional<Place> nextPlace) {
        this.nextPlace = nextPlace;
    }

    private void setDescriptor(@Nonnull FormDescriptor formDescriptors) {
        formDescriptorPresenter.setFormDescriptor(formDescriptors);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        settingsPresenter.start(container);

        AcceptsOneWidget descriptorViewContainer = settingsPresenter.addSection(formsMessages.forms_Title());
        formDescriptorPresenter.start(descriptorViewContainer, eventBus);

        AcceptsOneWidget selectorContainer = settingsPresenter.addSection(formsMessages.activation());

        entityFormSelectorPresenter.setSelectorCriteria(CompositeRootCriteria.get(
                ImmutableList.of(EntityTypeIsOneOfCriteria.get(ImmutableSet.of(EntityType.CLASS))),
                MultiMatchType.ALL
        ));
        entityFormSelectorPresenter.start(selectorContainer);

        settingsPresenter.setApplySettingsHandler(this::handleApply);
        settingsPresenter.setCancelSettingsHandler(this::handleCancel);
    }

    private void handleApply() {
        LanguageMap languageMap = formDescriptorPresenter.getFormLabel();
        if (languageMap.asMap().isEmpty()) {
            messageBox.showAlert("Please provide a label for this form" );
            return;
        }
        List<FormRegionAccessRestrictions> accessRestrictions = new ArrayList<>();
        formDescriptorPresenter.getRegionDescriptorPresenters()
                .forEach(p -> {
                    List<FormRegionAccessRestrictions> r = p.getFormRegionAccessRestrictions();
                    accessRestrictions.addAll(r);
                });

        dispatch.execute(new SetEntityFormDescriptorAction(projectId,
                        formDescriptorPresenter.getFormDescriptor(),
                        entityFormSelectorPresenter.getPurpose(),
                        entityFormSelectorPresenter.getSelectorCriteria().orElse(null)),
                settingsPresenter,
                setFormsResult -> {
                    dispatch.execute(SetFormRegionAccessRestrictionsAction.get(projectId, accessRestrictions),
                            setAccessRestrictionsResult -> nextPlace.ifPresent(placeController::goTo));
                });

    }

    private void handleCancel() {
        nextPlace.ifPresent(placeController::goTo);
    }
}

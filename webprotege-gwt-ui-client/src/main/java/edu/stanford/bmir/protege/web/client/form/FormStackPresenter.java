package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.form.FormRegionPageChangedEvent.FormRegionPageChangedHandler;
import edu.stanford.bmir.protege.web.client.tab.TabBarPresenter;
import edu.stanford.bmir.protege.web.client.tab.TabContentContainer;
import edu.stanford.bmir.protege.web.client.tab.SelectedTabChangedHandler;
import edu.stanford.bmir.protege.web.client.tab.SelectedTabIdStash;
import edu.stanford.bmir.protege.web.client.ui.DisplayContextManager;
import edu.stanford.bmir.protege.web.client.ui.HasDisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.DisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.FormRegionFilter;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrdering;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-20
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class FormStackPresenter implements HasFormRegionFilterChangedHandler, HasDisplayContextBuilder {

    private final DisplayContextManager displayContextManager = new DisplayContextManager(context -> {});

    @Nonnull
    public ImmutableList<FormId> getSelectedForms() {
        return tabBarPresenter.getSelectedTab()
                                  .map(ImmutableList::of)
                                  .orElse(ImmutableList.of());
    }

    public enum FormUpdate {
        REPLACE,
        PATCH
    }

    @Nonnull
    private final TabBarPresenter<FormId> tabBarPresenter;

    @Nonnull
    private final FormStackView view;

    private final NoFormView noFormView;

    private final Map<FormId, FormPresenter> formPresenters = new LinkedHashMap<>();

    @Nonnull
    private final Provider<FormPresenter> formPresenterProvider;

    private Optional<AcceptsOneWidget> container = Optional.empty();

    @Nonnull
    private FormRegionPageChangedHandler regionPageChangedHandler = formId -> {
    };

    private boolean enabled = true;

    @Nonnull
    private FormRegionOrderingChangedHandler formRegionOrderingChangedHandler = () -> {};

    @Nonnull
    private FormRegionFilterChangedHandler formRegionFilterChangedHandler = event -> {};


    @Inject
    public FormStackPresenter(@Nonnull TabBarPresenter<FormId> tabBarPresenter,
                              @Nonnull FormStackView view,
                              @Nonnull NoFormView noFormView,
                              @Nonnull Provider<FormPresenter> formPresenterProvider) {
        this.tabBarPresenter = checkNotNull(tabBarPresenter);
        this.view = checkNotNull(view);
        this.noFormView = checkNotNull(noFormView);
        this.formPresenterProvider = checkNotNull(formPresenterProvider);
    }

    public Optional<FormId> getSelectedForm() {
        return tabBarPresenter.getSelectedTab();
    }

    public void clearForms() {
        formPresenters.clear();
        tabBarPresenter.clear();
        updateView();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        getFormPresenters().forEach(formPresenter -> formPresenter.setEnabled(enabled));
    }

    public void setSelectedFormChangedHandler(@Nonnull SelectedTabChangedHandler selectedTabChangedHandler) {
        this.tabBarPresenter.setSelectedTabChangedHandler(selectedTabChangedHandler);
    }

    public void setFormRegionPageChangedHandler(@Nonnull FormRegionPageChangedHandler handler) {
        this.regionPageChangedHandler = checkNotNull(handler);
        getFormPresenters().forEach(formPresenter -> formPresenter.setFormRegionPageChangedHandler(handler));
    }

    public void setFormRegionOrderingChangedHandler(@Nonnull FormRegionOrderingChangedHandler handler) {
        this.formRegionOrderingChangedHandler = checkNotNull(handler);
        getFormPresenters().forEach(p -> p.setGridOrderByChangedHandler(handler));
    }

    @Nonnull
    public ImmutableList<FormPageRequest> getPageRequests() {
        return getFormPresenters().stream()
                                  .map(FormPresenter::getPageRequest)
                                  .flatMap(ImmutableList::stream)
                                  .collect(toImmutableList());
    }

    @Nonnull
    public FormDataByFormId getFormData() {
        Map<FormId, FormData> resultMap = new LinkedHashMap<>();
        formPresenters.forEach((formId, formPresenter) -> resultMap.put(formId, formPresenter.getFormData().orElse(null)));
        return new FormDataByFormId(resultMap);
    }

    public void expandAllFields() {
        getFormPresenters().forEach(FormPresenter::expandAll);
    }

    public void collapseAllFields() {
        getFormPresenters().forEach(FormPresenter::collapseAll);
    }

    public void setForms(@Nonnull ImmutableList<FormDataDto> forms,
                         @Nonnull FormUpdate updateType) {
        if (updateType.equals(FormUpdate.PATCH)) {
            updateForms(forms);
        }
        else if (formsAreCurrent(forms)) {
            updateForms(forms);
        }
        else {
            replaceForms(forms);
        }
        updateView();
    }

    private boolean formsAreCurrent(@Nonnull ImmutableList<FormDataDto> forms) {
        List<FormDescriptor> currentFormDescriptors = getFormPresenters().stream()
                                                                         .map(p -> p.getFormData()
                                                                                    .map(FormData::getFormDescriptor))
                                                                         .filter(Optional::isPresent)
                                                                         .map(Optional::get)
                                                                         .collect(Collectors.toList());
        List<FormDescriptor> nextFormDescriptors = forms.stream()
                                                        .map(FormDataDto::getFormDescriptor)
                                                        .map(FormDescriptorDto::toFormDescriptor)
                                                        .collect(Collectors.toList());
        return currentFormDescriptors.equals(nextFormDescriptors);
    }

    private void replaceForms(@Nonnull ImmutableList<FormDataDto> forms) {
        formPresenters.clear();
        tabBarPresenter.clear();
        view.clear();
        forms.forEach(formData -> {
            FormPresenter formPresenter = formPresenterProvider.get();
            FormDescriptorDto formDescriptor = formData.getFormDescriptor();
            TabContentContainer tabContentContainer = view.addContainer(formDescriptor.getLabel());
            formPresenter.start(tabContentContainer);
            formPresenter.setParentDisplayContextBuilder(this);
            formPresenter.setFormRegionPageChangedHandler(regionPageChangedHandler);
            formPresenter.setGridOrderByChangedHandler(formRegionOrderingChangedHandler);
            formPresenter.displayForm(formData);
            formPresenter.setEnabled(enabled);
            formPresenter.setFormRegionFilterChangedHandler(formRegionFilterChangedHandler);
            formPresenters.put(formData.getFormId(), formPresenter);
            tabBarPresenter.addTab(formDescriptor.getFormId(),
                                        formDescriptor.getLabel(),
                    tabContentContainer);
            formPresenter.setParentDisplayContextBuilder(this);
        });
        tabBarPresenter.restoreSelection();
    }

    private void updateForms(@Nonnull ImmutableList<FormDataDto> forms) {
        forms.forEach(form -> {
            FormId formId = form.getFormId();
            FormPresenter formPresenter = formPresenters.get(formId);
            if(formPresenter == null) {
                throw new RuntimeException("Can't find form presenter for " + formId);
            }
            formPresenter.displayForm(form);
        });
    }

    private void updateView() {
        if (formPresenters.isEmpty()) {
            container.ifPresent(c -> c.setWidget(noFormView));
        }
        else {
            container.ifPresent(c -> c.setWidget(view));
        }
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        this.container = Optional.of(container);
        container.setWidget(view);
        tabBarPresenter.start(view.getSelectorContainer());
        updateView();
    }

    public void setSelectedFormIdStash(@Nonnull SelectedTabIdStash<FormId> formIdStash) {
        tabBarPresenter.setSelectedKeyStash(formIdStash);
    }

    @Nonnull
    public ImmutableSet<FormRegionOrdering> getGridControlOrderings() {
        return getFormPresenters().stream()
                                  .flatMap(FormPresenter::getOrderings)
                                  .collect(toImmutableSet());
    }

    public Collection<FormPresenter> getFormPresenters() {
        return formPresenters.values();
    }



    @Nonnull
    public ImmutableSet<FormRegionFilter> getRegionFilters() {
        return getFormPresenters().stream()
                                  .flatMap(formPresenter -> formPresenter.getFilters().stream())
                                  .collect(toImmutableSet());
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler formRegionFilterChangedHandler) {
        this.formRegionFilterChangedHandler = checkNotNull(formRegionFilterChangedHandler);
        getFormPresenters().forEach(formPresenter -> formPresenter.setFormRegionFilterChangedHandler(formRegionFilterChangedHandler));
    }

    @Override
    public void setParentDisplayContextBuilder(HasDisplayContextBuilder parent) {
        displayContextManager.setParentDisplayContextBuilder(parent);
    }

    @Override
    public DisplayContextBuilder fillDisplayContextBuilder() {
        return displayContextManager.fillDisplayContextBuilder();
    }
}

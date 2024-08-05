package edu.stanford.bmir.protege.web.client.tab;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public class TabBarPresenter {

    @Nonnull
    private final List<TabPresenter> itemPresenters = new ArrayList<>();

    @Nonnull
    private final TabBarView view;

    private final TabPresenterFactory tabPresenterFactory;

    private SelectedTabChangedHandler selectedTabChangedHandler = () -> {};

    @Nonnull
    private Optional<SelectedTabIdStash> selectedFormIdStash = Optional.empty();

    @Inject
    public TabBarPresenter(@Nonnull TabBarView view, TabPresenterFactory tabPresenterFactory) {
        this.view = checkNotNull(view);
        this.tabPresenterFactory = checkNotNull(tabPresenterFactory);
    }

    public void clear() {
        itemPresenters.clear();
        view.clear();
        view.setVisible(false);
    }

    public void setSelectedFormChangedHandler(@Nonnull SelectedTabChangedHandler selectedTabChangedHandler) {
        this.selectedTabChangedHandler = checkNotNull(selectedTabChangedHandler);
    }

    public void setSelectedFormIdStash(@Nonnull SelectedTabIdStash selectedTabIdStash) {
        this.selectedFormIdStash = Optional.of(selectedTabIdStash);
        selectedTabIdStash.getSelectedForm().ifPresent(this::restoreFormSelection);
    }

    private void restoreFormSelection(FormId formId) {
        setSelected(formId);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        restoreSelection();
    }

    public void addForm(@Nonnull FormId formId, @Nonnull LanguageMap label, @Nonnull TabContentContainer tabContentContainer) {
        TabPresenter tabPresenter = tabPresenterFactory.create(formId);
        itemPresenters.add(tabPresenter);
        view.addView(tabPresenter.getView());
        tabPresenter.setLabel(label);
        tabPresenter.setFormContainer(tabContentContainer);
        tabPresenter.setClickHandler(event -> selectFormAndStashId(formId));
        view.setVisible(itemPresenters.size() > 1);
    }

    public void selectFormAndStashId(@Nonnull FormId formId) {
        setSelected(formId);
        selectedFormIdStash.ifPresent(stash -> stash.stashSelectedForm(formId));
        selectedTabChangedHandler.handleSelectedFormChanged();
    }

    private void setSelected(FormId formId) {
        for (TabPresenter ip : itemPresenters) {
            boolean selected = ip.getFormId().equals(formId);
            ip.setSelected(selected);
        }
    }

    public void restoreSelection() {
        this.selectedFormIdStash.ifPresent(stash -> {
            Optional<FormId> selectedForm = stash.getSelectedForm();
            selectedForm.ifPresent(this::restoreFormSelection);
            if (!selectedForm.isPresent()) {
                setFirstFormSelected();
            }
        });
        if (!selectedFormIdStash.isPresent()) {
            setFirstFormSelected();
        }
    }

    public void setFirstFormSelected() {
        itemPresenters.stream().findFirst().map(TabPresenter::getFormId).ifPresent(this::setSelected);
    }

    public Optional<FormId> getSelectedForm() {
        return itemPresenters.stream()
                             .filter(TabPresenter::isSelected)
                             .map(TabPresenter::getFormId)
                             .findFirst();
    }
}

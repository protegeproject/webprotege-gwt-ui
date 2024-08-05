package edu.stanford.bmir.protege.web.client.tab;


import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.event.dom.client.ClickHandler;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class TabPresenter {

    @Nonnull
    private final FormId formId;

    @Nonnull
    private final TabView view;

    private boolean selected = false;

    @Nonnull
    private Optional<TabContentContainer> formContainer = Optional.empty();

    @AutoFactory
    @Inject
    public TabPresenter(@Nonnull FormId formId,
                        @Provided @Nonnull TabView view) {
        this.formId = checkNotNull(formId);
        this.view = checkNotNull(view);
    }

    @Nonnull
    public FormId getFormId() {
        return formId;
    }

    public void setFormContainer(@Nonnull TabContentContainer tabContentContainer) {
        this.formContainer = Optional.of(checkNotNull(tabContentContainer));
    }

    public void setLabel(LanguageMap label) {
        view.setLabel(label);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        view.setSelected(selected);
        formContainer.ifPresent(c -> c.setVisible(selected));
    }

    public boolean isSelected() {
        return selected;
    }

    public void setClickHandler(@Nonnull ClickHandler clickHandler) {
        view.setClickHandler(clickHandler);
    }

    @Nonnull
    public TabView getView() {
        return view;
    }
}

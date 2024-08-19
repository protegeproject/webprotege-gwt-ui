package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.tab.TabContentContainer;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-20
 */
public interface FormStackView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getSelectorContainer();

    @Nonnull
    TabContentContainer addContainer(@Nonnull LanguageMap labels);

    void clear();
}

package edu.stanford.bmir.protege.web.client.tab;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public interface TabView extends IsWidget {

    void setClickHandler(@Nonnull ClickHandler clickHandler);

    void setLabel(@Nonnull LanguageMap label);

    void setColor(@Nonnull Color color);

    void setBackgroundColor(@Nonnull Color backgroundColor);

    void setSelected(boolean selected);
}

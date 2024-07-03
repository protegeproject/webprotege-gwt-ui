package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface FormFieldView extends IsWidget, HasRequestFocus {

    void collapse();

    void expand();

    boolean isExpanded();

    interface HeaderClickedHandler {
        void handleHeaderClicked();
    }

    void setHeaderClickedHandler(@Nonnull HeaderClickedHandler headerClickedHandler);

    void setHelpText(@Nonnull String helpText);

    void clearHelpText();

    void addStylePropertyValue(String cssProperty, String cssValue);

    void setId(FormRegionId elementId);

    Optional<FormRegionId> getId();

    void setFormLabel(String formLabel);

    @Nonnull
    AcceptsOneWidget getFormStackContainer();

    void setRequiredValueNotPresentVisible(boolean visible);

    void setRequired(Optionality required);

    Optionality getRequired();

    void setCollapsible(boolean collapsible);
}

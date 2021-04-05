package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public interface SingleChoiceControlDescriptorView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getSourceContainer();

    @Nonnull
    SingleChoiceControlType getWidgetType();

    void setWidgetType(@Nonnull SingleChoiceControlType widgetType);

}

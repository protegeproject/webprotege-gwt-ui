package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-10
 */
public interface MultiChoiceControlDescriptorView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getSourceContainer();
}

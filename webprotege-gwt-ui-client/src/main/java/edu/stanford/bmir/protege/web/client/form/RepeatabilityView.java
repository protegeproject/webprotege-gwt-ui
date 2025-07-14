package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-15
 */
public interface RepeatabilityView extends IsWidget {

    int MAX_PAGE_SIZE = 2000;

    void setRepeatability(@Nonnull Repeatability repeatability);

    @Nonnull
    Repeatability getRepeatability();

    int getPageSize();

    /**
     * Sets the page size.  This must be a positive integer.
     * @param pageSize The page size
     * @throws IllegalArgumentException if page size is less than or equal to zero
     */
    void setPageSize(int pageSize);
}

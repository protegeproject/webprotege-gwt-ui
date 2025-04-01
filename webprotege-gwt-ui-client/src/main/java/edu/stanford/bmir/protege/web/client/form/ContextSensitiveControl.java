package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.DisplayContext;

public interface ContextSensitiveControl {
    /**
     * Updates the control’s dynamic criteria based on the given display context.
     * @param context the current display context
     */
    void updateDynamicCriteria(DisplayContext context);
}
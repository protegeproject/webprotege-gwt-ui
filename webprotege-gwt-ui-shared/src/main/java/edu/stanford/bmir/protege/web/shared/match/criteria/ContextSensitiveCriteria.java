package edu.stanford.bmir.protege.web.shared.match.criteria;

import edu.stanford.bmir.protege.web.shared.DisplayContext;

public interface ContextSensitiveCriteria {
    /**
     * Returns the effective criteria based on the given display context.
     * @param displayContext the current display context
     * @return the criteria to be used for filtering
     */
    EntityMatchCriteria getEffectiveCriteria(DisplayContext displayContext);
}
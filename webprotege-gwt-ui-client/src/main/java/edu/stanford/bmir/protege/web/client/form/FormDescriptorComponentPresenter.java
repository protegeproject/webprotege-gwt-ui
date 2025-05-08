package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.FormRegionAccessRestrictions;

import java.util.Collections;
import java.util.List;

public interface FormDescriptorComponentPresenter {

    /**
     * Returns a list containing this presenter and all descendant component presenters.
     */
    List<FormDescriptorComponentPresenter> getSubComponentPresenters();

    default List<FormRegionAccessRestrictions> getFormRegionAccessRestrictions() {
        return Collections.emptyList();
    };

    default void setFormRegionAccessRestrictions(List<FormRegionAccessRestrictions> formRegionAccessRestrictions) {

    }
}

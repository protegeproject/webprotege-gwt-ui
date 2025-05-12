package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.FormRegionAccessRestriction;

import java.util.Collections;
import java.util.List;

public interface FormDescriptorComponentPresenter {

    void addChildren(FormDescriptorComponentPresenterHierarchyNode thisNode);

    default List<FormRegionAccessRestriction> getFormRegionAccessRestrictions() {
        return Collections.emptyList();
    };

    default void setFormRegionAccessRestrictions(List<FormRegionAccessRestriction> formRegionAccessRestrictions) {

    }
}

package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyFilterType;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class DirectCheckboxViewImpl extends Composite implements DirectCheckboxView {

    private static final ClassSelectorViewImplUiBinder ourUiBinder = GWT.create(ClassSelectorViewImplUiBinder.class);

    @UiField
    CheckBox directCheckBox;

    @Inject
    public DirectCheckboxViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public HierarchyFilterType getHierarchyFilterType() {
        if (directCheckBox.getValue()) {
            return HierarchyFilterType.DIRECT;
        } else {
            return HierarchyFilterType.ALL;
        }
    }

    @Override
    public void setHierarchyFilterType(@Nonnull HierarchyFilterType filterType) {
        directCheckBox.setValue(filterType == HierarchyFilterType.DIRECT);
    }

    interface ClassSelectorViewImplUiBinder extends UiBinder<HTMLPanel, DirectCheckboxViewImpl> {

    }
}
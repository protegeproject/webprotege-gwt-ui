package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class GridControlDescriptorViewImpl extends Composite implements GridControlDescriptorView {

    public static final int MAX_PAGE_SIZE = 2000;

    public static final int DEFAULT_PAGE_SIZE = 20;

    @UiField
    protected AcceptsOneWidget formSubjectFactoryDescriptorContainer;

    interface GridControlDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, GridControlDescriptorViewImpl> {

    }

    private static GridControlDescriptorViewImplUiBinder ourUiBinder = GWT.create(GridControlDescriptorViewImplUiBinder.class);

    @UiField
    SimplePanel viewContainer;

    @UiField
    TextBox maxRowsPerPage;

    @Inject
    public GridControlDescriptorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFormSubjectFactoryDescriptorContainer() {
        return formSubjectFactoryDescriptorContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getViewContainer() {
        return viewContainer;
    }

    @Override
    public int getPageSize() {
        try {
            int pageSize = Integer.parseInt(maxRowsPerPage.getValue());
            if(pageSize <= 0) {
                return DEFAULT_PAGE_SIZE;
            }
            if(pageSize > MAX_PAGE_SIZE) {
                return MAX_PAGE_SIZE;
            }
            return pageSize;
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE_SIZE;
        }
    }

    @Override
    public void setPageSize(int maxRowsPerPage) {
        this.maxRowsPerPage.setValue(Integer.toString(maxRowsPerPage));
    }
}

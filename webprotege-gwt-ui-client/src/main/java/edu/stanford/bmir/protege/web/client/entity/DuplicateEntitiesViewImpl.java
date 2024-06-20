package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.progress.BusyView;

import javax.inject.Inject;

public class DuplicateEntitiesViewImpl extends Composite implements DuplicateEntitiesView {

    @UiField
    BusyView busyView;

    @UiField
    public SimplePanel duplicateResultsContainer;

    private static DuplicateEntitiesViewImpl.DuplicateEntitiesViewImplUiBinder ourUiBinder = GWT.create(DuplicateEntitiesViewImpl.DuplicateEntitiesViewImplUiBinder.class);
    interface DuplicateEntitiesViewImplUiBinder extends UiBinder<HTMLPanel, DuplicateEntitiesViewImpl> {

    }



    @Inject
    public DuplicateEntitiesViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @Override
    public void setBusy(boolean busy) {
        this.busyView.setVisible(busy);
    }


    @Override
    public AcceptsOneWidget getDuplicateResultsContainer(){
        return duplicateResultsContainer;
    }


}

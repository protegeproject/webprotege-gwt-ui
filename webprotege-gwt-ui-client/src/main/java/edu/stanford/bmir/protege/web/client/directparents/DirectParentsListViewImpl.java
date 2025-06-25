package edu.stanford.bmir.protege.web.client.directparents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DirectParentsListViewImpl extends Composite implements DirectParentsListView {

    private final static java.util.logging.Logger logger = Logger.getLogger(DirectParentsListViewImpl.class.getName());

    interface DirectParentsListViewImplUiBinder extends UiBinder<HTMLPanel, DirectParentsListViewImpl> {
    }

    private final static DirectParentsListViewImplUiBinder ourUiBinder = GWT.create(DirectParentsListViewImplUiBinder.class);

    private String mainParentIri;

    private List<DirectParentView> directParentViews = new ArrayList<>();

    @UiField
    FlowPanel directParentsContainer;

    @Inject
    public DirectParentsListViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        logger.info("directParentsContainer = " + directParentsContainer);
    }

    @Override
    public void clearViews() {
        logger.info("clearing direct parents list view");
        directParentsContainer.clear();
    }

    @Override
    public void setDirectParentView(@Nonnull List<DirectParentView> directParentViews) {
        logger.info("called setDirectParentView");
        this.directParentViews = directParentViews;
        directParentViews.forEach(parentView -> {
            try {
                logger.info("Adding widget to UI: " + parentView.asWidget());
                directParentsContainer.add(parentView.asWidget());
                logger.info("After adding, directParentsContainer = " + directParentsContainer);
            } catch (Exception e) {
                logger.severe("Error while adding widget: " + e.getMessage());
            }
        });
        markMainParent();
    }

    @Override
    public void setMainParent(String parentIri) {
        this.mainParentIri = parentIri;
        markMainParent();
    }

    private void markMainParent() {
        if (this.mainParentIri != null && !this.mainParentIri.isEmpty()) {
            for(DirectParentView parentView : this.directParentViews) {
                if(mainParentIri.equals(parentView.getEntityIri())) {
                    parentView.markParentAsMain();
                    return;
                }
            }
        }
    }

}

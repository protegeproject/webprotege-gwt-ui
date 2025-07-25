package edu.stanford.bmir.protege.web.client.directparents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirectParentsListViewImpl extends Composite implements DirectParentsListView {

    private final static java.util.logging.Logger logger = Logger.getLogger(DirectParentsListViewImpl.class.getName());

    interface DirectParentsListViewImplUiBinder extends UiBinder<HTMLPanel, DirectParentsListViewImpl> {
    }

    private final static DirectParentsListViewImplUiBinder ourUiBinder = GWT.create(DirectParentsListViewImplUiBinder.class);

    private String mainParentIri;

    private List<DirectParentView> directParentViews = new ArrayList<>();

    private Set<OWLEntityData> equivalentOnlyParents = new HashSet<>();

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
    public void markParents(@Nonnull List<DirectParentView> directParentViews, Set<OWLEntityData> equivalentOnlyParents) {
        this.directParentViews = directParentViews;
        this.equivalentOnlyParents = equivalentOnlyParents;
        directParentViews.forEach(parentView -> {
            try {
                logger.info("Adding widget to UI: " + parentView.asWidget());
                directParentsContainer.add(parentView.asWidget());
                logger.info("After adding, directParentsContainer = " + directParentsContainer);
            } catch (Exception e) {
                logger.severe("Error while adding widget: " + e.getMessage());
            }
        });

    }

    @Override
    public void setMainParent(String parentIri) {
        this.mainParentIri = parentIri;
        for(DirectParentView view : this.directParentViews){
            view.resetStyle();
        }
        markMainParent();
        markEquivalentOnlyParents();
    }

    private void markMainParent() {
        if (this.mainParentIri != null && !this.mainParentIri.isEmpty()) {
            for(DirectParentView parentView : this.directParentViews) {
                if(mainParentIri.equals(parentView.getEntityIri())) {
                    parentView.markParentAsMain();
                    parentView.setTitle("The bolded font highlights the MMS linearization parent");
                    return;
                }
            }
        }
    }

    private void markEquivalentOnlyParents() {
        if(this.equivalentOnlyParents != null && !this.equivalentOnlyParents.isEmpty()) {
            for(OWLEntityData equivalentOnlyParent: equivalentOnlyParents) {
                Optional<DirectParentView> parentViewOptional = this.directParentViews.stream()
                        .filter(directParentView -> directParentView.getEntityIri().equals(equivalentOnlyParent.getIri().toString()))
                        .findFirst();
                if(parentViewOptional.isPresent()) {
                    parentViewOptional.get().markAsEquivalentOnly();
                    parentViewOptional.get().setTitle("Parents displayed in gray font are coming from logical definitions only");
                } else {
                    logger.log(Level.SEVERE, "Couldn't find equivalent parent for " + equivalentOnlyParent.getBrowserText());
                }
            }
        }
    }

}

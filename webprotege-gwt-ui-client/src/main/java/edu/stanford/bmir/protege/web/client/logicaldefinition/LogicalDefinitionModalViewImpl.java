package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyPopupPresenter;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyPopupPresenterFactory;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import org.semanticweb.owlapi.model.OWLClass;

import javax.inject.Inject;
import java.util.Set;
import java.util.logging.Logger;

public class LogicalDefinitionModalViewImpl extends Composite implements LogicalDefinitionModalView {
    Logger logger = java.util.logging.Logger.getLogger("LogicalDefinitionModal");

    @UiField
    HTMLPanel paneContainer;
    private final HierarchyPopupPresenterFactory hierarchyPopupPresenterFactory;

    private static final LogicalDefinitionModalViewImpl.LogicalDefinitionModalViewImplUiBinder ourUiBinder = GWT.create(LogicalDefinitionModalViewImpl.LogicalDefinitionModalViewImplUiBinder.class);
    private final WebProtegeEventBus eventBus;


    @Inject
    public LogicalDefinitionModalViewImpl(HierarchyPopupPresenterFactory hierarchyPopupPresenterFactory, WebProtegeEventBus eventBus) {
        this.hierarchyPopupPresenterFactory = hierarchyPopupPresenterFactory;
        this.eventBus = eventBus;

        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @Override
    public void setWidget(IsWidget w) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void showTree(Set<OWLClass> roots, LogicalDefinitionTableConfig.SelectedAxisValueHandler valueHandler) {
        HierarchyPopupPresenter hierarchyPopupPresenter = this.hierarchyPopupPresenterFactory.create(
                ClassHierarchyDescriptor.get(roots)
        );
        hierarchyPopupPresenter.start(eventBus);
        hierarchyPopupPresenter.show(this, (entityNode) -> {
            logger.info("ALEX " + entityNode);
            valueHandler.handleSelectAxisValue(entityNode);
        });

        paneContainer.add(hierarchyPopupPresenter.getView());
    }

    interface LogicalDefinitionModalViewImplUiBinder extends UiBinder<HTMLPanel, LogicalDefinitionModalViewImpl> {

    }
}

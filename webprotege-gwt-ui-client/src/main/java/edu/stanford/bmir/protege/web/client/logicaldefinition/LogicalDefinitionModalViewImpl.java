package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.auto.factory.Provided;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class LogicalDefinitionModalViewImpl extends Composite implements LogicalDefinitionModalView {
    Logger logger = java.util.logging.Logger.getLogger("LogicalDefinitionModal");

    @UiField
    HTMLPanel paneContainer;
    private static final LogicalDefinitionModalViewImpl.LogicalDefinitionModalViewImplUiBinder ourUiBinder = GWT.create(LogicalDefinitionModalViewImpl.LogicalDefinitionModalViewImplUiBinder.class);
    private final WebProtegeEventBus eventBus;

    private LogicalDefinitionResourceBundle.LogicalDefinitionCss style;

    private Optional<EntityNode> selectedEntity = Optional.empty();

    @Nonnull
    private final HierarchyPopupView view;

    @Nonnull
    private final EntityHierarchyModel model;

    @Inject
    public LogicalDefinitionModalViewImpl(WebProtegeEventBus eventBus,
                                          HierarchyPopupView view,
                                          @Nonnull EntityHierarchyModel model
                                          ) {
        this.eventBus = eventBus;
        this.view = view;
        this.model = checkNotNull(model);
        initWidget(ourUiBinder.createAndBindUi(this));

        LogicalDefinitionResourceBundle.INSTANCE.style().ensureInjected();
        style = LogicalDefinitionResourceBundle.INSTANCE.style();

    }


    @Override
    public void setWidget(IsWidget w) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void showTree(Set<OWLClass> roots, Consumer<EntityNode> mouseDownHandler) {
        paneContainer.clear();

        model.start(eventBus,  ClassHierarchyDescriptor.get(roots));
        view.setModel(model);

        view.setSelectionChangedHandler(sel -> {
            this.selectedEntity = Optional.of(sel);
        });
        view.setMouseDownHandler(mouseDownHandler);
        view.addCssClassToMain(style.mainModalView());
        paneContainer.add(view);
    }

    @Override
    public Optional<EntityNode> getSelectedEntity() {
        return selectedEntity;
    }

    private void closePanelAndSendSelection(EntityNode entityNode,  LogicalDefinitionTableConfig.SelectedAxisValueHandler valueHandler) {

    }

    interface LogicalDefinitionModalViewImplUiBinder extends UiBinder<HTMLPanel, LogicalDefinitionModalViewImpl> {

    }
}

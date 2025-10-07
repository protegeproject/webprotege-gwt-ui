package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Sep 2018
 */
public class HierarchyPopupPresenter {

   @Nonnull
   private final HierarchyDescriptor hierarchyDescriptor;

    @Nonnull
    private final HierarchyPopupView view;

    @Nonnull
    private final EntityHierarchyModel model;

    private PopupPanel popupPanel;

    private Optional<OWLEntity> selectedEntity = Optional.empty();


    @Inject
    @AutoFactory
    public HierarchyPopupPresenter(@Nonnull HierarchyDescriptor hierarchyDescriptor,
                                   @Provided @Nonnull HierarchyPopupView view,
                                   @Provided @Nonnull EntityHierarchyModel model) {
        this.hierarchyDescriptor = checkNotNull(hierarchyDescriptor);
        this.view = view;
        this.model = checkNotNull(model);
        popupPanel = new PopupPanel(true, true);
        popupPanel.setAutoHideEnabled(true);
        popupPanel.setAutoHideOnHistoryEventsEnabled(true);
        Event.addNativePreviewHandler(this::handleEscapeKey);
    }

    public void start(@Nonnull WebProtegeEventBus eventBus) {
        model.start(eventBus,
                hierarchyDescriptor);
        view.setModel(model);
    }

    public void show(@Nonnull UIObject target, Consumer<EntityNode> popupClosedHandler) {
        this.show(target, popupClosedHandler, true);
    }

    public void show(@Nonnull UIObject target, Consumer<EntityNode> popupClosedHandler, boolean isCurrSelectionLocked) {
        setPopUpWidgetAndShowRelativeTo(target);
        if(isCurrSelectionLocked){
            view.setSelectionChangedHandler(sel -> {
                if (!Optional.of(sel.getEntity()).equals(selectedEntity)) {
                    closePanelAndSendSelection(sel, popupClosedHandler);
                }
            });
            return;
        }

        view.setMouseDownHandler((entityNode -> {
            closePanelAndSendSelection(entityNode, popupClosedHandler);
        }));
    }

    private void closePanelAndSendSelection(EntityNode entityNode, Consumer<EntityNode> popupClosedHandler) {
        popupPanel.hide();
        selectedEntity = Optional.of(entityNode.getEntity());
        popupClosedHandler.accept(entityNode);
    }

    private void setPopUpWidgetAndShowRelativeTo(UIObject target) {
        popupPanel.setWidget(view);
        popupPanel.showRelativeTo(target);
    }

    public void setSelectedEntity(@Nonnull OWLEntity selectedEntity) {
        this.selectedEntity = Optional.of(selectedEntity);
        view.revealEntity(selectedEntity);
    }


    public HierarchyPopupView getView(){
        return view;
    }

    public void setDisplayNameSettings(@Nonnull DisplayNameSettings settings) {
        view.setDisplayNameSettings(settings);
    }

    private void handleEscapeKey(Event.NativePreviewEvent event) {
        if (event.getTypeInt() == Event.ONKEYDOWN && event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            popupPanel.hide();
            selectedEntity = Optional.empty();
        }
    }
}

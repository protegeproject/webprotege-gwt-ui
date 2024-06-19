package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.PortletId;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.protege.widgetmap.client.HasFixedPrimaryAxisSize;
import edu.stanford.protege.widgetmap.client.WidgetMapper;
import edu.stanford.protege.widgetmap.client.view.FixedSizeViewHolder;
import edu.stanford.protege.widgetmap.client.view.ViewHolder;
import edu.stanford.protege.widgetmap.shared.node.NodeProperties;
import edu.stanford.protege.widgetmap.shared.node.TerminalNode;
import edu.stanford.protege.widgetmap.shared.node.TerminalNodeId;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class PortletWidgetMapper implements WidgetMapper, HasDispose {

    private final Logger logger = Logger.getLogger("PortletWidgetMapper");

    private static final String DROP_ZONE = "drop-zone";

    private final Map<TerminalNodeId, ViewHolder> nodeId2ViewHolderMap = new HashMap<>();

    private final PortletFactory portletFactory;

    private boolean viewsCloseable = true;

    private final Provider<PortletUi> portletUiProvider;

    private final Provider<WebProtegeEventBus> eventBusProvider;

    private Consumer<TerminalNode> nodePropertiesChangedHandler = node -> {};

    private List<HasDispose> disposables = new ArrayList<>();

    @Inject
    public PortletWidgetMapper(@Nonnull PortletFactory portletFactory,
                               Provider<PortletUi> portletUiProvider,
                               Provider<WebProtegeEventBus> eventBusProvider) {
        this.portletFactory = portletFactory;
        this.portletUiProvider = portletUiProvider;
        this.eventBusProvider = eventBusProvider;
    }


    public boolean isViewsCloseable() {
        return viewsCloseable;
    }

    public void setViewsCloseable(boolean viewsCloseable) {
        this.viewsCloseable = viewsCloseable;
    }

    public void setNodePropertiesChangedHandler(Consumer<TerminalNode> nodePropertiesChangedHandler) {
        this.nodePropertiesChangedHandler = checkNotNull(nodePropertiesChangedHandler);
    }

    @Override
    public IsWidget getWidget(TerminalNode terminalNode) {
        logger.log(Level.FINE, "logger.infoGetting widget for TerminalNode: " + terminalNode);
        ViewHolder cachedViewHolder = nodeId2ViewHolderMap.get(terminalNode.getNodeId());
        if (cachedViewHolder != null) {
            logger.log(Level.FINE, "logger.infoUsing cached view: " + terminalNode);
            return cachedViewHolder;
        }
        String portletClass = terminalNode.getNodeProperties().getPropertyValue("portlet", "");
        logger.log(Level.FINE, "logger.infoInstantiate portlet: " + portletClass);
        ViewHolder viewHolder;
        if (!portletClass.isEmpty()) {
            Optional<WebProtegePortletComponents> thePortlet = portletFactory.createPortlet(new PortletId(portletClass));
            if (thePortlet.isPresent()) {
                logger.log(Level.FINE, "logger.infoCreated portlet from auto-generated factory");
                WebProtegePortletComponents portletComponents = thePortlet.get();
                WebProtegePortletPresenter portletPresenter = portletComponents.getPresenter();
                viewHolder = createViewHolder(terminalNode,
                                              portletComponents,
                                              terminalNode.getNodeProperties());
            }
            else {
                CouldNotFindPortletWidget childWidget = new CouldNotFindPortletWidget();
                childWidget.setPortletId(portletClass);
                viewHolder = new ViewHolder(childWidget, NodeProperties.emptyNodeProperties());
            }

        }
        else {
            viewHolder = new ViewHolder(new Label("No view class specified"), NodeProperties.emptyNodeProperties());
        }
        viewHolder.setCloseable(viewsCloseable);
        nodeId2ViewHolderMap.put(terminalNode.getNodeId(), viewHolder);
        return viewHolder;

    }


    private ViewHolder createViewHolder(@Nonnull TerminalNode node,
                                        @Nonnull WebProtegePortletComponents portlet,
                                        @Nonnull NodeProperties nodeProperties) {
        PortletUi portletUi = portletUiProvider.get();
        portletUi.setNodeProperties(nodeProperties);
        portletUi.setNodePropertiesChangedHandler((ui, np) -> {
            node.setNodeProperties(np);
            nodePropertiesChangedHandler.accept(node);
        });
        WebProtegeEventBus eventBus = eventBusProvider.get();
        disposables.add(eventBus);
        portletUi.setTitle(portlet.getPortletDescriptor().getTitle());
        WebProtegePortletPresenter portletPresenter = portlet.getPresenter();
        disposables.add(portletPresenter);
        portletPresenter.start(portletUi, eventBus);
        ViewHolder viewHolder;
        if (portletPresenter instanceof HasFixedPrimaryAxisSize) {
            viewHolder = new FixedSizeViewHolder(portletUi.asWidget(), nodeProperties, ((HasFixedPrimaryAxisSize) portletPresenter).getFixedPrimaryAxisSize());
        }
        else {
            viewHolder = new ViewHolder(portletUi, nodeProperties);
        }
        viewHolder.addStyleName(DROP_ZONE);
        viewHolder.addCloseHandler(event -> {
            disposables.remove(eventBus);
            eventBus.dispose();
            disposables.remove(portletPresenter);
            portletPresenter.dispose();
            nodeId2ViewHolderMap.remove(node.getNodeId());
        });
        return viewHolder;
    }

    @Override
    public void dispose() {
        disposables.forEach(HasDispose::dispose);
    }
}

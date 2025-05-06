package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.WebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.ui.DisplayContextManager;
import edu.stanford.bmir.protege.web.client.ui.HasDisplayContextBuilder;
import edu.stanford.bmir.protege.web.client.ui.LayoutUtil;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4;
import edu.stanford.bmir.protege.web.shared.DisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.protege.widgetmap.client.RootNodeChangedHandler;
import edu.stanford.protege.widgetmap.client.WidgetMapPanel;
import edu.stanford.protege.widgetmap.client.WidgetMapPanelManager;
import edu.stanford.protege.widgetmap.client.WidgetMapRootWidget;
import edu.stanford.protege.widgetmap.shared.node.Node;
import edu.stanford.protege.widgetmap.shared.node.NodeProperties;
import edu.stanford.protege.widgetmap.shared.node.TerminalNode;
import edu.stanford.protege.widgetmap.shared.node.TerminalNodeId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static com.google.common.base.MoreObjects.toStringHelper;


public final class PerspectiveImpl extends Composite implements IsWidget, Perspective {

    private static final Logger logger = Logger.getLogger(PerspectiveImpl.class.getName());

    private final PerspectiveId perspectiveId;

    private final WidgetMapPanel widgetMapPanel;

    private final DispatchServiceManager dispatchServiceManager;

    private final PortletWidgetMapper widgetMapper;

    private Optional<Node> rootNode;

    private Consumer<TerminalNode> nodePropertiesChangedHandler = node -> {};

    private final DisplayContextManager displayContextManager = new DisplayContextManager(this::fillDisplayContext);

    @Inject
    public PerspectiveImpl(@Nonnull final PerspectiveId perspectiveId,
                           @Nonnull DispatchServiceManager dispatchServiceManager,
                           @Nonnull PortletWidgetMapper widgetMapper) {
        super();
        this.perspectiveId = perspectiveId;
        this.dispatchServiceManager = dispatchServiceManager;
        this.widgetMapper = widgetMapper;
        this.widgetMapper.setNodePropertiesChangedHandler(tn -> nodePropertiesChangedHandler.accept(tn));
        WidgetMapRootWidget rootWidget = new WidgetMapRootWidget();
        WidgetMapPanelManager panelManager = new WidgetMapPanelManager(rootWidget, widgetMapper);
        widgetMapPanel = new WidgetMapPanel(rootWidget, panelManager);
        initWidget(widgetMapPanel);
        rootNode = Optional.empty();
        widgetMapper.setParentDisplayContextBuilder(this);
    }

    @Override
    public void setViewsCloseable(boolean closeable) {
        widgetMapper.setViewsCloseable(closeable);
    }

    public void setNodePropertiesChangedHandler(@Nonnull Consumer<TerminalNode> nodePropertiesChangedHandler) {
        this.nodePropertiesChangedHandler = nodePropertiesChangedHandler;
    }

    @Override
    public void setRootNodeChangedHandler(RootNodeChangedHandler handler) {
        widgetMapPanel.addRootNodeChangedHandler(handler);
    }

    @Override
    public void setEmptyPerspectiveWidget(IsWidget widget) {
        widgetMapPanel.setEmptyWidget(widget);
    }

    @Override
    public PerspectiveId getPerspectiveId() {
        return perspectiveId;
    }

    private void removePortlet(final WebProtegePortletPresenter portlet) {
        portlet.dispose();
    }

    public String getLabel() {
        return getTitle();
    }

    public void setLabel(final String label) {
        setTitle(label);
    }

    public boolean isVisible() {
        return super.isVisible();
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public void dropView(String className) {

        NodeProperties nodeProperties = NodeProperties.builder().setValue("portlet", className).build();
        TerminalNode terminalNode = new TerminalNode(TerminalNodeId.get(UuidV4.uuidv4()), nodeProperties);
        widgetMapPanel.doDrop(terminalNode);
    }

    @Override
    public Optional<Node> getRootNode() {
        return widgetMapPanel.getRootNode();
    }

    @Override
    public void setRootNode(Optional<Node> rootNode) {
        logger.fine("[Perspective] Root node set: " + rootNode);
        this.rootNode = rootNode;
        if (rootNode.isPresent()) {
            dispatchServiceManager.beginBatch();
            try {
                widgetMapPanel.setRootNode(rootNode.get());
            } finally {
                dispatchServiceManager.executeCurrentBatch();
            }
        }
        else {
            logger.fine("[Perspective] Root node is not present");
        }
    }

    @Override
    public void onResize() {
        logger.fine("[Perspective] onResize");
        LayoutUtil.setBounds(widgetMapPanel, 0, 0, 0, 0);
        widgetMapPanel.onResize();

    }

    @Override
    public void dispose() {
        widgetMapper.dispose();
    }


    @Override
    public String toString() {
        return toStringHelper("Perspective")
                .add("perspectiveId", perspectiveId)
                .toString();
    }

    @Override
    public void setParentDisplayContextBuilder(HasDisplayContextBuilder parent) {
        displayContextManager.setParentDisplayContextBuilder(parent);
    }

    private void fillDisplayContext(DisplayContextBuilder displayContextBuilder) {
        displayContextBuilder.setPerspectiveId(perspectiveId);
    }

    @Override
    public DisplayContextBuilder fillDisplayContextBuilder() {
        return displayContextManager.fillDisplayContextBuilder();
    }
}

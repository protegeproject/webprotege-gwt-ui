package edu.stanford.bmir.protege.web.client.directparents;

import com.google.auto.factory.*;
import edu.stanford.bmir.protege.web.client.entity.EntityNodeHtmlRenderer;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class DirectParentPresenter implements HasDispose {

    private final static java.util.logging.Logger logger = Logger.getLogger(DirectParentPresenter.class.getName());

    private final EntityNode entityNode;

    private final DirectParentView view;

    private final EntityNodeHtmlRenderer nodeRenderer;

    @AutoFactory
    @Inject
    public DirectParentPresenter(@Nonnull EntityNode entityNode,
                                 @Provided @Nonnull DirectParentView view,
                                 @Provided @Nonnull EntityNodeHtmlRenderer nodeRenderer) {
        this.entityNode = checkNotNull(entityNode);
        this.view = checkNotNull(view);
        this.nodeRenderer = nodeRenderer;
    }

    public void start() {
        logger.info("Rendering entity: " + nodeRenderer.getHtmlRendering(entityNode));
        this.view.setEntity(nodeRenderer.getHtmlRendering(entityNode));
    }

    @Override
    public void dispose() {
        view.clear();
    }

    public DirectParentView getView() {
        return view;
    }
}

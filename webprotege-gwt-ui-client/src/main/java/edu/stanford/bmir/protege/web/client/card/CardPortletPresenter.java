package edu.stanford.bmir.protege.web.client.card;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.FormContainer;
import edu.stanford.bmir.protege.web.client.form.FormTabBarPresenter;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletChooserPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.PortletId;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Portlet(id = "webprotege.card",
        title = "Card view",
        tooltip = "Presents a stack of cards that display information about the selected entity.")
public class CardPortletPresenter extends AbstractWebProtegePortletPresenter {

    private List<EntityCardPresenter> cardPresenters = new ArrayList<>();

    private final CardPortletView view;
    @Nonnull
    private final PortletCardPresenterFactory portletCardPresenterFactory;
    @Nonnull
    private final FormCardPresenterFactory formCardPresenterFactory;
    @Nonnull
    private final Provider<EntityCardView> entityCardViewProvider;

    private final PortletChooserPresenter portletChooserPresenter;

    private final FormTabBarPresenter tabBarPresenter;

    @Inject
    public CardPortletPresenter(@Nonnull ProjectId projectId,
                                @Nonnull SelectionModel selectionModel,
                                @Nonnull DisplayNameRenderer displayNameRenderer,
                                @Nonnull DispatchServiceManager dispatch, CardPortletView view,
                                @Nonnull PortletCardPresenterFactory portletCardPresenterFactory,
                                @Nonnull FormCardPresenterFactory formCardPresenterFactory,
                                @Nonnull Provider<EntityCardView> entityCardViewProvider, PortletChooserPresenter portletChooserPresenter, FormTabBarPresenter tabBarPresenter) {
        super(selectionModel, projectId, displayNameRenderer, dispatch);
        this.view = view;
        this.portletCardPresenterFactory = portletCardPresenterFactory;
        this.formCardPresenterFactory = formCardPresenterFactory;
        this.entityCardViewProvider = entityCardViewProvider;
        this.portletChooserPresenter = portletChooserPresenter;
        this.tabBarPresenter = tabBarPresenter;
        cardPresenters.add(portletCardPresenterFactory.create(new PortletId("portlets.CommentedEntities")));
        cardPresenters.add(formCardPresenterFactory.create(FormId.get("b4c85b41-e37c-442b-ac8b-7c6abe80f012")));
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(view);
        cardPresenters.forEach(p -> {
            EntityCardView cardView = entityCardViewProvider.get();
            cardView.setLabel(p.getLabel().get("en"));
            p.start(cardView, eventBus);
            view.addView(cardView);
        });
        tabBarPresenter.start(view.getTabBarContainer());
        portletUi.addAction(new AbstractUiAction("Add view...") {
            @Override
            public void execute() {
                portletChooserPresenter.show(portletId -> {
                    EntityCardView v = entityCardViewProvider.get();
                    EntityCardPresenter p = portletCardPresenterFactory.create(portletId);
                    p.start(v, eventBus);
                    view.addView(v);
                    new FormContainer() {
                        @Override
                        public void setWidget(IsWidget w) {

                        }

                        @Override
                        public boolean isVisible() {
                            return false;
                        }

                        @Override
                        public void setVisible(boolean visible) {

                        }

                        @Override
                        public Widget asWidget() {
                            return null;
                        }
                    };
                });
            }
        });
    }

    @Override
    protected void handleReloadRequest() {

    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {

    }
}


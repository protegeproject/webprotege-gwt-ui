package edu.stanford.bmir.protege.web.client.card;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.library.common.HasEditable;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.shared.PortletId;
import edu.stanford.bmir.protege.web.shared.card.CardId;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

public class PortletCardPresenter implements EntityCardPresenter {


    private final CardId cardId;
    private final PortletId portletId;

    private final LanguageMap label;
    private final Color color;
    private final Color backgroundColor;
    private final PortletFactory portletFactory;

    private final PortletUi portletUi;

    private Optional<WebProtegePortletPresenter> portletPresenter = Optional.empty();

    @AutoFactory
    @Inject
    public PortletCardPresenter(CardId cardId,
                                PortletId portletId,
                                LanguageMap label,
                                @Nullable Color color,
                                @Nullable Color backgroundColor,
                                @Provided PortletFactory portletFactory,
                                @Provided PortletUi portletUi,
                                @Provided WebProtegeEventBus eventBus) {
        this.cardId = cardId;
        this.portletId = portletId;
        this.label = label;
        this.color = color;
        this.backgroundColor = backgroundColor;
        this.portletFactory = portletFactory;
        this.portletUi = portletUi;
    }

    @Override
    public CardId getCardId() {
        return cardId;
    }

    public void start(AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        Optional<WebProtegePortletComponents> portlet = portletFactory.createPortlet(portletId);
        portlet.ifPresent(c -> {
            WebProtegePortletPresenter presenter = c.getPresenter();
            presenter.start(portletUi, eventBus);
            this.portletPresenter = Optional.of(presenter);
            container.setWidget(portletUi);
        });
        if(!portlet.isPresent()) {
            CouldNotFindPortletWidget w = new CouldNotFindPortletWidget();
            w.setPortletId(portletId.getPortletId());
            container.setWidget(w);
        }
    }

    @Override
    public LanguageMap getLabel() {
        return label;
    }

    @Override
    public Optional<Color> getColor() {
        return Optional.ofNullable(color);
    }

    @Override
    public Optional<Color> getBackgroundColor() {
        return Optional.ofNullable(backgroundColor);
    }

    @Override
    public boolean isEditor() {
        return portletPresenter.map(p -> p instanceof CardPortletPresenter).orElse(false);
    }

    @Override
    public void setEditable(boolean editable) {
        portletPresenter.ifPresent(p -> {
            if(p instanceof HasEditable) {
                ((HasEditable) p).setEditable(editable);
            }
        });
    }

    @Override
    public void commitChanges() {
        portletPresenter.ifPresent(p -> {
            if(p instanceof CardPortletPresenter) {
                CardPortletPresenter editingSupport = (CardPortletPresenter) p;
                editingSupport.applyEdits();
            }
        });
    }

    @Override
    public void cancelChanges() {
        portletPresenter.ifPresent(p -> {
            if(p instanceof CardPortletPresenter) {
                CardPortletPresenter editingSupport = (CardPortletPresenter) p;
                editingSupport.cancelEdits();
            }
        });
    }

    @Override
    public void requestFocus() {
        portletPresenter.ifPresent(p -> {
            if(p instanceof CardPortletPresenter) {
                CardPortletPresenter editingSupport = (CardPortletPresenter) p;
                editingSupport.requestFocus();
            }
        });
    }
}

package edu.stanford.bmir.protege.web.client.card;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.shared.PortletId;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

public class PortletCardPresenter implements EntityCardPresenter {


    private final PortletId portletId;

    private final PortletFactory portletFactory;

    private final PortletUi portletUi;

    private String title = "Other";

    private Optional<WebProtegePortletPresenter> portletPresenter = Optional.empty();

    @AutoFactory
    @Inject
    public PortletCardPresenter(PortletId portletId,
                                @Provided PortletFactory portletFactory,
                                @Provided PortletUi portletUi,
                                @Provided WebProtegeEventBus eventBus) {
        this.portletId = portletId;
        this.portletFactory = portletFactory;
        this.portletUi = portletUi;
    }

    public void start(AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        Optional<WebProtegePortletComponents> portlet = portletFactory.createPortlet(portletId);
        portlet.ifPresent(c -> {
            WebProtegePortletPresenter presenter = c.getPresenter();
            presenter.start(portletUi, eventBus);

            this.portletPresenter = Optional.of(presenter);
            this.title = c.getPortletDescriptor().getTitle();
            container.setWidget(portletUi);
        });
    }

    @Override
    public LanguageMap getLabel() {
        return LanguageMap.of("en", title);
    }
}

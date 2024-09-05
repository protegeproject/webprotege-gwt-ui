package edu.stanford.bmir.protege.web.client.card;

import edu.stanford.bmir.protege.web.shared.card.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class EntityCardPresenterFactory {

    private final FormCardPresenterFactory formCardPresenterFactory;

    private final PortletCardPresenterFactory portletCardPresenterFactory;

    @Inject
    public EntityCardPresenterFactory(FormCardPresenterFactory formCardPresenterFactory, PortletCardPresenterFactory portletCardPresenterFactory) {
        this.formCardPresenterFactory = formCardPresenterFactory;
        this.portletCardPresenterFactory = portletCardPresenterFactory;
    }

    /**
     * Creates an EntityCardPresenter based on a given CardDescriptor.
     *
     * @param cardDescriptor The CardDescriptor containing the necessary information to create the EntityCardPresenter.
     * @return An EntityCardPresenter.
     */
    @Nonnull
    public EntityCardPresenter create(@Nonnull CardDescriptor cardDescriptor) {
        CardContentDescriptor contentDescriptor = cardDescriptor.getContentDescriptor();
        return contentDescriptor.accept(new CardContentDescriptorVisitor<EntityCardPresenter>() {
            @Override
            public EntityCardPresenter visit(FormCardContentDescriptor formCardContentDescriptor) {
                return formCardPresenterFactory.create(cardDescriptor.getId(),
                        formCardContentDescriptor.getFormId(),
                        cardDescriptor.getLabel(),
                        cardDescriptor.getColor().orElse(null),
                        cardDescriptor.getBackgroundColor().orElse(null));
            }

            @Override
            public EntityCardPresenter visit(PortletCardContentDescriptor portletCardContentDescriptor) {
                return portletCardPresenterFactory.create(cardDescriptor.getId(),
                        portletCardContentDescriptor.getPortletId(),
                        cardDescriptor.getLabel(),
                        cardDescriptor.getColor().orElse(null),
                        cardDescriptor.getBackgroundColor().orElse(null));
            }
        });
    }
}

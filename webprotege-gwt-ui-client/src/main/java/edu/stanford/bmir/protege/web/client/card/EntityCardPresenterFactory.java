package edu.stanford.bmir.protege.web.client.card;

import edu.stanford.bmir.protege.web.shared.card.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

public class EntityCardPresenterFactory {

    private final FormContentEntityCardPresenterFactory formContentPresenterFactory;

    private final CustomContentEntityCardPresenterFactory customContentEntityCardPresenterFactory;

    @Inject
    public EntityCardPresenterFactory(FormContentEntityCardPresenterFactory formContentPresenterFactory, CustomContentEntityCardPresenterFactory customContentEntityCardPresenterFactory) {
        this.formContentPresenterFactory = formContentPresenterFactory;
        this.customContentEntityCardPresenterFactory = customContentEntityCardPresenterFactory;
    }

    /**
     * Creates an EntityCardPresenter based on a given CardDescriptor.
     *
     * @param cardDescriptor The CardDescriptor containing the necessary information to create the EntityCardPresenter.
     * @return An EntityCardPresenter.
     */
    @Nonnull
    public Optional<? extends EntityCardPresenter> create(@Nonnull CardDescriptor cardDescriptor) {
        EntityCardContentDescriptor contentDescriptor = cardDescriptor.getContentDescriptor();
        return contentDescriptor.accept(new EntityCardContentDescriptorVisitor<Optional<? extends EntityCardPresenter>>() {
            @Override
            public Optional<? extends EntityCardPresenter> visit(FormContentDescriptor descriptor) {
                return Optional.of(formContentPresenterFactory.create(
                        descriptor.getFormId()));
            }

            @Override
            public Optional<? extends EntityCardPresenter> visit(CustomContentDescriptor descriptor) {
                return customContentEntityCardPresenterFactory.create(cardDescriptor.getId(),
                        descriptor.getCustomContentId());
            }
        });
    }
}

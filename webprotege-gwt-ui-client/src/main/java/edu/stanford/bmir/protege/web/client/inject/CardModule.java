package edu.stanford.bmir.protege.web.client.inject;

import dagger.Binds;
import dagger.Module;
import edu.stanford.bmir.protege.web.client.card.linearization.LinearizationCardView;
import edu.stanford.bmir.protege.web.client.card.linearization.LinearizationCardViewImpl;

@Module
public abstract class CardModule {
    @Binds
    public abstract LinearizationCardView bindLinearizationCardView(LinearizationCardViewImpl impl);
}

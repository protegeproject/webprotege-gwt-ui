package edu.stanford.bmir.protege.web.client.ui;

import edu.stanford.bmir.protege.web.shared.DisplayContextBuilder;

import java.util.Optional;

public class DisplayContextManager {

    private HasDisplayContextBuilder parent;

    private final DisplayContextFiller filler;

    public DisplayContextManager(DisplayContextFiller filler) {
        this.filler = filler;
    }

    public void setParentDisplayContextBuilder(HasDisplayContextBuilder parent) {
        this.parent = parent;
    }

    public Optional<HasDisplayContextBuilder> getParentDisplayContextBuilder() {
        return Optional.ofNullable(parent);
    }

    public DisplayContextBuilder fillDisplayContextBuilder() {
        DisplayContextBuilder builder = getParentDisplayContextBuilder()
                .map(HasDisplayContextBuilder::fillDisplayContextBuilder)
                .orElse(DisplayContextBuilder.builder());
        filler.fillDisplayContext(builder);
        return builder;
    }

}

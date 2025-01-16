package edu.stanford.bmir.protege.web.client.ui;

import edu.stanford.bmir.protege.web.shared.DisplayContext;

import java.util.Optional;
import java.util.function.Consumer;

public class DisplayContextManager {

    private HasDisplayContext parent;

    private Consumer<DisplayContext> filler;

    public DisplayContextManager(Consumer<DisplayContext> filler) {
        this.filler = filler;
    }

    public void setParentDisplayContext(HasDisplayContext parent) {
        this.parent = parent;
    }

    public Optional<HasDisplayContext> getDisplayContextParent() {
        return Optional.ofNullable(parent);
    }

    public DisplayContext getDisplayContext() {
        DisplayContext displayContext = new DisplayContext();
        filler.accept(displayContext);
        getDisplayContextParent().ifPresent(p -> displayContext.merge(p.getDisplayContext()));
        return displayContext;
    }

}

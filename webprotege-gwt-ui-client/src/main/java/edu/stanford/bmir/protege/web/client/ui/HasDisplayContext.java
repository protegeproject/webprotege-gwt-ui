package edu.stanford.bmir.protege.web.client.ui;

import edu.stanford.bmir.protege.web.shared.DisplayContext;

import java.util.Optional;

public interface HasDisplayContext {

    void setParentDisplayContext(HasDisplayContext parent);

    Optional<HasDisplayContext> getParentDisplayContext();

    DisplayContext getDisplayContext();
}

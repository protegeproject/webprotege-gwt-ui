package edu.stanford.bmir.protege.web.client.ui;

import edu.stanford.bmir.protege.web.shared.DisplayContextBuilder;

import java.util.Optional;

public interface HasDisplayContextBuilder {

    void setParentDisplayContextBuilder(HasDisplayContextBuilder parent);

    DisplayContextBuilder fillDisplayContextBuilder();
}

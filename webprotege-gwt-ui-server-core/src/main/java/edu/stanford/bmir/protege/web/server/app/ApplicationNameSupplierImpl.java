package edu.stanford.bmir.protege.web.server.app;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-04
 */
public class ApplicationNameSupplierImpl implements ApplicationNameSupplier {

    @Inject
    public ApplicationNameSupplierImpl() {
    }

    @Override
    public String get() {
        return "WebProtégé";
    }
}

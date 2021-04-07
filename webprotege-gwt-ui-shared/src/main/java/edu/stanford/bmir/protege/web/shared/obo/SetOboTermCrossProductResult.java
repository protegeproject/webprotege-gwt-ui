package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class SetOboTermCrossProductResult implements Result {

    private SetOboTermCrossProductResult() {
    }

    public static SetOboTermCrossProductResult create() {
        return new SetOboTermCrossProductResult();
    }
}

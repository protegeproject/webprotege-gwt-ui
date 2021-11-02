package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-01
 */
public class InternalServerError extends RuntimeException implements IsSerializable {

    public InternalServerError(String message) {
        super(message);
    }
}

package edu.stanford.bmir.protege.web.server.upload;

import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-15
 */
@ApplicationSingleton
public class StorageException extends RuntimeException {

    public StorageException(Throwable cause) {
        super(cause);
    }
}

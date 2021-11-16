package edu.stanford.bmir.protege.web.server.upload;

import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import java.nio.file.Path;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-15
 */
@ApplicationSingleton
public interface StorageService {

    FileSubmissionId storeUpload(Path tempFile);
}

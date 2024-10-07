package edu.stanford.bmir.protege.web.client.postcoordination;

import edu.stanford.bmir.protege.web.shared.postcoordination.WhoficEntityPostCoordinationSpecification;

import java.util.Optional;

public interface SaveButtonHandler {

    void saveValues(Optional<WhoficEntityPostCoordinationSpecification> specificationOptional);
}

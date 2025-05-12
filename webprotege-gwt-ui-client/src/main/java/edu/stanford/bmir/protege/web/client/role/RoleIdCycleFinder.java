package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.shared.access.RoleId;

import java.util.List;
import java.util.Optional;

public interface RoleIdCycleFinder {

    Optional<List<RoleId>> findCycle(RoleId roleId);
}

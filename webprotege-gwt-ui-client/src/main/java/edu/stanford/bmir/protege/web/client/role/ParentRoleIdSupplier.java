package edu.stanford.bmir.protege.web.client.role;

import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;

public interface ParentRoleIdSupplier extends Supplier<Set<RoleId>> {

}

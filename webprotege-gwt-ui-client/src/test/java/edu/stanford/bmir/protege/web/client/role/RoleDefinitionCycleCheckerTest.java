package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;
import edu.stanford.bmir.protege.web.shared.permissions.RoleType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class RoleDefinitionCycleCheckerTest {

    private final RoleDefinitionCycleFinder finder = new RoleDefinitionCycleFinder();

    @Test
    public void shouldDetectNoCycle() {
        RoleId roleA = RoleId.valueOf("A");
        RoleId roleB = RoleId.valueOf("B");
        RoleId roleC = RoleId.valueOf("C");

        RoleDefinition defA = RoleDefinition.get(roleA,  RoleType.PROJECT_ROLE, "", "", singletonList(roleB), Collections.emptyList());
        RoleDefinition defB = RoleDefinition.get(roleB, RoleType.PROJECT_ROLE, "","", singletonList(roleC), Collections.emptyList());
        RoleDefinition defC = RoleDefinition.get(roleC, RoleType.PROJECT_ROLE, "", "", Collections.emptyList(), Collections.emptyList());

        List<RoleDefinition> defs = Arrays.asList(defA, defB, defC);

        assertFalse(finder.findCycle(defs, roleA).isPresent());
        assertFalse(finder.findCycle(defs, roleB).isPresent());
        assertFalse(finder.findCycle(defs, roleC).isPresent());
    }

    @Test
    public void shouldDetectCycleAndReturnPath() {
        RoleId roleA = RoleId.valueOf("A");
        RoleId roleB = RoleId.valueOf("B");
        RoleId roleC = RoleId.valueOf("C");

        RoleDefinition defA = RoleDefinition.get(roleA, RoleType.PROJECT_ROLE, "", "", singletonList(roleB), Collections.emptyList());
        RoleDefinition defB = RoleDefinition.get(roleB, RoleType.PROJECT_ROLE, "", "", singletonList(roleC), Collections.emptyList());
        RoleDefinition defC = RoleDefinition.get(roleC, RoleType.PROJECT_ROLE, "", "", singletonList(roleA), Collections.emptyList());

        List<RoleDefinition> defs = Arrays.asList(defA, defB, defC);

        Optional<List<RoleId>> cycleFromA = finder.findCycle(defs, roleA);
        assertTrue(cycleFromA.isPresent());
        List<RoleId> path = cycleFromA.get();
        assertEquals(roleA, path.get(0));
        assertEquals(roleB, path.get(1));
        assertEquals(roleC, path.get(2));
        assertEquals(roleA, path.get(3)); // cycle closes here
    }
}

package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;

import java.util.*;

/**
 * Checks for cycles in a role hierarchy and returns the path of the first detected cycle, if any.
 */
public class RoleDefinitionCycleFinder {

    /**
     * Detects a cycle in the role hierarchy starting from the given role. If a cycle is found,
     * returns the path of {@link RoleId}s forming the cycle.
     *
     * @param roleDefinitions The list of all role definitions.
     * @param startingId      The ID of the role to start checking from.
     * @return An {@code Optional} containing the list of {@code RoleId}s forming the cycle, or empty if none is found.
     */
    public Optional<List<RoleId>> findCycle(List<RoleDefinition> roleDefinitions, RoleId startingId) {
        Map<RoleId, RoleDefinition> roleMap = new HashMap<>();
        for (RoleDefinition def : roleDefinitions) {
            roleMap.put(def.getRoleId(), def);
        }

        Set<RoleId> visited = new HashSet<>();
        List<RoleId> path = new ArrayList<>();
        return dfs(startingId, roleMap, visited, path);
    }

    /**
     * Performs DFS traversal to find a cycle path.
     *
     * @param currentId The current role being visited.
     * @param roleMap   A map of {@link RoleId} to {@link RoleDefinition}.
     * @param visited   The set of nodes already fully explored.
     * @param path      The current traversal path.
     * @return An {@code Optional} containing the cycle path, or empty if none is found.
     */
    private Optional<List<RoleId>> dfs(RoleId currentId,
                                       Map<RoleId, RoleDefinition> roleMap,
                                       Set<RoleId> visited,
                                       List<RoleId> path) {
        if (path.contains(currentId)) {
            // Cycle detected. Reconstruct cycle path from first occurrence to end
            int startIndex = path.indexOf(currentId);
            List<RoleId> cycle = new ArrayList<>(path.subList(startIndex, path.size()));
            cycle.add(currentId); // to close the loop
            return Optional.of(cycle);
        }

        if (visited.contains(currentId)) {
            return Optional.empty();
        }

        path.add(currentId);

        RoleDefinition def = roleMap.get(currentId);
        if (def != null) {
            for (RoleId parentId : def.getParentRoles()) {
                Optional<List<RoleId>> result = dfs(parentId, roleMap, visited, path);
                if (result.isPresent()) {
                    return result;
                }
            }
        }

        path.remove(path.size() - 1); // backtrack
        visited.add(currentId);
        return Optional.empty();
    }
}

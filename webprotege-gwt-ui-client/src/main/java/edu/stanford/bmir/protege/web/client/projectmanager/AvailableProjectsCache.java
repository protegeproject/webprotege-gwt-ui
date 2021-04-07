package edu.stanford.bmir.protege.web.client.projectmanager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import edu.stanford.bmir.protege.web.shared.project.AvailableProject;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 11/04/2014
 */
public class AvailableProjectsCache {

    private Map<ProjectId, AvailableProject> cache = Maps.newHashMap();

    public void add(AvailableProject availableProject) {
        cache.put(availableProject.getProjectId(), availableProject);
    }

    public boolean remove(ProjectId projectId) {
        return cache.remove(projectId) != null;
    }

    public void setAvailableProjects(Collection<AvailableProject> availableProjects) {
        cache.clear();
        for(AvailableProject availableProject : availableProjects) {
            cache.put(availableProject.getProjectId(), availableProject);
        }
    }

    public List<AvailableProject> getAvailableProjectsList() {
        return Lists.newArrayList(cache.values());
    }

    public Optional<AvailableProject> getProjectDetails(ProjectId projectId) {
        AvailableProject projectDetails = cache.get(projectId);
        return Optional.ofNullable(projectDetails);
    }

    public boolean setInTrash(ProjectId projectId, boolean inTrash) {
        AvailableProject availableProject = cache.get(projectId);
        if(availableProject == null) {
            return false;
        }
        if(availableProject.isInTrash() == inTrash) {
            return false;
        }
        AvailableProject replacement = AvailableProject.get(
                availableProject.getProjectId(),
                availableProject.getDisplayName(),
                availableProject.getDescription(),
                availableProject.getOwner(),
                inTrash,
                availableProject.getCreatedAt(),
                availableProject.getCreatedBy(),
                availableProject.getLastModifiedAt(),
                availableProject.getLastModifiedBy(),
                availableProject.isDownloadable(),
                availableProject.isTrashable(),
                availableProject.getLastOpenedAt()
        );
        cache.put(projectId, replacement);
        return true;
    }


}

package edu.stanford.bmir.protege.web.shared.sharing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/02/2012
 */
public class ProjectSharingSettings implements Serializable {

    private ProjectId projectId;
    
    private List<SharingSetting> sharingSettings;

    @Nullable
    private SharingPermission linkSharingPermission = null;

    /**
     * Default no-args constructor for GWT serialization purposes.
     */
    private ProjectSharingSettings() {
    }

    public ProjectSharingSettings(@JsonProperty("projectId") ProjectId projectId,
                                  @JsonProperty("linkSharingPermission") Optional<SharingPermission> linkSharingPermission,
                                  @JsonProperty("sharingSettings") List<SharingSetting> sharingSettings) {
        this.projectId = checkNotNull(projectId);
        this.sharingSettings = new ArrayList<>(checkNotNull(sharingSettings));
        this.linkSharingPermission = checkNotNull(linkSharingPermission).orElse(null);
    }

    @JsonProperty("projectId")
    public ProjectId getProjectId() {
        return projectId;
    }

    @JsonProperty("sharingSettings")
    public List<SharingSetting> getSharingSettings() {
        return new ArrayList<>(sharingSettings);
    }

    @JsonProperty("linkSharingPermission")
    public Optional<SharingPermission> getLinkSharingPermission() {
        return Optional.ofNullable(linkSharingPermission);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, linkSharingPermission, sharingSettings);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectSharingSettings)) {
            return false;
        }
        ProjectSharingSettings other = (ProjectSharingSettings) obj;
        return other.projectId.equals(this.projectId) && Objects.equal(this.linkSharingPermission, other.linkSharingPermission) && other.sharingSettings.equals(this.sharingSettings);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectSharingSettings")
                .addValue(projectId)
                .addValue(sharingSettings)
                .toString();
    }
}

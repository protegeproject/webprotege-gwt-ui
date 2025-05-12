package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RoleType {

    @JsonProperty("ApplicationRole")
    APPLICATION_ROLE,

    @JsonProperty("ProjectRole")
    PROJECT_ROLE
}
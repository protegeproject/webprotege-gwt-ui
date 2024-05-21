package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-19
 */
public enum GitHubUserType {

    @JsonProperty("User")
    USER,

    @JsonProperty("Organization")
    ORGANIZATION;

    public static GitHubUserType get(String apiValue) {
        if("Organization".equals(apiValue)) {
            return ORGANIZATION;
        }
        else {
            return USER;
        }
    }
}

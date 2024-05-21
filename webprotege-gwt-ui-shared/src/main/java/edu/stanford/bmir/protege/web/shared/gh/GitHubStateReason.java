package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-07-12
 */
public enum GitHubStateReason {

    @JsonProperty("completed") COMPLETED,

    @JsonProperty("reopened") REOPENED,

    @JsonProperty("not_planned") NOT_PLANNED
}

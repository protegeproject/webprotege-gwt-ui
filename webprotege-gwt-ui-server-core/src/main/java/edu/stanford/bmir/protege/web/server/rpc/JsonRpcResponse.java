package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.app.GetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.shared.app.SetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.shared.auth.ChangePasswordResult;
import edu.stanford.bmir.protege.web.shared.bulkop.EditAnnotationsResult;
import edu.stanford.bmir.protege.web.shared.bulkop.MoveEntitiesToParentResult;
import edu.stanford.bmir.protege.web.shared.bulkop.SetAnnotationValueResult;
import edu.stanford.bmir.protege.web.shared.change.GetProjectChangesResult;
import edu.stanford.bmir.protege.web.shared.change.GetWatchedEntityChangesResult;
import edu.stanford.bmir.protege.web.shared.change.RevertRevisionResult;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsResult;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsResult;
import edu.stanford.bmir.protege.web.shared.dispatch.BatchResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.*;
import edu.stanford.bmir.protege.web.shared.entity.DeleteEntitiesResult;
import edu.stanford.bmir.protege.web.shared.entity.GetDeprecatedEntitiesResult;
import edu.stanford.bmir.protege.web.shared.entity.LookupEntitiesResult;
import edu.stanford.bmir.protege.web.shared.entity.MergeEntitiesResult;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsPageContainingIndividualResult;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsResult;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdCompletionsResult;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPossibleItemCompletionsResult;
import edu.stanford.bmir.protege.web.shared.itemlist.GetUserIdCompletionsResult;
import edu.stanford.bmir.protege.web.shared.lang.GetProjectLangTagsResult;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectResult;
import edu.stanford.bmir.protege.web.shared.merge_add.ExistingOntologyMergeAddResult;
import edu.stanford.bmir.protege.web.shared.merge_add.GetAllOntologiesResult;
import edu.stanford.bmir.protege.web.shared.merge_add.NewOntologyMergeAddResult;
import edu.stanford.bmir.protege.web.shared.obo.*;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsResult;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsResult;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.project.*;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsResult;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsResult;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityHtmlRenderingResult;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberResult;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesResult;
import edu.stanford.bmir.protege.web.shared.search.GetSearchSettingsResult;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.SetSearchSettingsResult;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsResult;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsResult;
import edu.stanford.bmir.protege.web.shared.tag.*;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageResult;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountResult;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserResult;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityGraphResult;
import edu.stanford.bmir.protege.web.shared.viz.GetUserProjectEntityGraphCriteriaResult;
import edu.stanford.bmir.protege.web.shared.viz.SetEntityGraphActiveFiltersResult;
import edu.stanford.bmir.protege.web.shared.viz.SetUserProjectEntityGraphSettingsResult;
import edu.stanford.bmir.protege.web.shared.watches.GetWatchesResult;
import edu.stanford.bmir.protege.web.shared.watches.SetEntityWatchesResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
//@AutoValue
public class JsonRpcResponse {

    private final Result resultInternal;

    private final JsonRpcError errorInternal;

    @JsonCreator
    public JsonRpcResponse(@JsonProperty("result") Result resultInternal,
                           @JsonProperty("error") JsonRpcError errorInternal) {
        this.resultInternal = resultInternal;
        this.errorInternal = errorInternal;
    }

    @JsonProperty("jsonrpc")
    public final String getJsonRpc() {
        return "2.0";
    }

    @JsonProperty("result")
    @Nullable
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "method", include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
    public Result getResultInternal() {
        return resultInternal;
    }

    @JsonIgnore
    public Optional<Result> getResult() {
        return Optional.ofNullable(getResultInternal());
    }

    @JsonProperty("error")
    @Nullable
    public JsonRpcError getErrorInternal() {
        return errorInternal;
    }

    @JsonIgnore
    public Optional<JsonRpcError> getError() {
        return Optional.ofNullable(getErrorInternal());
    }

    public static JsonRpcResponse create(@JsonProperty("result") Result newResultInternal,
                                         @JsonProperty("error") JsonRpcError newErrorInternal) {
        return new JsonRpcResponse(newResultInternal, newErrorInternal);
    }


    @Override
    public String toString() {
        return "JsonRpcResponse{"
                + "resultInternal=" + resultInternal + ", "
                + "errorInternal=" + errorInternal
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof JsonRpcResponse) {
            JsonRpcResponse that = (JsonRpcResponse) o;
            return (this.resultInternal == null ? that.getResultInternal() == null : this.resultInternal.equals(that.getResultInternal()))
                    && (this.errorInternal == null ? that.getErrorInternal() == null : this.errorInternal.equals(that.getErrorInternal()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h$ = 1;
        h$ *= 1000003;
        h$ ^= (resultInternal == null) ? 0 : resultInternal.hashCode();
        h$ *= 1000003;
        h$ ^= (errorInternal == null) ? 0 : errorInternal.hashCode();
        return h$;
    }
}

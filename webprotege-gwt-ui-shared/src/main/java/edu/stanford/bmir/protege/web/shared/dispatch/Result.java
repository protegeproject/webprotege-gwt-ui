package edu.stanford.bmir.protege.web.shared.dispatch;

import com.fasterxml.jackson.annotation.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.app.*;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.bulkop.*;
import edu.stanford.bmir.protege.web.shared.change.*;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.crud.*;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.*;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.icd.*;
import edu.stanford.bmir.protege.web.shared.individuals.*;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.itemlist.*;
import edu.stanford.bmir.protege.web.shared.lang.GetProjectLangTagsResult;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.logicaldefinition.*;
import edu.stanford.bmir.protege.web.shared.mail.*;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.merge.*;
import edu.stanford.bmir.protege.web.shared.merge_add.*;
import edu.stanford.bmir.protege.web.shared.obo.*;
import edu.stanford.bmir.protege.web.shared.permissions.*;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.*;
import edu.stanford.bmir.protege.web.shared.projectsettings.*;
import edu.stanford.bmir.protege.web.shared.renderer.*;
import edu.stanford.bmir.protege.web.shared.revision.*;
import edu.stanford.bmir.protege.web.shared.search.*;
import edu.stanford.bmir.protege.web.shared.sharing.*;
import edu.stanford.bmir.protege.web.shared.tag.*;
import edu.stanford.bmir.protege.web.shared.upload.SubmitFileResult;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageResult;
import edu.stanford.bmir.protege.web.shared.user.*;
import edu.stanford.bmir.protege.web.shared.viz.*;
import edu.stanford.bmir.protege.web.shared.watches.*;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 * <p>
 * The basic interface for results which are returned from the dispatch service
 * </p>
 */
@JsonSubTypes({
        @JsonSubTypes.Type(AddEntityCommentResult.class),
        @JsonSubTypes.Type(AddProjectTagResult.class),
        @JsonSubTypes.Type(BatchResult.class),
        @JsonSubTypes.Type(CheckManchesterSyntaxFrameResult.class),
        @JsonSubTypes.Type(ChangePasswordResult.class),
        @JsonSubTypes.Type(ComputeProjectMergeResult.class),
        @JsonSubTypes.Type(CopyFormDescriptorsFromProjectResult.class),
        @JsonSubTypes.Type(CreateAnnotationPropertiesResult.class),
        @JsonSubTypes.Type(CreateClassesResult.class),
        @JsonSubTypes.Type(CreateDataPropertiesResult.class),
        @JsonSubTypes.Type(CreateNamedIndividualsResult.class),
        @JsonSubTypes.Type(CreateObjectPropertiesResult.class),
        @JsonSubTypes.Type(CreateEntityDiscussionThreadResult.class),
        @JsonSubTypes.Type(CreateEntityFromFormDataResult.class),
        @JsonSubTypes.Type(CreateNewProjectResult.class),
        @JsonSubTypes.Type(CreateUserAccountResult.class),
        @JsonSubTypes.Type(LoadProjectResult.class),
        @JsonSubTypes.Type(LogOutUserResult.class),
        @JsonSubTypes.Type(RebuildPermissionsResult.class),
        @JsonSubTypes.Type(DeleteEntitiesResult.class),
        @JsonSubTypes.Type(DeleteEntityCommentResult.class),
        @JsonSubTypes.Type(DeleteFormResult.class),
        @JsonSubTypes.Type(EditAnnotationsResult.class),
        @JsonSubTypes.Type(EditCommentResult.class),
        @JsonSubTypes.Type(ExistingOntologyMergeAddResult.class),
        @JsonSubTypes.Type(GetAllOntologiesResult.class),
        @JsonSubTypes.Type(GetApplicationSettingsResult.class),
        @JsonSubTypes.Type(GetCommentedEntitiesResult.class),
        @JsonSubTypes.Type(GetAvailableProjectsResult.class),
        @JsonSubTypes.Type(GetAvailableProjectsWithPermissionResult.class),
        @JsonSubTypes.Type(GetClassFrameResult.class),
        @JsonSubTypes.Type(GetDataPropertyFrameResult.class),
        @JsonSubTypes.Type(GetAnnotationPropertyFrameResult.class),
        @JsonSubTypes.Type(GetDataPropertyFrameResult.class),
        @JsonSubTypes.Type(GetAuthenticatedUserDetailsResult.class),
        @JsonSubTypes.Type(GetDeprecatedEntitiesResult.class),
        @JsonSubTypes.Type(GetEmailAddressResult.class),
        @JsonSubTypes.Type(GetEntityCreationFormsResult.class),
        @JsonSubTypes.Type(GetEntityCrudKitsResult.class),
        @JsonSubTypes.Type(GetEntityDeprecationFormsResult.class),
        @JsonSubTypes.Type(GetEntityDiscussionThreadsResult.class),
        @JsonSubTypes.Type(GetEntityFormDescriptorResult.class),
        @JsonSubTypes.Type(GetEntityFormsResult.class),
        @JsonSubTypes.Type(GetEntityGraphResult.class),
        @JsonSubTypes.Type(GetEntityRenderingResult.class),
        @JsonSubTypes.Type(GetEntityTagsResult.class),
        @JsonSubTypes.Type(GetHeadRevisionNumberResult.class),
        @JsonSubTypes.Type(GetHierarchyChildrenResult.class),
        @JsonSubTypes.Type(GetHierarchyParentsResult.class),
        @JsonSubTypes.Type(GetHierarchyPathsToRootResult.class),
        @JsonSubTypes.Type(GetHierarchyRootsResult.class),
        @JsonSubTypes.Type(GetHierarchySiblingsResult.class),
        @JsonSubTypes.Type(GetEntityHtmlRenderingResult.class),
        @JsonSubTypes.Type(GetIndividualsResult.class),
        @JsonSubTypes.Type(GetIndividualsPageContainingIndividualResult.class),
        @JsonSubTypes.Type(GetManchesterSyntaxFrameResult.class),
        @JsonSubTypes.Type(GetManchesterSyntaxFrameCompletionsResult.class),
        @JsonSubTypes.Type(GetMatchingEntitiesResult.class),
        @JsonSubTypes.Type(GetNamedIndividualFrameResult.class),
        @JsonSubTypes.Type(GetObjectPropertyFrameResult.class),
        @JsonSubTypes.Type(GetOboTermIdResult.class),
        @JsonSubTypes.Type(GetOboNamespacesResult.class),
        @JsonSubTypes.Type(GetOboTermCrossProductResult.class),
        @JsonSubTypes.Type(GetOboTermDefinitionResult.class),
        @JsonSubTypes.Type(GetOboTermSynonymsResult.class),
        @JsonSubTypes.Type(GetOboTermRelationshipsResult.class),
        @JsonSubTypes.Type(GetOboTermXRefsResult.class),
        @JsonSubTypes.Type(GetOntologyAnnotationsResult.class),
        @JsonSubTypes.Type(GetOntologyFramesResult.class),
        @JsonSubTypes.Type(GetPersonIdCompletionsResult.class),
        @JsonSubTypes.Type(GetPerspectiveDetailsResult.class),
        @JsonSubTypes.Type(GetPerspectiveLayoutResult.class),
        @JsonSubTypes.Type(GetPerspectivesResult.class),
        @JsonSubTypes.Type(GetPossibleItemCompletionsResult.class),
        @JsonSubTypes.Type(GetProjectChangesResult.class),
        @JsonSubTypes.Type(GetProjectDetailsResult.class),
        @JsonSubTypes.Type(GetProjectEventsResult.class),
        @JsonSubTypes.Type(GetProjectFormDescriptorsResult.class),
        @JsonSubTypes.Type(GetProjectInfoResult.class),
        @JsonSubTypes.Type(GetPerspectiveLayoutResult.class),
        @JsonSubTypes.Type(GetProjectPermissionsResult.class),
        @JsonSubTypes.Type(GetProjectPrefixDeclarationsResult.class),
        @JsonSubTypes.Type(GetProjectSettingsResult.class),
        @JsonSubTypes.Type(GetProjectSharingSettingsResult.class),
        @JsonSubTypes.Type(GetProjectTagsResult.class),
        @JsonSubTypes.Type(GetProjectLangTagsResult.class),
        @JsonSubTypes.Type(GetRevisionSummariesResult.class),
        @JsonSubTypes.Type(GetRootOntologyIdResult.class),
        @JsonSubTypes.Type(GetSearchSettingsResult.class),
        @JsonSubTypes.Type(GetUserIdCompletionsResult.class),
        @JsonSubTypes.Type(GetUserProjectEntityGraphCriteriaResult.class),
        @JsonSubTypes.Type(GetUsageResult.class),
        @JsonSubTypes.Type(GetWatchesResult.class),
        @JsonSubTypes.Type(GetWatchedEntityChangesResult.class),
        @JsonSubTypes.Type(LoadProjectResult.class),
        @JsonSubTypes.Type(LogOutUserResult.class),
        @JsonSubTypes.Type(LookupEntitiesResult.class),
        @JsonSubTypes.Type(MergeEntitiesResult.class),
        @JsonSubTypes.Type(MergeUploadedProjectResult.class),
        @JsonSubTypes.Type(MoveEntitiesToParentResult.class),
        @JsonSubTypes.Type(ChangeEntityParentsResult.class),
        @JsonSubTypes.Type(MoveHierarchyNodeResult.class),
        @JsonSubTypes.Type(MoveProjectsToTrashResult.class),
        @JsonSubTypes.Type(NewOntologyMergeAddResult.class),
        @JsonSubTypes.Type(PerformEntitySearchResult.class),
        @JsonSubTypes.Type(PerformLoginResult.class),
        @JsonSubTypes.Type(RebuildPermissionsResult.class),
        @JsonSubTypes.Type(RemoveProjectFromTrashResult.class),
        @JsonSubTypes.Type(ResetPasswordResult.class),
        @JsonSubTypes.Type(ResetPerspectivesResult.class),
        @JsonSubTypes.Type(ResetPerspectiveLayoutResult.class),
        @JsonSubTypes.Type(ResetPerspectivesResult.class),
        @JsonSubTypes.Type(SetPerspectivesResult.class),
        @JsonSubTypes.Type(RevertRevisionResult.class),
        @JsonSubTypes.Type(SetAnnotationValueResult.class),
        @JsonSubTypes.Type(SetApplicationSettingsResult.class),
        @JsonSubTypes.Type(SetDiscussionThreadStatusResult.class),
        @JsonSubTypes.Type(SetEmailAddressResult.class),
        @JsonSubTypes.Type(SetEntityCrudKitSettingsResult.class),
        @JsonSubTypes.Type(SetEntityFormDescriptorResult.class),
        @JsonSubTypes.Type(SetEntityFormDataResult.class),
        @JsonSubTypes.Type(SetEntityGraphActiveFiltersResult.class),
        @JsonSubTypes.Type(SetEntityWatchesResult.class),
        @JsonSubTypes.Type(SetManchesterSyntaxFrameResult.class),
        @JsonSubTypes.Type(SetOboTermCrossProductResult.class),
        @JsonSubTypes.Type(SetOboTermDefinitionResult.class),
        @JsonSubTypes.Type(SetOboTermIdResult.class),
        @JsonSubTypes.Type(SetOboTermRelationshipsResult.class),
        @JsonSubTypes.Type(SetOboTermSynonymsResult.class),
        @JsonSubTypes.Type(SetOboTermXRefsResult.class),
        @JsonSubTypes.Type(SetOntologyAnnotationsResult.class),
        @JsonSubTypes.Type(SetPerspectiveLayoutResult.class),
        @JsonSubTypes.Type(SetProjectFormDescriptorsResult.class),
        @JsonSubTypes.Type(SetProjectFormsResult.class),
        @JsonSubTypes.Type(SetProjectPrefixDeclarationsResult.class),
        @JsonSubTypes.Type(SetProjectSettingsResult.class),
        @JsonSubTypes.Type(SetProjectSharingSettingsResult.class),
        @JsonSubTypes.Type(SetProjectTagsResult.class),
        @JsonSubTypes.Type(SetSearchSettingsResult.class),
        @JsonSubTypes.Type(SetUserProjectEntityGraphSettingsResult.class),
        @JsonSubTypes.Type(SubmitFileResult.class),
        @JsonSubTypes.Type(UpdateEntityTagsResult.class),
        @JsonSubTypes.Type(UpdateFormDescriptorResult.class),
        @JsonSubTypes.Type(UpdateClassFrameResult.class),
        @JsonSubTypes.Type(UpdateObjectPropertyFrameResult.class),
        @JsonSubTypes.Type(UpdateDataPropertyFrameResult.class),
        @JsonSubTypes.Type(UpdateAnnotationPropertyFrameResult.class),
        @JsonSubTypes.Type(UpdateNamedIndividualFrameResult.class),
        @JsonSubTypes.Type(GetLinearizationDefinitionsResult.class),
        @JsonSubTypes.Type(GetEntityLinearizationResult.class),
        @JsonSubTypes.Type(GetUserInfoResult.class),
        @JsonSubTypes.Type(SaveEntityLinearizationResult.class),
        @JsonSubTypes.Type(GetClassAncestorsResult.class),
        @JsonSubTypes.Type(GetRenderedOwlEntitiesResult.class),
        @JsonSubTypes.Type(GetEntityPostCoordinationResult.class),
        @JsonSubTypes.Type(ProcessUploadedCustomScalesResult.class),
        @JsonSubTypes.Type(GetEntityCustomScalesResult.class),
        @JsonSubTypes.Type(SaveEntityPostCoordinationResult.class),
        @JsonSubTypes.Type(ProcessUploadedPostCoordinationResult.class),
        @JsonSubTypes.Type(GetPostCoordinationTableConfigurationResult.class),
        @JsonSubTypes.Type(GetProjectChangesForHistoryViewResult.class),
        @JsonSubTypes.Type(ProcessUploadedLinearizationResult.class),
        @JsonSubTypes.Type(GetPostcoordinationAxisToGenericScaleResult.class),
        @JsonSubTypes.Type(SaveEntityCustomScaleResult.class),
        @JsonSubTypes.Type(GetEntityLogicalDefinitionResult.class),
        @JsonSubTypes.Type(UpdateLogicalDefinitionResult.class),
        @JsonSubTypes.Type(GetUserInfoResult.class),
        @JsonSubTypes.Type(SetNamedHierarchiesResult.class),
        @JsonSubTypes.Type(CreateNewProjectFromProjectBackupResult.class),
        @JsonSubTypes.Type(MoveEntitiesToParentIcdResult.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface Result extends IsSerializable {

}

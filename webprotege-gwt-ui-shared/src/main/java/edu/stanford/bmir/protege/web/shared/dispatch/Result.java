package edu.stanford.bmir.protege.web.shared.dispatch;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.app.GetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.shared.app.SetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.shared.auth.ChangePasswordResult;
import edu.stanford.bmir.protege.web.shared.auth.PerformLoginResult;
import edu.stanford.bmir.protege.web.shared.bulkop.EditAnnotationsResult;
import edu.stanford.bmir.protege.web.shared.bulkop.MoveEntitiesToParentResult;
import edu.stanford.bmir.protege.web.shared.bulkop.SetAnnotationValueResult;
import edu.stanford.bmir.protege.web.shared.change.*;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsResult;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateAnnotationPropertiesResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateClassesResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateDataPropertiesResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateEntityFromFormDataResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateNamedIndividualsResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateObjectPropertiesResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetAuthenticatedUserDetailsResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetNamedIndividualFrameResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetRootOntologyIdResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetUserInfoResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.SetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.UpdateClassFrameResult;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.frame.CheckManchesterSyntaxFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.GetClassFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameCompletionsResult;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.GetOntologyFramesResult;
import edu.stanford.bmir.protege.web.shared.frame.SetManchesterSyntaxFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.UpdateAnnotationPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.UpdateDataPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.UpdateNamedIndividualFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.UpdateObjectPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.hierarchy.ChangeEntityParentsResult;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyChildrenResult;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyParentsResult;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyPathsToRootResult;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyRootsResult;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchySiblingsResult;
import edu.stanford.bmir.protege.web.shared.hierarchy.MoveHierarchyNodeResult;
import edu.stanford.bmir.protege.web.shared.icd.GetClassAncestorsResult;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsPageContainingIndividualResult;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsResult;
import edu.stanford.bmir.protege.web.shared.issues.AddEntityCommentResult;
import edu.stanford.bmir.protege.web.shared.issues.CreateEntityDiscussionThreadResult;
import edu.stanford.bmir.protege.web.shared.issues.DeleteEntityCommentResult;
import edu.stanford.bmir.protege.web.shared.issues.EditCommentResult;
import edu.stanford.bmir.protege.web.shared.issues.GetCommentedEntitiesResult;
import edu.stanford.bmir.protege.web.shared.issues.GetEntityDiscussionThreadsResult;
import edu.stanford.bmir.protege.web.shared.issues.SetDiscussionThreadStatusResult;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdCompletionsResult;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPossibleItemCompletionsResult;
import edu.stanford.bmir.protege.web.shared.itemlist.GetUserIdCompletionsResult;
import edu.stanford.bmir.protege.web.shared.lang.GetProjectLangTagsResult;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectResult;
import edu.stanford.bmir.protege.web.shared.merge_add.ExistingOntologyMergeAddResult;
import edu.stanford.bmir.protege.web.shared.merge_add.GetAllOntologiesResult;
import edu.stanford.bmir.protege.web.shared.merge_add.NewOntologyMergeAddResult;
import edu.stanford.bmir.protege.web.shared.obo.GetOboNamespacesResult;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermCrossProductResult;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermDefinitionResult;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermIdResult;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermRelationshipsResult;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermSynonymsResult;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermXRefsResult;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermCrossProductResult;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermDefinitionResult;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermIdResult;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermRelationshipsResult;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermSynonymsResult;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermXRefsResult;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsResult;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsResult;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectiveDetailsResult;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectiveLayoutResult;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectivesResult;
import edu.stanford.bmir.protege.web.shared.perspective.ResetPerspectiveLayoutResult;
import edu.stanford.bmir.protege.web.shared.perspective.ResetPerspectivesResult;
import edu.stanford.bmir.protege.web.shared.perspective.SetPerspectiveLayoutResult;
import edu.stanford.bmir.protege.web.shared.perspective.SetPerspectivesResult;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectResult;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsResult;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsWithPermissionResult;
import edu.stanford.bmir.protege.web.shared.project.GetProjectDetailsResult;
import edu.stanford.bmir.protege.web.shared.project.GetProjectInfoResult;
import edu.stanford.bmir.protege.web.shared.project.GetProjectPrefixDeclarationsResult;
import edu.stanford.bmir.protege.web.shared.project.LoadProjectResult;
import edu.stanford.bmir.protege.web.shared.project.MoveProjectsToTrashResult;
import edu.stanford.bmir.protege.web.shared.project.RemoveProjectFromTrashResult;
import edu.stanford.bmir.protege.web.shared.project.SetProjectPrefixDeclarationsResult;
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
import edu.stanford.bmir.protege.web.shared.tag.AddProjectTagResult;
import edu.stanford.bmir.protege.web.shared.tag.GetEntityTagsResult;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsResult;
import edu.stanford.bmir.protege.web.shared.tag.SetProjectTagsResult;
import edu.stanford.bmir.protege.web.shared.tag.UpdateEntityTagsResult;
import edu.stanford.bmir.protege.web.shared.upload.SubmitFileResult;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageResult;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountResult;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserResult;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityGraphResult;
import edu.stanford.bmir.protege.web.shared.viz.GetUserProjectEntityGraphCriteriaResult;
import edu.stanford.bmir.protege.web.shared.viz.SetEntityGraphActiveFiltersResult;
import edu.stanford.bmir.protege.web.shared.viz.SetUserProjectEntityGraphSettingsResult;
import edu.stanford.bmir.protege.web.shared.watches.GetWatchesResult;
import edu.stanford.bmir.protege.web.shared.watches.SetEntityWatchesResult;


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
        @JsonSubTypes.Type(SaveEntityCustomScaleResult.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface Result extends IsSerializable {

}

package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.DisplayContext;
import edu.stanford.bmir.protege.web.shared.ViewId;
import edu.stanford.bmir.protege.web.shared.ViewNodeId;
import edu.stanford.bmir.protege.web.shared.app.GetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.shared.app.GetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.shared.app.SetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.shared.app.SetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.bulkop.*;
import edu.stanford.bmir.protege.web.shared.change.*;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordAction;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.crud.*;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.WhiteSpaceTreatment;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidFormat;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.*;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.individuals.*;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.itemlist.*;
import edu.stanford.bmir.protege.web.shared.lang.*;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesAction;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.match.RelationshipPresence;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeAction;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectAction;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectResult;
import edu.stanford.bmir.protege.web.shared.merge_add.*;
import edu.stanford.bmir.protege.web.shared.obo.*;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsResult;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsResult;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.project.*;
import edu.stanford.bmir.protege.web.shared.projectsettings.*;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityHtmlRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityHtmlRenderingResult;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import edu.stanford.bmir.protege.web.shared.revision.*;
import edu.stanford.bmir.protege.web.shared.search.*;
import edu.stanford.bmir.protege.web.shared.sharing.*;
import edu.stanford.bmir.protege.web.shared.shortform.*;
import edu.stanford.bmir.protege.web.shared.tag.*;
import edu.stanford.bmir.protege.web.shared.upload.SubmitFileResult;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageAction;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageResult;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountAction;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountResult;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserAction;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserResult;
import edu.stanford.bmir.protege.web.shared.viz.*;
import edu.stanford.bmir.protege.web.shared.watches.GetWatchesAction;
import edu.stanford.bmir.protege.web.shared.watches.GetWatchesResult;
import edu.stanford.bmir.protege.web.shared.watches.SetEntityWatchesAction;
import edu.stanford.bmir.protege.web.shared.watches.SetEntityWatchesResult;
import edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType;
import edu.stanford.protege.gwt.graphtree.shared.DropType;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import edu.stanford.protege.widgetmap.shared.node.ParentNode;
import edu.stanford.protege.widgetmap.shared.node.TerminalNode;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplPlain;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jun 2018
 * <p>
 * Do not use this class in any client code.  It is here to whitelist objects
 * that don't get added to the serialization whitelist.
 * @noinspection unused
 */
public class RpcWhiteList implements IsSerializable, Action, Result {

    AuthenticationResponse _AUAuthenticationResponse;

    ActionExecutionResult _ActionExecutionResult;

    AddEntityCommentAction _AddEntityCommentAction;

    AddEntityCommentResult _AddEntityCommentResult;

    AddProjectTagAction _AddProjectTagAction;

    AddProjectTagResult _AddProjectTagResult;

    AnnotationPropertyFrame _AnnotationPropertyFrame;

    AvailableProject _AvailableProject;

    BatchAction _BatchAction;

    BatchResult _BatchResult;

    ChangePasswordAction _ChangePasswordAction;

    ChangePasswordResult _ChangePasswordResult;

    CheckManchesterSyntaxFrameAction _CheckManchesterSyntaxFrameAction;

    CheckManchesterSyntaxFrameResult _CheckManchesterSyntaxFrameResult;

    ChoiceDescriptor _ChoiceDescriptor;

    ChoiceDescriptorDto _ChoiceDescriptorDto;

    ChoiceListSourceDescriptor _ChoiceListSourceDescriptor;

    ClassFrame _ClassFrame;

    Color _Color;

    Comment _Comment;

    CommentId _CommentId;

    CommentedEntityData _CommentedEntityData;

    CompositeRelationshipValueCriteria _CompositeRelationshipValueCriteria;

    ComputeProjectMergeAction _ComputeProjectMergeAction;

    ComputeProjectMergeResult _ComputeProjectMergeResult;

    ConditionalIriPrefix _ConditionalIriPrefix;

    CopyFormDescriptorsFromProjectAction _CopyFormDescriptorsFromProjectAction;

    CopyFormDescriptorsFromProjectResult _CopyFormDescriptorsFromProjectResult;

    CreateAnnotationPropertiesAction _CreateAnnotationPropertiesAction;

    CreateAnnotationPropertiesResult _CreateAnnotationPropertiesResult;

    CreateClassesAction _CreateClassesAction;

    CreateClassesResult _CreateClassesResult;

    CreateDataPropertiesAction _CreateDataPropertiesAction;

    CreateDataPropertiesResult _CreateDataPropertiesResult;

    CreateEntitiesInHierarchyAction _CreateEntitiesInHierarchyAction;

    CreateEntityDiscussionThreadAction _CreateEntityDiscussionThreadAction;

    CreateEntityDiscussionThreadResult _CreateEntityDiscussionThreadResult;

    CreateEntityFromFormDataAction _CreateEntityFromFormDataAction;

    CreateEntityFromFormDataResult _CreateEntityFromFormDataResult;

    CreateNamedIndividualsAction _CreateNamedIndividualsAction;

    CreateNamedIndividualsResult _CreateNamedIndividualsResult;

    CreateNewProjectAction _CreateNewProjectAction;

    CreateNewProjectResult _CreateNewProjectResult;

    CreateObjectPropertiesAction _CreateObjectPropertiesAction;

    CreateObjectPropertiesResult _CreateObjectPropertiesResult;

    CreateUserAccountAction _CreateUserAccountAction;

    CreateUserAccountResult _CreateUserAccountResult;

    Criteria _Criteria;

    DataPropertyFrame _DataPropertyFrame;

    DeleteEntitiesAction _DeleteEntitiesAction;

    DeleteEntitiesResult _DeleteEntitiesResult;

    DeleteEntityCommentAction _DeleteEntityCommentAction;

    DeleteEntityCommentResult _DeleteEntityCommentResult;

    DeleteFormAction _DeleteFormAction;

    DeleteFormResult _DeleteFormResult;

    DeprecateEntityByFormAction _DeprecateEntityByFormAction;

    DictionaryLanguage _DictionaryLanguage;

    DictionaryLanguageData _DictionaryLanguageData;

    DictionaryLanguageUsage _DictionaryLanguageUsage;

    DiffElement<String, String> _DiffElement;

    DisplayNameSettings _DisplayNameSettings;
    DocumentId _DocumentId;
    DropType _DropType;
    EdgeCriteria _EdgeCriteria;
    EditAnnotationsAction _EditAnnotationsAction;
    EditAnnotationsResult _EditAnnotationsResult;
    EditCommentAction _EditCommentAction;
    EditCommentResult _EditCommentResult;
    EntityCrudKit _EntityCrudKit;
    EntityCrudKitPrefixSettings _EntityCrudKitPrefixSettings;
    EntityCrudKitSettings _EntityCrudKitSettings;
    EntityCrudKitSuffixSettings _EntityCrudKitSuffixSettings;
    EntityDeprecationSettings _EntityDeprecationSettings;
    EntityDiscussionThread _EntityDiscussionThread;
    EntityFormSelector _EntityFormSelector;
    EntityGraph _EntityGraph;
    EntityGraphFilter _EntityGraphFilter;
    EntityGraphSettings _EntityGraphSettings;
    EntityLookupResult _EntityLookupResult;
    EntityNameControlDescriptor _EntityNameControlDescriptor;
    EntityNameMatchResult _EntityNameMatchResult;
    EntityNameMatchType _EntityNameMatchType;
    EntitySearchResult _EntitySearchResult;
    ExistingOntologyMergeAddAction _ExistingOntologyMergeAddAction;
    ExistingOntologyMergeAddResult _ExistingOntologyMergeAddResult;
    ExpansionState _ExpansionState;
    FieldRun _FieldRun;
    FilterName _FilterName;
    FilterState _FilterState;
    FormControlData _FormControlData;
    FormControlDataDto _FormControlDataDto;
    FormControlDescriptor _FormControlDescriptor;
    FormControlDescriptorDto _FormControlDescriptorDto;
    FormDataByFormId _FormDataByFormId;
    FormDataDto _FormDataDto;
    FormDescriptor _FormDescriptor;
    FormDescriptorDto _FormDescriptorDto;
    FormEntitySubject _FormEntitySubject;
    FormEntitySubjectDto _FormEntitySubjectDto;
    FormFieldData _FormFieldData;
    FormFieldDataDto _FormFieldDataDto;
    FormFieldDeprecationStrategy _FormFieldDeprecationStrategy;
    FormFieldDescriptor _FormFieldDescriptor;
    FormFieldDescriptorDto _FormFieldDescriptorDto;
    FormId _FormId;
    FormPageRequest _FormPageRequest;
    FormPageRequest.SourceType _FormPageRequest_SourceType;
    FormPurpose _FormPurpose;
    FormRegionFilter _FormRegionFilter;
    FormRegionId _FormRegionId;
    FormRegionOrdering _FormRegionOrdering;
    FormRegionOrderingDirection _FormRegionOrderingDirection;
    FormSubjectDto _FormSubjectDto;
    GeneratedAnnotationsSettings _GeneratedAnnotationsSettings;
    GetAllOntologiesAction _GetAllOntologiesAction;
    GetAllOntologiesResult _GetAllOntologiesResult;
    GetAnnotationPropertyFrameAction _GetAnnotationPropertyFrameAction;
    GetAnnotationPropertyFrameResult _GetAnnotationPropertyFrameResult;
    GetApplicationSettingsAction _GetApplicationSettingsAction;
    GetApplicationSettingsResult _GetApplicationSettingsResult;
    GetAuthenticatedUserDetailsAction _GetAuthenticatedUserDetailsAction;
    GetAuthenticatedUserDetailsResult _GetAuthenticatedUserDetailsResult;
    GetAvailableProjectsAction _GetAvailableProjectsAction;
    GetAvailableProjectsResult _GetAvailableProjectsResult;
    GetAvailableProjectsWithPermissionAction _GetAvailableProjectsWithPermissionAction;
    GetAvailableProjectsWithPermissionResult _GetAvailableProjectsWithPermissionResult;
    GetClassFrameAction _GetClassFrameAction;
    GetClassFrameResult _GetClassFrameResult;
    GetCommentedEntitiesAction _GetCommentedEntitiesAction;
    GetCommentedEntitiesResult _GetCommentedEntitiesResult;
    GetDataPropertyFrameAction _GetDataPropertyFrameAction;
    GetDataPropertyFrameResult _GetDataPropertyFrameResult;
    GetDeprecatedEntitiesAction _GetDeprecatedEntitiesAction;
    GetDeprecatedEntitiesResult _GetDeprecatedEntitiesResult;
    GetEmailAddressAction _GetEmailAddressAction;
    GetEmailAddressResult _GetEmailAddressResult;
    GetEntityCreationFormsAction _GetEntityCreationFormsAction;
    GetEntityCreationFormsResult _GetEntityCreationFormsResult;
    GetEntityCrudKitsAction _GetEntityCrudKitsAction;
    GetEntityCrudKitsResult _GetEntityCrudKitsResult;
    GetEntityDeprecationFormsAction _GetEntityDeprecationFormsAction;
    GetEntityDeprecationFormsResult _GetEntityDeprecationFormsResult;
    GetEntityDiscussionThreadsAction _GetEntityDiscussionThreadsAction;
    GetEntityDiscussionThreadsResult _GetEntityDiscussionThreadsResult;
    GetEntityFormDescriptorAction _GetEntityFormDescriptorAction;
    GetEntityFormDescriptorResult _GetEntityFormDescriptorResult;
    GetEntityFormsAction _GetEntityFormsAction;
    GetEntityFormsResult _GetEntityFormsResult;
    GetEntityGraphAction _GetEntityGraphAction;
    GetEntityGraphResult _GetEntityGraphResult;
    GetEntityHtmlRenderingAction _GetEntityHtmlRenderingAction;
    GetEntityHtmlRenderingResult _GetEntityHtmlRenderingResult;
    GetEntityRenderingAction _GetEntityRenderingAction;
    GetEntityRenderingResult _GetEntityRenderingResult;
    GetEntityTagsAction _GetEntityTagsAction;
    GetEntityTagsResult _GetEntityTagsResult;
    GetHeadRevisionNumberAction _GetHeadRevisionNumberAction;
    GetHeadRevisionNumberResult _GetHeadRevisionNumberResult;
    GetHierarchyChildrenAction _GetHierarchyChildrenAction;
    GetHierarchyChildrenResult _GetHierarchyChildrenResult;
    GetHierarchyPathsToRootAction _GetHierarchyPathsToRootAction;
    GetHierarchyPathsToRootResult _GetHierarchyPathsToRootResult;
    GetHierarchyRootsAction _GetHierarchyRootsAction;
    GetHierarchyRootsResult _GetHierarchyRootsResult;
    GetHierarchySiblingsAction _GetHierarchySiblingsAction;
    GetHierarchySiblingsResult _GetHierarchySiblingsResult;
    GetIndividualsAction _GetIndividualsAction;
    GetIndividualsPageContainingIndividualAction _GetIndividualsPageContainingIndividualAction;
    GetIndividualsPageContainingIndividualResult _GetIndividualsPageContainingIndividualResult;
    GetIndividualsResult _GetIndividualsResult;
    GetManchesterSyntaxFrameAction _GetManchesterSyntaxFrameAction;
    GetManchesterSyntaxFrameCompletionsAction _GetManchesterSyntaxFrameCompletionsAction;
    GetManchesterSyntaxFrameCompletionsResult _GetManchesterSyntaxFrameCompletionsResult;
    GetManchesterSyntaxFrameResult _GetManchesterSyntaxFrameResult;
    GetMatchingEntitiesAction _GetMatchingEntitiesAction;
    GetMatchingEntitiesResult _GetMatchingEntitiesResult;
    GetNamedIndividualFrameAction _GetNamedIndividualFrameAction;
    GetNamedIndividualFrameResult _GetNamedIndividualFrameResult;
    GetObjectAction _GetObjectAction;
    GetObjectPropertyFrameAction _GetObjectPropertyFrameAction;
    GetObjectPropertyFrameResult _GetObjectPropertyFrameResult;
    GetOboNamespacesAction _GetOboNamespacesAction;
    GetOboNamespacesResult _GetOboNamespacesResult;
    GetOboTermCrossProductAction _GetOboTermCrossProductAction;
    GetOboTermCrossProductResult _GetOboTermCrossProductResult;
    GetOboTermDefinitionAction _GetOboTermDefinitionAction;
    GetOboTermDefinitionResult _GetOboTermDefinitionResult;
    GetOboTermIdAction _GetOboTermIdAction;
    GetOboTermIdResult _GetOboTermIdResult;
    GetOboTermRelationshipsAction _GetOboTermRelationshipsAction;
    GetOboTermRelationshipsResult _GetOboTermRelationshipsResult;
    GetOboTermSynonymsAction _GetOboTermSynonymsAction;
    GetOboTermSynonymsResult _GetOboTermSynonymsResult;
    GetOboTermXRefsAction _GetOboTermXRefsAction;
    GetOboTermXRefsResult _GetOboTermXRefsResult;
    GetOntologyAnnotationsAction _GetOntologyAnnotationsAction;
    GetOntologyAnnotationsResult _GetOntologyAnnotationsResult;
    GetOntologyFramesAction _GetOntologyFramesAction;
    GetOntologyFramesResult _GetOntologyFramesResult;
    GetPersonIdCompletionsAction _GetPersonIdCompletionsAction;
    GetPersonIdCompletionsResult _GetPersonIdCompletionsResult;
    GetPerspectiveDetailsAction _GetPerspectiveDetailsAction;
    GetPerspectiveDetailsResult _GetPerspectiveDetailsResult;
    PerspectiveDetails _PerspectiveDetails;
    GetPerspectiveLayoutAction _GetPerspectiveLayoutAction;
    GetPerspectiveLayoutResult _GetPerspectiveLayoutResult;
    GetPerspectivesAction _GetPerspectivesAction;
    GetPerspectivesResult _GetPerspectivesResult;
    GetPossibleItemCompletionsAction _GetPossibleItemCompletionsAction;
    GetPossibleItemCompletionsResult _GetPossibleItemCompletionsResult;
    GetProjectChangesAction _GetProjectChangesAction;
    GetProjectChangesResult _GetProjectChangesResult;
    GetProjectDetailsAction _GetProjectDetailsAction;
    GetProjectDetailsResult _GetProjectDetailsResult;
    GetProjectEventsAction _GetProjectEventsAction;
    GetProjectEventsResult _GetProjectEventsResult;
    GetProjectFormDescriptorsAction _GetProjectFormDescriptorsAction;
    GetProjectFormDescriptorsResult _GetProjectFormDescriptorsResult;
    GetProjectInfoAction _GetProjectInfoAction;
    GetProjectInfoResult _GetProjectInfoResult;
    GetProjectLangTagsAction _GetProjectLangTagsAction;
    GetProjectLangTagsResult _GetProjectLangTagsResult;
    GetProjectPermissionsAction _GetProjectPermissionsAction;
    GetProjectPermissionsResult _GetProjectPermissionsResult;
    GetProjectPrefixDeclarationsAction _GetProjectPrefixDeclarationsAction;
    GetProjectPrefixDeclarationsResult _GetProjectPrefixDeclarationsResult;
    GetProjectSettingsAction _GetProjectSettingsAction;
    GetProjectSettingsResult _GetProjectSettingsResult;
    GetProjectSharingSettingsAction _GetProjectSharingSettingsAction;
    GetProjectSharingSettingsResult _GetProjectSharingSettingsResult;
    GetProjectTagsAction _GetProjectTagsAction;
    GetProjectTagsResult _GetProjectTagsResult;
    GetRevisionSummariesAction _GetRevisionSummariesAction;
    GetRevisionSummariesResult _GetRevisionSummariesResult;
    GetRootOntologyIdAction _GetRootOntologyIdAction;
    GetRootOntologyIdResult _GetRootOntologyIdResult;
    GetSearchSettingsAction _GetSearchSettingsAction;
    GetSearchSettingsResult _GetSearchSettingsResult;
    GetUsageAction _GetUsageAction;
    GetUsageResult _GetUsageResult;
    GetUserIdCompletionsAction _GetUserIdCompletionsAction;
    GetUserIdCompletionsResult _GetUserIdCompletionsResult;
    GetUserInfoResult _GetUserInfoResult;
    GetUserProjectEntityGraphCriteriaAction _GetUserProjectEntityGraphCriteriaAction;
    GetUserProjectEntityGraphCriteriaResult _GetUserProjectEntityGraphCriteriaResult;
    GetWatchedEntityChangesAction _GetWatchedEntityChangesAction;
    GetWatchedEntityChangesResult _GetWatchedEntityChangesResult;
    GetWatchesAction _GetWatchesAction;
    GetWatchesResult _GetWatchesResult;
    GraphNode<EntityNode> _GraphNode;
    GridCellData _GridCellData;
    GridCellDataDto _GridCellDataDto;
    GridColumnDescriptor _GridColumnDescriptor;
    GridColumnDescriptorDto _GridColumnDescriptorDto;
    GridControlData _GridControlData;
    GridControlDataDto _GridControlDataDto;
    GridControlDescriptor _GridControlDescriptor;
    GridRowData _GridRowData;
    GridRowDataDto _GridRowDataDto;
    HierarchyDescriptor _HierarchyDescriptor;
    HierarchyFilterType _HierarchyFilterType;
    HierarchyPositionCriteria _HierarchyPositionCriteria;
    IRI _IRI;
    IRIData _IRIData;
    ImageControlDescriptor _ImageControlDescriptor;
    ImmutableSetMultimap _ImmutableSetMultimap;
    InstanceRetrievalMode _InstanceRetrievalMode;
    InternalServerError _InternalServerError;
    IsAEdge _IsAEdge;
    LangTag _LangTag;
    LangTagFilter _LangTagFilter;
    LanguageMap _LanguageMap;
    LoadProjectAction _LoadProjectAction;
    LoadProjectResult _LoadProjectResult;
    LogOutUserAction _LogOutUserAction;
    LogOutUserResult _LogOutUserResult;
    LookupEntitiesAction _LookupEntitiesAction;
    LookupEntitiesResult _LookupEntitiesResult;
    MergeEntitiesAction _MergeEntitiesAction;
    MergeEntitiesResult _MergeEntitiesResult;
    MergeUploadedProjectAction _MergeUploadedProjectAction;
    MergeUploadedProjectResult _MergeUploadedProjectResult;
    MoveEntitiesToParentAction _MoveEntitiesToParentAction;
    MoveEntitiesToParentResult _MoveEntitiesToParentResult;
    MoveHierarchyNodeAction _MoveHierarchyNodeAction;
    MoveHierarchyNodeResult _MoveHierarchyNodeResult;
    MoveProjectsToTrashAction _MoveProjectsToTrashAction;
    MoveProjectsToTrashResult _MoveProjectsToTrashResult;
    MultiChoiceControlDescriptor _MultiChoiceControlDescriptor;
    MultiMatchType _MultiMatchType;
    NamedIndividualFrame _NamedIndividualFrame;
    NewOntologyMergeAddAction _NewOntologyMergeAddAction;
    NewOntologyMergeAddResult _NewOntologyMergeAddResult;
    NumberControlDescriptor _NumberControlDescriptor;
    OWLAnnotationPropertyData _OWLAnnotationPropertyData;
    OWLClassData _OWLClassData;
    OWLDataPropertyData _OWLDataPropertyData;
    OWLDatatypeData _OWLDatatypeData;
    OWLLiteral _OWLLiteral;
    OWLLiteralData _OWLLiteralData;
    OWLLiteralImplPlain _OWLLiteralImplPlain;
    OWLNamedIndividualData _OWLNamedIndividualData;
    OWLPrimitiveData _OWLPrimitiveData;
    ObjectPropertyCharacteristic _ObjectPropertyCharacteristic;
    ObjectPropertyFrame _ObjectPropertyFrame;
    Operation _Operation;
    Optionality _Optionality;
    OwlBinding _OwlBinding;
    OwlClassBinding _OwlClassBinding;
    OWLObjectPropertyData _OwlObjectPropertyData;
    OwlPropertyBinding _OwlPropertyBinding;
    OwlSubClassBinding _OwlSubClassBinding;
    ParentNode _ParentNode;
    Password _Password;
    Path<EntityNode> _Path;
    PerformEntitySearchAction _PerformEntitySearchAction;
    PerformEntitySearchResult _PerformEntitySearchResult;
    PerformLoginAction _PerformLoginAction;
    PerformLoginResult _PerformLoginResult;
    PerspectiveDescriptor _PerspectiveDescriptor;
    PerspectiveLayout _PerspectiveLayout;
    PlainAnnotationPropertyFrame _PlainAnnotationPropertyFrame;
    PlainClassFrame _PlainClassFrame;
    PlainDataPropertyFrame _PlainDataPropertyFrame;
    PlainNamedIndividualFrame _PlainNamedIndividualFrame;
    PlainObjectPropertyFrame _PlainObjectPropertyFrame;
    PlainPropertyValue _PlainPropertyValue;
    PrefixNameMatchType _PrefixNameMatchType;
    PrimitiveFormControlData _PrimitiveFormControlData;
    PrimitiveFormControlDataDto _PrimitiveFormControlDataDto;
    ProjectAction _ProjectAction;
    ProjectChange _ProjectChange;
    ProjectDetails _ProjectDetails;
    ProjectSettings _ProjectSettings;
    ProjectUserEntityGraphSettings _ProjectUserEntityGraphSettings;
    ProjectWebhookEventType _ProjectWebhookEventType;
    PropertyValue _PropertyValue;
    PropertyValueDescriptor _PropertyValueDescriptor;
    RebuildPermissionsAction _RebuildPermissionsAction;
    RebuildPermissionsResult _RebuildPermissionsResult;
    RelationshipEdge _RelationshipEdge;
    RelationshipPresence _RelationshipPresence;
    RemoveProjectFromTrashAction _RemoveProjectFromTrashAction;
    RemoveProjectFromTrashResult _RemoveProjectFromTrashResult;
    Repeatability _Repeatability;
    ResetPasswordAction _ResetPasswordAction;
    ResetPasswordResult _ResetPasswordResult;
    ResetPerspectiveLayoutAction _ResetPerspectiveLayoutAction;
    ResetPerspectiveLayoutResult _ResetPerspectiveLayoutResult;
    ResetPerspectivesAction _ResetPerspectivesAction;
    ResetPerspectivesResult _ResetPerspectivesResult;
    RevertRevisionAction _RevertRevisionAction;
    RevertRevisionResult _RevertRevisionResult;
    RevisionNumber _RevisionNumber;
    RpcWhiteList _RpcWhiteList;
    SafeHtml _SafeHtml;
    SearchResultMatch _SearchResultMatch;
    SearchResultMatchPosition _SearchResultMatchPosition;
    SetAnnotationValueAction _SetAnnotationValueAction;
    SetAnnotationValueResult _SetAnnotationValueResult;
    SetApplicationSettingsAction _SetApplicationSettingsAction;
    SetApplicationSettingsResult _SetApplicationSettingsResult;
    SetDiscussionThreadStatusAction _SetDiscussionThreadStatusAction;
    SetDiscussionThreadStatusResult _SetDiscussionThreadStatusResult;
    SetEmailAddressAction _SetEmailAddressAction;
    SetEmailAddressResult _SetEmailAddressResult;
    SetEntityCrudKitSettingsAction _SetEntityCrudKitSettingsAction;
    SetEntityCrudKitSettingsResult _SetEntityCrudKitSettingsResult;
    SetEntityFormDataResult _SetEntityFormDataResult;
    SetEntityFormDescriptorAction _SetEntityFormDescriptorAction;
    SetEntityFormDescriptorResult _SetEntityFormDescriptorResult;
    SetEntityFormsDataAction _SetEntityFormsDataAction;
    SetEntityGraphActiveFiltersAction _SetEntityGraphActiveFiltersAction;
    SetEntityGraphActiveFiltersResult _SetEntityGraphActiveFiltersResult;
    SetEntityWatchesAction _SetEntityWatchesAction;
    SetEntityWatchesResult _SetEntityWatchesResult;
    SetManchesterSyntaxFrameAction _SetManchesterSyntaxFrameAction;
    SetManchesterSyntaxFrameResult _SetManchesterSyntaxFrameResult;
    SetOboTermCrossProductAction _SetOboTermCrossProductAction;
    SetOboTermCrossProductResult _SetOboTermCrossProductResult;
    SetOboTermDefinitionAction _SetOboTermDefinitionAction;
    SetOboTermDefinitionResult _SetOboTermDefinitionResult;
    SetOboTermIdAction _SetOboTermIdAction;
    SetOboTermIdResult _SetOboTermIdResult;
    SetOboTermRelationshipsAction _SetOboTermRelationshipsAction;
    SetOboTermRelationshipsResult _SetOboTermRelationshipsResult;
    SetOboTermSynonymsAction _SetOboTermSynonymsAction;
    SetOboTermSynonymsResult _SetOboTermSynonymsResult;
    SetOboTermXRefsAction _SetOboTermXRefsAction;
    SetOboTermXRefsResult _SetOboTermXRefsResult;
    SetOntologyAnnotationsAction _SetOntologyAnnotationsAction;
    SetOntologyAnnotationsResult _SetOntologyAnnotationsResult;
    SetPerspectiveLayoutAction _SetPerspectiveLayoutAction;
    SetPerspectiveLayoutResult _SetPerspectiveLayoutResult;
    SetPerspectivesAction _SetPerspectivesAction;
    SetPerspectivesResult _SetPerspectivesResult;
    SetProjectFormDescriptorsAction _SetProjectFormDescriptorsAction;
    SetProjectFormDescriptorsResult _SetProjectFormDescriptorsResult;
    SetProjectPrefixDeclarationsAction _SetProjectPrefixDeclarationsAction;

    SaveEntityChildReorderingAction _SaveEntityChildReorderingAction;
    SaveEntityChildReorderingResult _SaveEntityChildReorderingResult;
    SetProjectPrefixDeclarationsResult _SetProjectPrefixDeclarationsResult;
    SetProjectSettingsAction _SetProjectSettingsAction;
    SetProjectSettingsResult _SetProjectSettingsResult;
    SetProjectSharingSettingsAction _SetProjectSharingSettingsAction;
    SetProjectSharingSettingsResult _SetProjectSharingSettingsResult;
    SetProjectTagsAction _SetProjectTagsAction;
    SetProjectTagsResult _SetProjectTagsResult;
    SetSearchSettingsAction _SetSearchSettingsAction;
    SetSearchSettingsResult _SetSearchSettingsResult;
    SetUserProjectEntityGraphSettingsAction _SetUserProjectEntityGraphSettingsAction;
    SetUserProjectEntityGraphSettingsResult _SetUserProjectEntityGraphSettingsResult;
    ShortForm _ShortForm;
    SingleChoiceControlDescriptor _SingleChoiceControlDescriptor;
    SingleChoiceControlType _SingleChoiceControlType;
    SlackIntegrationSettings _SlackIntegrationSettings;
    SortingKey _SortingKey;
    State _State;
    Status _Status;
    SubFormControlDescriptor _SubFormControlDescriptor;
    SubmitFileResult _SubmitFileResult;
    Tag _Tag;
    TagData _TagData;
    TerminalNode _TerminalNode;
    TextControlDescriptor _TextControlDescriptor;
    UpdateAnnotationPropertyFrameAction _UpdateAnnotationPropertyFrameAction;
    UpdateAnnotationPropertyFrameResult _UpdateAnnotationPropertyFrameResult;
    UpdateClassFrameAction _UpdateClassFrameAction;
    UpdateClassFrameResult _UpdateClassFrameResult;
    UpdateDataPropertyFrameAction _UpdateDataPropertyFrameAction;
    UpdateDataPropertyFrameResult _UpdateDataPropertyFrameResult;
    UpdateEntityTagsAction _UpdateEntityTagsAction;
    UpdateEntityTagsResult _UpdateEntityTagsResult;
    UpdateFormDescriptorAction _UpdateFormDescriptorAction;
    UpdateFormDescriptorResult _UpdateFormDescriptorResult;
    UpdateFrameAction _UpdateFrameAction;
    UpdateNamedIndividualFrameAction _UpdateNamedIndividualFrameAction;
    UpdateNamedIndividualFrameResult _UpdateNamedIndividualFrameResult;
    UpdateObjectAction _UpdateObjectAction;
    UpdateObjectPropertyFrameAction _UpdateObjectPropertyFrameAction;
    UpdateObjectPropertyFrameResult _UpdateObjectPropertyFrameResult;
    UpdateObjectResult _UpdateObjectResult;
    UuidFormat _UuidFormat;
    WebProtegeEvent _WebProtegeEvent;
    WebhookSetting _WebhookSetting;
    WebhookSettings _WebhookSettings;
    WhiteSpaceTreatment _WhiteSpaceTreatment;
    OwlBinding get_OwlBinding;
    PlainPropertyValue get_PlainPropertyValue;
    PrimitiveFormControlData primitiveFormControlData;
    SetNamedHierarchiesAction _SetNamedHierarchiesAction;
    SetNamedHierarchiesResult _SetNamedHierarchiesResult;
    GetHierarchyDescriptorAction _GetHierarchyDescriptorAction;
    GetHierarchyDescriptorResult _GetHierarchyDescriptorResult;
    DisplayContext _DisplayContext;
    ViewId _ViewId;
    ViewNodeId _ViewNodeId;
    ProcessUploadedSiblingsOrderingAction _ProcessUploadedSiblingsOrderingAction;
    ProcessUploadedSiblingsOrderingResult _ProcessUploadedSiblingsOrderingResult;

    public RpcWhiteList() {
    }
}

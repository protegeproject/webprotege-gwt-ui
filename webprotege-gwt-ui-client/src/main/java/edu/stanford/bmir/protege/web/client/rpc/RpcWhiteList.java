package edu.stanford.bmir.protege.web.client.rpc;

import com.google.common.collect.ImmutableSetMultimap;
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
import edu.stanford.bmir.protege.web.shared.crud.supplied.WhiteSpaceTreatment;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidFormat;
import edu.stanford.bmir.protege.web.shared.dispatch.*;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.*;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
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
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberAction;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberResult;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesAction;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesResult;
import edu.stanford.bmir.protege.web.shared.search.*;
import edu.stanford.bmir.protege.web.shared.sharing.*;
import edu.stanford.bmir.protege.web.shared.shortform.*;
import edu.stanford.bmir.protege.web.shared.tag.*;
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
import edu.stanford.protege.widgetmap.shared.node.*;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplPlain;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jun 2018
 *
 * Do not use this class in any client code.  It is here to whitelist objects
 * that don't get added to the serialization whitelist.
 */
public class RpcWhiteList implements Action, Result {

    public LangTag get(LangTag in) { return null; }    public LangTagFilter get(LangTagFilter in) { return null; }    public FormRegionFilter get(FormRegionFilter in) { return null; }
    
    public FormPageRequest.SourceType getSource(FormPageRequest.SourceType in) { return null; }
    public PrimitiveFormControlDataDto get(PrimitiveFormControlDataDto in) { return null; }
    public FormControlDataDto get(FormControlDataDto in) { return null; }
    public FormDataDto get(FormDataDto in) { return null; }
    public FormFieldDataDto get(FormFieldDataDto in) { return null; }
    public FormSubjectDto get(FormSubjectDto in) { return null; }
    public FormEntitySubjectDto get(FormEntitySubjectDto in) { return null; }

    public GridControlDataDto get(GridControlDataDto in) { return null; }

    public GridRowDataDto get(GridRowDataDto in) { return null; }

    public GridCellDataDto get(GridCellDataDto in) { return null; }

    public ChoiceDescriptorDto get(ChoiceDescriptorDto in) { return null; }

    public FormRegionOrdering get(FormRegionOrdering in) { return null; }


    public FormRegionOrderingDirection get(FormRegionOrderingDirection in) { return null; }

    public ExpansionState get(ExpansionState in) { return null; }

    public FormDescriptorDto get(FormDescriptorDto in) { return null; }

    public FormFieldDescriptorDto get(FormFieldDescriptorDto in) { return null; }

    public FormControlDescriptorDto get(FormControlDescriptorDto in) { return null; }

    public GridColumnDescriptorDto get(GridColumnDescriptorDto in) { return null; }

    public OwlSubClassBinding get(OwlSubClassBinding in) { return null; }
    public FormPageRequest get(FormPageRequest in) { return null; }    public HierarchyPositionCriteria get(HierarchyPositionCriteria in) { return null; }    public ConditionalIriPrefix get(ConditionalIriPrefix in) { return null; }
    public EntityCrudKitSettings get(EntityCrudKitSettings in) { return null; }
    public EntityCrudKitSuffixSettings get(EntityCrudKitSuffixSettings in) { return null; }

    public EntityCrudKitPrefixSettings get(EntityCrudKitPrefixSettings in) { return null; }
    public PlainPropertyValue get(PlainPropertyValue in) { return null; }

    public UuidFormat get(UuidFormat in) { return null; }    public EntityFormSelector get(EntityFormSelector in) { return null; }

    public ChoiceListSourceDescriptor get(ChoiceListSourceDescriptor in) { return null; }

    public FormEntitySubject get(FormEntitySubject in) { return null; }
    public MultiChoiceControlDescriptor get(MultiChoiceControlDescriptor in) { return null; }    public RpcWhiteList() {
    }    public ActionExecutionResult get(ActionExecutionResult in) { return null; }

    public AvailableProject get(AvailableProject in) { return null; }

    public ChoiceDescriptor get(ChoiceDescriptor in) { return null; }

    public SingleChoiceControlDescriptor get(SingleChoiceControlDescriptor in) { return null; }

    public Color get(Color in) { return null; }

    public GridColumnId get(GridColumnId in) { return null; }

    public Criteria get(Criteria in) { return null; }

    public DictionaryLanguage get(DictionaryLanguage in) { return null; }

    public DictionaryLanguageData get(DictionaryLanguageData in) { return null; }

    public DictionaryLanguageUsage get(DictionaryLanguageUsage in) { return null; }

    public DisplayNameSettings get(DisplayNameSettings in) { return null; }

    public EdgeCriteria get(EdgeCriteria in) { return null; }

    public EntityGraph get(EntityGraph in) { return null; }

    public EntityGraphFilter get(EntityGraphFilter in) { return null; }

    public EntityGraphSettings get(EntityGraphSettings in) { return null; }

    public EntityNameControlDescriptor get(EntityNameControlDescriptor in) { return null; }

    public EntityNameMatchResult get(EntityNameMatchResult in) { return null; }

    public EntityNameMatchType get(EntityNameMatchType in) { return null; }

    public FieldRun get(FieldRun in) { return null; }

    public FilterName get(FilterName in) { return null; }

    public FormControlData get(FormControlData in) { return null; }

    public FormFieldData get(FormFieldData in) { return null; }

    public FormFieldId get(FormFieldId in) { return null; }

    public GridCellData get(GridCellData in) { return null; }

    public GridColumnDescriptor get(GridColumnDescriptor in) { return null; }

    public GridControlData get(GridControlData in) { return null; }

    public GridControlDescriptor get(GridControlDescriptor in) { return null; }

    public GridRowData get(GridRowData in) { return null; }

    public HierarchyFilterType get(HierarchyFilterType in) { return null; }

    public ImageControlDescriptor get(ImageControlDescriptor in) { return null; }

    public ImmutableSetMultimap get(ImmutableSetMultimap in) { return null; }

    public InstanceRetrievalMode get(InstanceRetrievalMode in) { return null; }

    public IRI get(IRI in) { return null; }

    public IsAEdge get(IsAEdge in) { return null; }

    public LanguageMap get(LanguageMap in) { return null; }

    public OWLLiteral get(OWLLiteral in) { return null; }

    public OWLLiteralImplPlain get(OWLLiteralImplPlain in) { return null; }

    public MultiMatchType get(MultiMatchType in) { return null; }

    public NumberControlDescriptor get(NumberControlDescriptor in) { return null; }

    public ObjectPropertyCharacteristic get(ObjectPropertyCharacteristic in) { return null; }

    public Operation get(Operation in) { return null; }

    public Optionality get(Optionality in) { return null; }

    public OwlBinding get(OwlBinding in) { return null; }

    public OwlClassBinding get(OwlClassBinding in) { return null; }

    public OwlPropertyBinding get(OwlPropertyBinding in) { return null; }

    public PrefixNameMatchType get(PrefixNameMatchType in) { return null; }

    public OWLPrimitiveData get(OWLPrimitiveData in) { return null; }

    public ProjectDetails get(ProjectDetails in) { return null; }

    public ProjectSettings get(ProjectSettings in) { return null; }

    public ProjectUserEntityGraphSettings get(ProjectUserEntityGraphSettings in) { return null; }

    public ProjectWebhookEventType get(ProjectWebhookEventType in) { return null; }

    public PropertyValue get(PropertyValue in) { return null; }

    public PropertyValueDescriptor get(PropertyValueDescriptor in) { return null; }

    public RelationshipEdge get(RelationshipEdge in) { return null; }

    public RelationshipPresence get(RelationshipPresence in) { return null; }

    public PrimitiveFormControlData get(PrimitiveFormControlData in) { return null; }    private PrimitiveFormControlData primitiveFormControlData;

    public Repeatability get(Repeatability in) { return null; }

    public SingleChoiceControlType get(SingleChoiceControlType in) { return null; }

    public SlackIntegrationSettings get(SlackIntegrationSettings in) { return null; }

    public State get(State in) { return null; }
    public SubFormControlDescriptor get(SubFormControlDescriptor in) { return null; }

    public Tag get(Tag in) { return null; }

    public TextControlDescriptor get(TextControlDescriptor in) { return null; }

    public WebhookSetting get(WebhookSetting in) { return null; }

    public WebhookSettings get(WebhookSettings in) { return null; }


    public CompositeRelationshipValueCriteria get(CompositeRelationshipValueCriteria in) { return null; }

    public WhiteSpaceTreatment get(WhiteSpaceTreatment in) { return null; }

    AddEntityCommentAction get(AddEntityCommentAction action) { return null;}
    AddProjectTagAction get(AddProjectTagAction action) { return null;}
    AuthenticateUserAction get(AuthenticateUserAction action) { return null;}
    BatchAction get(BatchAction action) { return null;}
    ChangePasswordAction get(ChangePasswordAction action) { return null;}
    CheckManchesterSyntaxFrameAction get(CheckManchesterSyntaxFrameAction action) { return null;}
    ComputeProjectMergeAction get(ComputeProjectMergeAction action) { return null;}
    CopyFormDescriptorsFromProjectAction get(CopyFormDescriptorsFromProjectAction action) { return null;}
    CreateAnnotationPropertiesAction get(CreateAnnotationPropertiesAction action) { return null;}
    CreateClassesAction get(CreateClassesAction action) { return null;}
    CreateDataPropertiesAction get(CreateDataPropertiesAction action) { return null;}
    CreateEntitiesInHierarchyAction get(CreateEntitiesInHierarchyAction action) { return null;}
    CreateEntityDiscussionThreadAction get(CreateEntityDiscussionThreadAction action) { return null;}
    CreateEntityFromFormDataAction get(CreateEntityFromFormDataAction action) { return null;}
    CreateNamedIndividualsAction get(CreateNamedIndividualsAction action) { return null;}
    CreateNewProjectAction get(CreateNewProjectAction action) { return null;}
    CreateObjectPropertiesAction get(CreateObjectPropertiesAction action) { return null;}
    CreateUserAccountAction get(CreateUserAccountAction action) { return null;}
    DeleteEntitiesAction get(DeleteEntitiesAction action) { return null;}
    DeleteEntityCommentAction get(DeleteEntityCommentAction action) { return null;}
    DeleteFormAction get(DeleteFormAction action) { return null;}
    DeprecateEntityByFormAction get(DeprecateEntityByFormAction action) { return null;}
    EditAnnotationsAction get(EditAnnotationsAction action) { return null;}
    EditCommentAction get(EditCommentAction action) { return null;}
    ExistingOntologyMergeAddAction get(ExistingOntologyMergeAddAction action) { return null;}
    GetAllOntologiesAction get(GetAllOntologiesAction action) { return null;}
    GetAnnotationPropertyFrameAction get(GetAnnotationPropertyFrameAction action) { return null;}
    GetApplicationSettingsAction get(GetApplicationSettingsAction action) { return null;}
    GetAvailableProjectsAction get(GetAvailableProjectsAction action) { return null;}
    GetAvailableProjectsWithPermissionAction get(GetAvailableProjectsWithPermissionAction action) { return null;}
    GetChapSessionAction get(GetChapSessionAction action) { return null;}
    GetClassFrameAction get(GetClassFrameAction action) { return null;}
    GetCommentedEntitiesAction get(GetCommentedEntitiesAction action) { return null;}
    GetCurrentUserInSessionAction get(GetCurrentUserInSessionAction action) { return null;}
    GetDataPropertyFrameAction get(GetDataPropertyFrameAction action) { return null;}
    GetDeprecatedEntitiesAction get(GetDeprecatedEntitiesAction action) { return null;}
    GetEmailAddressAction get(GetEmailAddressAction action) { return null;}
    GetEntityCreationFormsAction get(GetEntityCreationFormsAction action) { return null;}
    GetEntityCrudKitsAction get(GetEntityCrudKitsAction action) { return null;}
    GetEntityDeprecationFormsAction get(GetEntityDeprecationFormsAction action) { return null;}
    GetEntityDiscussionThreadsAction get(GetEntityDiscussionThreadsAction action) { return null;}
    GetEntityFormDescriptorAction get(GetEntityFormDescriptorAction action) { return null;}
    GetEntityFormsAction get(GetEntityFormsAction action) { return null;}
    GetEntityGraphAction get(GetEntityGraphAction action) { return null;}
    GetEntityHtmlRenderingAction get(GetEntityHtmlRenderingAction action) { return null;}
    GetEntityRenderingAction get(GetEntityRenderingAction action) { return null;}
    GetEntityTagsAction get(GetEntityTagsAction action) { return null;}
    GetHeadRevisionNumberAction get(GetHeadRevisionNumberAction action) { return null;}
    GetHierarchyChildrenAction get(GetHierarchyChildrenAction action) { return null;}
    GetHierarchyPathsToRootAction get(GetHierarchyPathsToRootAction action) { return null;}
    GetHierarchyRootsAction get(GetHierarchyRootsAction action) { return null;}
    GetHierarchySiblingsAction get(GetHierarchySiblingsAction action) { return null;}
    GetIndividualsAction get(GetIndividualsAction action) { return null;}
    GetIndividualsPageContainingIndividualAction get(GetIndividualsPageContainingIndividualAction action) { return null;}
    GetManchesterSyntaxFrameAction get(GetManchesterSyntaxFrameAction action) { return null;}
    GetManchesterSyntaxFrameCompletionsAction get(GetManchesterSyntaxFrameCompletionsAction action) { return null;}
    GetMatchingEntitiesAction get(GetMatchingEntitiesAction action) { return null;}
    GetNamedIndividualFrameAction get(GetNamedIndividualFrameAction action) { return null;}
    GetObjectAction get(GetObjectAction action) { return null;}
    GetObjectPropertyFrameAction get(GetObjectPropertyFrameAction action) { return null;}
    GetOboNamespacesAction get(GetOboNamespacesAction action) { return null;}
    GetOboTermCrossProductAction get(GetOboTermCrossProductAction action) { return null;}
    GetOboTermDefinitionAction get(GetOboTermDefinitionAction action) { return null;}
    GetOboTermIdAction get(GetOboTermIdAction action) { return null;}
    GetOboTermRelationshipsAction get(GetOboTermRelationshipsAction action) { return null;}
    GetOboTermSynonymsAction get(GetOboTermSynonymsAction action) { return null;}
    GetOboTermXRefsAction get(GetOboTermXRefsAction action) { return null;}
    GetOntologyAnnotationsAction get(GetOntologyAnnotationsAction action) { return null;}
    GetOntologyFramesAction get(GetOntologyFramesAction action) { return null;}
    GetPersonIdCompletionsAction get(GetPersonIdCompletionsAction action) { return null;}
    GetPerspectiveDetailsAction get(GetPerspectiveDetailsAction action) { return null;}
    GetPerspectiveLayoutAction get(GetPerspectiveLayoutAction action) { return null;}
    GetPerspectivesAction get(GetPerspectivesAction action) { return null;}
    GetPossibleItemCompletionsAction get(GetPossibleItemCompletionsAction action) { return null;}
    GetProjectChangesAction get(GetProjectChangesAction action) { return null;}
    GetProjectDetailsAction get(GetProjectDetailsAction action) { return null;}
    GetProjectEventsAction get(GetProjectEventsAction action) { return null;}
    GetProjectFormDescriptorsAction get(GetProjectFormDescriptorsAction action) { return null;}
    GetProjectInfoAction get(GetProjectInfoAction action) { return null;}
    GetProjectLangTagsAction get(GetProjectLangTagsAction action) { return null;}
    GetProjectPermissionsAction get(GetProjectPermissionsAction action) { return null;}
    GetProjectPrefixDeclarationsAction get(GetProjectPrefixDeclarationsAction action) { return null;}
    GetProjectSettingsAction get(GetProjectSettingsAction action) { return null;}
    GetProjectSharingSettingsAction get(GetProjectSharingSettingsAction action) { return null;}
    GetProjectTagsAction get(GetProjectTagsAction action) { return null;}
    GetRevisionSummariesAction get(GetRevisionSummariesAction action) { return null;}
    GetRootOntologyIdAction get(GetRootOntologyIdAction action) { return null;}
    GetSearchSettingsAction get(GetSearchSettingsAction action) { return null;}
    GetUsageAction get(GetUsageAction action) { return null;}
    GetUserIdCompletionsAction get(GetUserIdCompletionsAction action) { return null;}
    GetUserProjectEntityGraphCriteriaAction get(GetUserProjectEntityGraphCriteriaAction action) { return null;}
    GetWatchedEntityChangesAction get(GetWatchedEntityChangesAction action) { return null;}
    GetWatchesAction get(GetWatchesAction action) { return null;}
    LoadProjectAction get(LoadProjectAction action) { return null;}
    LogOutUserAction get(LogOutUserAction action) { return null;}
    LookupEntitiesAction get(LookupEntitiesAction action) { return null;}
    MergeEntitiesAction get(MergeEntitiesAction action) { return null;}
    MergeUploadedProjectAction get(MergeUploadedProjectAction action) { return null;}
    MoveEntitiesToParentAction get(MoveEntitiesToParentAction action) { return null;}
    MoveHierarchyNodeAction get(MoveHierarchyNodeAction action) { return null;}
    MoveProjectsToTrashAction get(MoveProjectsToTrashAction action) { return null;}
    NewOntologyMergeAddAction get(NewOntologyMergeAddAction action) { return null;}
    PerformEntitySearchAction get(PerformEntitySearchAction action) { return null;}
    PerformLoginAction get(PerformLoginAction action) { return null;}
    ProjectAction get(ProjectAction action) { return null;}
    RebuildPermissionsAction get(RebuildPermissionsAction action) { return null;}
    RemoveProjectFromTrashAction get(RemoveProjectFromTrashAction action) { return null;}
    ResetPasswordAction get(ResetPasswordAction action) { return null;}
    ResetPerspectiveLayoutAction get(ResetPerspectiveLayoutAction action) { return null;}
    ResetPerspectivesAction get(ResetPerspectivesAction action) { return null;}
    RevertRevisionAction get(RevertRevisionAction action) { return null;}
    RpcWhiteList get(RpcWhiteList action) { return null;}
    SetAnnotationValueAction get(SetAnnotationValueAction action) { return null;}
    SetApplicationSettingsAction get(SetApplicationSettingsAction action) { return null;}
    SetDiscussionThreadStatusAction get(SetDiscussionThreadStatusAction action) { return null;}
    SetEmailAddressAction get(SetEmailAddressAction action) { return null;}
    SetEntityCrudKitSettingsAction get(SetEntityCrudKitSettingsAction action) { return null;}
    SetEntityFormDescriptorAction get(SetEntityFormDescriptorAction action) { return null;}
    SetEntityFormsDataAction get(SetEntityFormsDataAction action) { return null;}
    SetEntityGraphActiveFiltersAction get(SetEntityGraphActiveFiltersAction action) { return null;}
    SetEntityWatchesAction get(SetEntityWatchesAction action) { return null;}
    SetManchesterSyntaxFrameAction get(SetManchesterSyntaxFrameAction action) { return null;}
    SetOboTermCrossProductAction get(SetOboTermCrossProductAction action) { return null;}
    SetOboTermDefinitionAction get(SetOboTermDefinitionAction action) { return null;}
    SetOboTermIdAction get(SetOboTermIdAction action) { return null;}
    SetOboTermRelationshipsAction get(SetOboTermRelationshipsAction action) { return null;}
    SetOboTermSynonymsAction get(SetOboTermSynonymsAction action) { return null;}
    SetOboTermXRefsAction get(SetOboTermXRefsAction action) { return null;}
    SetOntologyAnnotationsAction get(SetOntologyAnnotationsAction action) { return null;}
    SetPerspectiveLayoutAction get(SetPerspectiveLayoutAction action) { return null;}
    SetPerspectivesAction get(SetPerspectivesAction action) { return null;}
    SetProjectFormDescriptorsAction get(SetProjectFormDescriptorsAction action) { return null;}
    SetProjectPrefixDeclarationsAction get(SetProjectPrefixDeclarationsAction action) { return null;}
    SetProjectSettingsAction get(SetProjectSettingsAction action) { return null;}
    SetProjectSharingSettingsAction get(SetProjectSharingSettingsAction action) { return null;}
    SetProjectTagsAction get(SetProjectTagsAction action) { return null;}
    SetSearchSettingsAction get(SetSearchSettingsAction action) { return null;}
    SetUserProjectEntityGraphSettingsAction get(SetUserProjectEntityGraphSettingsAction action) { return null;}
    UpdateAnnotationPropertyFrameAction get(UpdateAnnotationPropertyFrameAction action) { return null;}
    UpdateClassFrameAction get(UpdateClassFrameAction action) { return null;}
    UpdateDataPropertyFrameAction get(UpdateDataPropertyFrameAction action) { return null;}
    UpdateEntityTagsAction get(UpdateEntityTagsAction action) { return null;}
    UpdateFormDescriptorAction get(UpdateFormDescriptorAction action) { return null;}
    UpdateFrameAction get(UpdateFrameAction action) { return null;}
    UpdateNamedIndividualFrameAction get(UpdateNamedIndividualFrameAction action) { return null;}
    UpdateObjectAction get(UpdateObjectAction action) { return null;}
    UpdateObjectPropertyFrameAction get(UpdateObjectPropertyFrameAction action) { return null;}    AddEntityCommentResult get(AddEntityCommentResult result) { return null; }
    AddProjectTagResult get(AddProjectTagResult result) { return null; }
    AuthenticateUserResult get(AuthenticateUserResult result) { return null; }
    BatchResult get(BatchResult result) { return null; }
    ChangePasswordResult get(ChangePasswordResult result) { return null; }
    CheckManchesterSyntaxFrameResult get(CheckManchesterSyntaxFrameResult result) { return null; }
    ComputeProjectMergeResult get(ComputeProjectMergeResult result) { return null; }
    CopyFormDescriptorsFromProjectResult get(CopyFormDescriptorsFromProjectResult result) { return null; }
    CreateAnnotationPropertiesResult get(CreateAnnotationPropertiesResult result) { return null; }
    CreateClassesResult get(CreateClassesResult result) { return null; }
    CreateDataPropertiesResult get(CreateDataPropertiesResult result) { return null; }
    CreateNamedIndividualsResult get(CreateNamedIndividualsResult result) { return null; }
    CreateObjectPropertiesResult get(CreateObjectPropertiesResult result) { return null; }
    CreateEntityDiscussionThreadResult get(CreateEntityDiscussionThreadResult result) { return null; }
    CreateEntityFromFormDataResult get(CreateEntityFromFormDataResult result) { return null; }
    CreateNewProjectResult get(CreateNewProjectResult result) { return null; }
    GetChapSessionResult get(GetChapSessionResult result) { return null; }
    LoadProjectResult get(LoadProjectResult result) { return null; }
    LogOutUserResult get(LogOutUserResult result) { return null; }
    RebuildPermissionsResult get(RebuildPermissionsResult result) { return null; }
    CreateUserAccountResult get(CreateUserAccountResult result) { return null; }
    DeleteEntitiesResult get(DeleteEntitiesResult result) { return null; }
    DeleteEntityCommentResult get(DeleteEntityCommentResult result) { return null; }
    DeleteFormResult get(DeleteFormResult result) { return null; }
    EditAnnotationsResult get(EditAnnotationsResult result) { return null; }
    EditCommentResult get(EditCommentResult result) { return null; }
    ExistingOntologyMergeAddResult get(ExistingOntologyMergeAddResult result) { return null; }
    GetAllOntologiesResult get(GetAllOntologiesResult result) { return null; }
    GetApplicationSettingsResult get(GetApplicationSettingsResult result) { return null; }
    GetCommentedEntitiesResult get(GetCommentedEntitiesResult result) { return null; }
    GetAvailableProjectsResult get(GetAvailableProjectsResult result) { return null; }
    GetAvailableProjectsWithPermissionResult get(GetAvailableProjectsWithPermissionResult result) { return null; }
    GetCurrentUserInSessionResult get(GetCurrentUserInSessionResult result) { return null; }
    GetDeprecatedEntitiesResult get(GetDeprecatedEntitiesResult result) { return null; }
    GetEmailAddressResult get(GetEmailAddressResult result) { return null; }
    GetEntityCreationFormsResult get(GetEntityCreationFormsResult result) { return null; }
    GetEntityCrudKitsResult get(GetEntityCrudKitsResult result) { return null; }
    GetEntityDeprecationFormsResult get(GetEntityDeprecationFormsResult result) { return null; }
    GetEntityDiscussionThreadsResult get(GetEntityDiscussionThreadsResult result) { return null; }
    GetEntityFormDescriptorResult get(GetEntityFormDescriptorResult result) { return null; }
    GetEntityFormsResult get(GetEntityFormsResult result) { return null; }
    GetEntityGraphResult get(GetEntityGraphResult result) { return null; }
    GetEntityRenderingResult get(GetEntityRenderingResult result) { return null; }
    GetEntityTagsResult get(GetEntityTagsResult result) { return null; }
    GetHeadRevisionNumberResult get(GetHeadRevisionNumberResult result) { return null; }
    GetHierarchyChildrenResult get(GetHierarchyChildrenResult result) { return null; }
    GetHierarchyPathsToRootResult get(GetHierarchyPathsToRootResult result) { return null; }
    GetHierarchyRootsResult get(GetHierarchyRootsResult result) { return null; }
    GetHierarchySiblingsResult get(GetHierarchySiblingsResult result) { return null; }
    GetEntityHtmlRenderingResult get(GetEntityHtmlRenderingResult result) { return null; }
    GetIndividualsResult get(GetIndividualsResult result) { return null; }
    GetIndividualsPageContainingIndividualResult get(GetIndividualsPageContainingIndividualResult result) { return null; }
    GetManchesterSyntaxFrameResult get(GetManchesterSyntaxFrameResult result) { return null; }
    GetManchesterSyntaxFrameCompletionsResult get(GetManchesterSyntaxFrameCompletionsResult result) { return null; }
    GetMatchingEntitiesResult get(GetMatchingEntitiesResult result) { return null; }
    GetNamedIndividualFrameResult get(GetNamedIndividualFrameResult result) { return null; }
    GetObjectPropertyFrameResult get(GetObjectPropertyFrameResult result) { return null; }
    GetOboTermIdResult get(GetOboTermIdResult result) { return null; }
    GetOboNamespacesResult get(GetOboNamespacesResult result) { return null; }
    GetOboTermCrossProductResult get(GetOboTermCrossProductResult result) { return null; }
    GetOboTermDefinitionResult get(GetOboTermDefinitionResult result) { return null; }
    GetOboTermSynonymsResult get(GetOboTermSynonymsResult result) { return null; }
    GetOboTermRelationshipsResult get(GetOboTermRelationshipsResult result) { return null; }
    GetOboTermXRefsResult get(GetOboTermXRefsResult result) { return null; }
    GetOntologyAnnotationsResult get(GetOntologyAnnotationsResult result) { return null; }
    GetOntologyFramesResult get(GetOntologyFramesResult result) { return null; }
    GetPersonIdCompletionsResult get(GetPersonIdCompletionsResult result) { return null; }
    GetPerspectiveDetailsResult get(GetPerspectiveDetailsResult result) { return null; }
    GetPerspectivesResult get(GetPerspectivesResult result) { return null; }
    GetPossibleItemCompletionsResult get(GetPossibleItemCompletionsResult result) { return null; }
    GetProjectChangesResult get(GetProjectChangesResult result) { return null; }
    GetProjectDetailsResult get(GetProjectDetailsResult result) { return null; }
    GetProjectEventsResult get(GetProjectEventsResult result) { return null; }
    GetProjectFormDescriptorsResult get(GetProjectFormDescriptorsResult result) { return null; }
    GetProjectInfoResult get(GetProjectInfoResult result) { return null; }
    GetPerspectiveLayoutResult get(GetPerspectiveLayoutResult result) { return null; }
    GetProjectPermissionsResult get(GetProjectPermissionsResult result) { return null; }
    GetProjectPrefixDeclarationsResult get(GetProjectPrefixDeclarationsResult result) { return null; }
    GetProjectSettingsResult get(GetProjectSettingsResult result) { return null; }
    GetProjectSharingSettingsResult get(GetProjectSharingSettingsResult result) { return null; }
    GetProjectTagsResult get(GetProjectTagsResult result) { return null; }
    GetRevisionSummariesResult get(GetRevisionSummariesResult result) { return null; }
    GetRootOntologyIdResult get(GetRootOntologyIdResult result) { return null; }
    GetSearchSettingsResult get(GetSearchSettingsResult result) { return null; }
    GetUserIdCompletionsResult get(GetUserIdCompletionsResult result) { return null; }
    GetUserProjectEntityGraphCriteriaResult get(GetUserProjectEntityGraphCriteriaResult result) { return null; }
    GetUsageResult get(GetUsageResult result) { return null; }
    GetWatchesResult get(GetWatchesResult result) { return null; }
    GetWatchedEntityChangesResult get(GetWatchedEntityChangesResult result) { return null; }
    LookupEntitiesResult get(LookupEntitiesResult result) { return null; }
    MergeEntitiesResult get(MergeEntitiesResult result) { return null; }
    MergeUploadedProjectResult get(MergeUploadedProjectResult result) { return null; }
    MoveEntitiesToParentResult get(MoveEntitiesToParentResult result) { return null; }
    MoveHierarchyNodeResult get(MoveHierarchyNodeResult result) { return null; }
    MoveProjectsToTrashResult get(MoveProjectsToTrashResult result) { return null; }
    NewOntologyMergeAddResult get(NewOntologyMergeAddResult result) { return null; }
    PerformEntitySearchResult get(PerformEntitySearchResult result) { return null; }
    PerformLoginResult get(PerformLoginResult result) { return null; }
    RemoveProjectFromTrashResult get(RemoveProjectFromTrashResult result) { return null; }
    ResetPasswordResult get(ResetPasswordResult result) { return null; }
    ResetPerspectiveLayoutResult get(ResetPerspectiveLayoutResult result) { return null; }
    ResetPerspectivesResult get(ResetPerspectivesResult result) { return null; }
    SetPerspectivesResult get(SetPerspectivesResult result) { return null; }
    RevertRevisionResult get(RevertRevisionResult result) { return null; }
    SetAnnotationValueResult get(SetAnnotationValueResult result) { return null; }
    SetApplicationSettingsResult get(SetApplicationSettingsResult result) { return null; }
    SetDiscussionThreadStatusResult get(SetDiscussionThreadStatusResult result) { return null; }
    SetEmailAddressResult get(SetEmailAddressResult result) { return null; }
    SetEntityCrudKitSettingsResult get(SetEntityCrudKitSettingsResult result) { return null; }
    SetEntityFormDescriptorResult get(SetEntityFormDescriptorResult result) { return null; }
    SetEntityFormDataResult get(SetEntityFormDataResult result) { return null; }
    SetEntityGraphActiveFiltersResult get(SetEntityGraphActiveFiltersResult result) { return null; }
    SetEntityWatchesResult get(SetEntityWatchesResult result) { return null; }
    SetManchesterSyntaxFrameResult get(SetManchesterSyntaxFrameResult result) { return null; }
    SetOboTermCrossProductResult get(SetOboTermCrossProductResult result) { return null; }
    SetOboTermDefinitionResult get(SetOboTermDefinitionResult result) { return null; }
    SetOboTermIdResult get(SetOboTermIdResult result) { return null; }
    SetOboTermRelationshipsResult get(SetOboTermRelationshipsResult result) { return null; }
    SetOboTermSynonymsResult get(SetOboTermSynonymsResult result) { return null; }
    SetOboTermXRefsResult get(SetOboTermXRefsResult result) { return null; }
    SetOntologyAnnotationsResult get(SetOntologyAnnotationsResult result) { return null; }
    SetPerspectiveLayoutResult get(SetPerspectiveLayoutResult result) { return null; }
    SetProjectFormDescriptorsResult get(SetProjectFormDescriptorsResult result) { return null; }
    SetProjectPrefixDeclarationsResult get(SetProjectPrefixDeclarationsResult result) { return null; }
    SetProjectSettingsResult get(SetProjectSettingsResult result) { return null; }
    SetProjectSharingSettingsResult get(SetProjectSharingSettingsResult result) { return null; }
    SetProjectTagsResult get(SetProjectTagsResult result) { return null; }
    SetSearchSettingsResult get(SetSearchSettingsResult result) { return null; }
    SetUserProjectEntityGraphSettingsResult get(SetUserProjectEntityGraphSettingsResult result) { return null; }
    UpdateEntityTagsResult get(UpdateEntityTagsResult result) { return null; }
    UpdateFormDescriptorResult get(UpdateFormDescriptorResult result) { return null; }
}

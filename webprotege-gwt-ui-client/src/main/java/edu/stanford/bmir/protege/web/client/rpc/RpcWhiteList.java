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
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationDescriptor;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedValueDescriptor;
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

    public LangTag getLangTag() {
        return langTag;
    }

    public void setLangTag(LangTag langTag) {
        this.langTag = langTag;
    }

    LangTag langTag;

    public LangTagFilter getLangTagFilter() {
        return langTagFilter;
    }

    public void setLangTagFilter(LangTagFilter langTagFilter) {
        this.langTagFilter = langTagFilter;
    }

    public FormRegionFilter getFormRegionFilter() {
        return formRegionFilter;
    }

    public void setFormRegionFilter(FormRegionFilter formRegionFilter) {
        this.formRegionFilter = formRegionFilter;
    }

    FormRegionFilter formRegionFilter;

    FormPurpose formPurpose;

    LangTagFilter langTagFilter;

    MultiMatchType multiMatchType;

    HierarchyFilterType hierarchyFilterType;

    ProjectSettings projectSettings;

    PerspectiveDetails perspectiveDetails;

    SlackIntegrationSettings slackIntegrationSettings;

    WebhookSettings webhookSettings;

    WebhookSetting webhookSetting;

    ProjectWebhookEventType projectWebhookEventType;

    DictionaryLanguageData dictionaryLanguageData;

    DictionaryLanguage dictionaryLanguage;

    AnnotationAssertionDictionaryLanguage annotationAssertionDictionaryLanguage;

    AnnotationAssertionPathDictionaryLanguage annotationAssertionPathDictionaryLanguage;

    LocalNameDictionaryLanguage localNameDictionaryLanguage;

    OboIdDictionaryLanguage oboIdDictionaryLanguage;

    PrefixedNameDictionaryLanguage prefixedNameDictionaryLanguage;

    AvailableProject availableProject;

    ProjectDetails projectDetails;

    OWLPrimitiveData primitiveData;

    FilterState filterState;

    State state;

    ObjectPropertyCharacteristic objectPropertyCharacteristic;

    PropertyValueDescriptor propertyValueDescriptor;

    PropertyValue propertyValue;

    EntityDeprecationSettings entityDeprecationSettings;

    DisplayNameSettings displayNameSettings;

    DictionaryLanguageUsage dictionaryLanguageUsage;

    EntityNameMatchType entityNameMatchType;

    PrefixNameMatchType prefixNameMatchType;

    NodePropertyValueMap nodePropertyValueMap;

    NodePropertyValue nodePropertyValue;

    StringNodePropertyValue stringNodePropertyValue;

    EntityNameMatchResult entityNameMatchResult;

    InstanceRetrievalMode instanceRetrievalMode;

    ActionExecutionResult actionExecutionResult;

    PerspectiveId perspectiveId;

    PerspectiveLayout perspectiveLayout;

    PerspectiveDescriptor perspectiveDescriptor;

    FormFieldId formFieldId;

    OwlPropertyBinding owlPropertyBinding;

    OwlClassBinding owlClassBinding;

    OwlBinding owlBinding;

    FormControlData formControlData;

    GridColumnId columnId;

    GridControlDescriptor gridFieldDescriptor;

    GridColumnDescriptor gridColumnDescriptor;

    SingleChoiceControlType singleChoiceControlType;

    GridControlData gridControlData;

    GridRowData gridRowData;

    GridCellData gridCellData;

    IRI iri;

    OWLLiteral literal;

    OWLLiteralImplPlain literalImplPlain;

    EntityGraphFilter entityGraphFilter;

    FilterName filterName;

    ProjectUserEntityGraphSettings projectUserEntityGraphSettings;

    EntityGraphSettings entityGraphSettings;

    Operation operation;

    PersonId personId;

    Tag tag;

    EntityGraph entityGraph;

    IsAEdge isAEdge;

    RelationshipEdge relationshipEdge;

    ImmutableSetMultimap immutableSetMultimap;

    FieldRun fieldRun;

    Optionality optionality;

    Repeatability repeatability;

    TextControlDescriptor textFieldDescriptor;

    NumberControlDescriptor numberFieldDescriptor;

    EntityNameControlDescriptor entityNameFieldDescriptor;

    SubFormControlDescriptor subFormFieldDescriptor;

    SingleChoiceControlDescriptor choiceFieldDescriptor;

    ChoiceDescriptor choiceDescriptor;

    ImageControlDescriptor imageFieldDescriptor;

    FormFieldData formFieldData;

    LanguageMap languageMap;

    EdgeCriteria edgeCriteria;

    RelationshipPresence relationshipPresence;

    EntitySearchFilter entitySearchFilter;

    EntitySearchFilterId entitySearchFilterId;

    public PrimitiveFormControlDataDto primitiveFormControlDataDto;

    public FormControlDataDto formControlDataDto;

    public FormDataDto formDataDto;

    public FormFieldDataDto formFieldDataDto;

    public FormSubjectDto formSubjectDto;

    public FormSubject formSubject;

    public FormEntitySubjectDto formEntitySubjectDto;

    public GridControlDataDto gridControlDataDto;

    public GridRowDataDto gridRowDataDto;

    public GridCellDataDto gridCellDataDto;

    public ChoiceDescriptorDto choiceDescriptorDto;

    public FormRegionOrdering formRegionOrdering;

    public FormRegionOrdering gridControlOrdering;

    public GeneratedAnnotationsSettings generatedAnnotationsSettings;

    public GeneratedAnnotationDescriptor generatedAnnotationDescriptor;

    public GeneratedValueDescriptor generatedValueDescriptor;

    public FormRegionOrderingDirection formRegionOrderingDirection;

    public FormPageRequest.SourceType getSource() {
        return source;
    }

    public ProjectSearchSettings searchSettings;

    public void setSource(FormPageRequest.SourceType source) {
        this.source = source;
    }

    public ExpansionState expansionState;

    FormPageRequest.SourceType source;

    FormDescriptorDto formDescriptorDto;

    FormFieldDescriptorDto formFieldDescriptorDto;

    FormControlDescriptorDto formControlDescriptorDto;

    GridColumnDescriptorDto gridColumnDescriptorDto;

    OwlSubClassBinding subClassBinding;

    EntitySearchResult entitySearchResult;

    SearchResultMatch searchResultMatch;

    SearchResultMatchPosition searchResultMatchPosition;

    TerminalNodeId terminalNodeId;

    TerminalNode terminalNode;

    NodeProperties nodeProperties;

    FreshEntityIri freshEntityIri;

    FormFieldDeprecationStrategy deprecationStrategy;

    public PrimitiveFormControlDataDto getPrimitiveFormControlDataDto() {
        return primitiveFormControlDataDto;
    }

    public void setPrimitiveFormControlDataDto(PrimitiveFormControlDataDto primitiveFormControlDataDto) {
        this.primitiveFormControlDataDto = primitiveFormControlDataDto;
    }

    public FormControlDataDto getFormControlDataDto() {
        return formControlDataDto;
    }

    public void setFormControlDataDto(FormControlDataDto formControlDataDto) {
        this.formControlDataDto = formControlDataDto;
    }

    public FormDataDto getFormDataDto() {
        return formDataDto;
    }

    public void setFormDataDto(FormDataDto formDataDto) {
        this.formDataDto = formDataDto;
    }

    public FormFieldDataDto getFormFieldDataDto() {
        return formFieldDataDto;
    }

    public void setFormFieldDataDto(FormFieldDataDto formFieldDataDto) {
        this.formFieldDataDto = formFieldDataDto;
    }

    public FormSubjectDto getFormSubjectDto() {
        return formSubjectDto;
    }

    public void setFormSubjectDto(FormSubjectDto formSubjectDto) {
        this.formSubjectDto = formSubjectDto;
    }

    public FormEntitySubjectDto getFormEntitySubjectDto() {
        return formEntitySubjectDto;
    }

    public void setFormEntitySubjectDto(FormEntitySubjectDto formEntitySubjectDto) {
        this.formEntitySubjectDto = formEntitySubjectDto;
    }

    public GridControlDataDto getGridControlDataDto() {
        return gridControlDataDto;
    }

    public void setGridControlDataDto(GridControlDataDto gridControlDataDto) {
        this.gridControlDataDto = gridControlDataDto;
    }

    public GridRowDataDto getGridRowDataDto() {
        return gridRowDataDto;
    }

    public void setGridRowDataDto(GridRowDataDto gridRowDataDto) {
        this.gridRowDataDto = gridRowDataDto;
    }

    public GridCellDataDto getGridCellDataDto() {
        return gridCellDataDto;
    }

    public void setGridCellDataDto(GridCellDataDto gridCellDataDto) {
        this.gridCellDataDto = gridCellDataDto;
    }

    public ChoiceDescriptorDto getChoiceDescriptorDto() {
        return choiceDescriptorDto;
    }

    public void setChoiceDescriptorDto(ChoiceDescriptorDto choiceDescriptorDto) {
        this.choiceDescriptorDto = choiceDescriptorDto;
    }

    public FormRegionOrdering getFormRegionOrdering() {
        return formRegionOrdering;
    }

    public void setFormRegionOrdering(FormRegionOrdering formRegionOrdering) {
        this.formRegionOrdering = formRegionOrdering;
    }

    public FormRegionOrdering getGridControlOrdering() {
        return gridControlOrdering;
    }

    public void setGridControlOrdering(FormRegionOrdering gridControlOrdering) {
        this.gridControlOrdering = gridControlOrdering;
    }

    public FormRegionOrderingDirection getFormRegionOrderingDirection() {
        return formRegionOrderingDirection;
    }

    public void setFormRegionOrderingDirection(FormRegionOrderingDirection formRegionOrderingDirection) {
        this.formRegionOrderingDirection = formRegionOrderingDirection;
    }

    public ExpansionState getExpansionState() {
        return expansionState;
    }

    public void setExpansionState(ExpansionState expansionState) {
        this.expansionState = expansionState;
    }

    public FormDescriptorDto getFormDescriptorDto() {
        return formDescriptorDto;
    }

    public void setFormDescriptorDto(FormDescriptorDto formDescriptorDto) {
        this.formDescriptorDto = formDescriptorDto;
    }

    public FormFieldDescriptorDto getFormFieldDescriptorDto() {
        return formFieldDescriptorDto;
    }

    public void setFormFieldDescriptorDto(FormFieldDescriptorDto formFieldDescriptorDto) {
        this.formFieldDescriptorDto = formFieldDescriptorDto;
    }

    public FormControlDescriptorDto getFormControlDescriptorDto() {
        return formControlDescriptorDto;
    }

    public void setFormControlDescriptorDto(FormControlDescriptorDto formControlDescriptorDto) {
        this.formControlDescriptorDto = formControlDescriptorDto;
    }

    public GridColumnDescriptorDto getGridColumnDescriptorDto() {
        return gridColumnDescriptorDto;
    }

    public void setGridColumnDescriptorDto(GridColumnDescriptorDto gridColumnDescriptorDto) {
        this.gridColumnDescriptorDto = gridColumnDescriptorDto;
    }

    public OwlSubClassBinding getSubClassBinding() {
        return subClassBinding;
    }

    public void setSubClassBinding(OwlSubClassBinding subClassBinding) {
        this.subClassBinding = subClassBinding;
    }

    public FormPageRequest getFormPageRequest() {
        return formPageRequest;
    }

    public HierarchyPositionCriteria getHierarchyPositionCriteria() {
        return hierarchyPositionCriteria;
    }

    public void setFormPageRequest(FormPageRequest formPageRequest) {
        this.formPageRequest = formPageRequest;
    }

    public void setHierarchyPositionCriteria(HierarchyPositionCriteria hierarchyPositionCriteria) {
        this.hierarchyPositionCriteria = hierarchyPositionCriteria;
    }

    HierarchyPositionCriteria hierarchyPositionCriteria;

    public ConditionalIriPrefix getConditionalIriPrefix() {
        return conditionalIriPrefix;
    }

    public void setConditionalIriPrefix(ConditionalIriPrefix conditionalIriPrefix) {
        this.conditionalIriPrefix = conditionalIriPrefix;
    }

    ConditionalIriPrefix conditionalIriPrefix;

    public EntityCrudKitSettings getEntityCrudKitSettings() {
        return entityCrudKitSettings;
    }

    public void setEntityCrudKitSettings(EntityCrudKitSettings entityCrudKitSettings) {
        this.entityCrudKitSettings = entityCrudKitSettings;
    }

    EntityCrudKitSettings entityCrudKitSettings;

    public EntityCrudKitSuffixSettings getSuffixSettings() {
        return suffixSettings;
    }

    public void setSuffixSettings(EntityCrudKitSuffixSettings suffixSettings) {
        this.suffixSettings = suffixSettings;
    }

    public EntityCrudKitPrefixSettings getEntityCrudKitPrefixSettings() {
        return entityCrudKitPrefixSettings;
    }

    public void setEntityCrudKitPrefixSettings(EntityCrudKitPrefixSettings entityCrudKitPrefixSettings) {
        this.entityCrudKitPrefixSettings = entityCrudKitPrefixSettings;
    }

    EntityCrudKitPrefixSettings entityCrudKitPrefixSettings;

    EntityCrudKitSuffixSettings suffixSettings;

    ValidationStatus validationStatus;

    private Color color;

    private Criteria criteria;

    private WhiteSpaceTreatment whiteSpaceTreatment;

    private CompositeRelationshipValueCriteria compositeRelationshipValueCriteria;

    private FormPageRequest formPageRequest;

    public PlainPropertyValue getPlainPropertyValue() {
        return plainPropertyValue;
    }

    public void setPlainPropertyValue(PlainPropertyValue plainPropertyValue) {
        this.plainPropertyValue = plainPropertyValue;
    }

    PlainPropertyValue plainPropertyValue;

    public UuidFormat getUuidFormat() {
        return uuidFormat;
    }

    public void setUuidFormat(UuidFormat uuidFormat) {
        this.uuidFormat = uuidFormat;
    }

    private UuidFormat uuidFormat;

    public EntityFormSelector getEntityFormSelector() {
        return entityFormSelector;
    }

    public void setEntityFormSelector(EntityFormSelector entityFormSelector) {
        this.entityFormSelector = entityFormSelector;
    }

    public ChoiceListSourceDescriptor getChoiceListSourceDescriptor() {
        return choiceListSourceDescriptor;
    }

    public void setChoiceListSourceDescriptor(ChoiceListSourceDescriptor choiceListSourceDescriptor) {
        this.choiceListSourceDescriptor = choiceListSourceDescriptor;
    }

    EntityFormSelector entityFormSelector;

    ChoiceListSourceDescriptor choiceListSourceDescriptor;

    public FormEntitySubject getFormEntitySubject() {
        return formEntitySubject;
    }

    public void setFormEntitySubject(FormEntitySubject formEntitySubject) {
        this.formEntitySubject = formEntitySubject;
    }

    public void setPrimitiveFormControlData(PrimitiveFormControlData primitiveFormControlData) {
        this.primitiveFormControlData = primitiveFormControlData;
    }

    private FormEntitySubject formEntitySubject;

    public MultiChoiceControlDescriptor getMultiChoiceControlDescriptor() {
        return multiChoiceControlDescriptor;
    }

    public void setMultiChoiceControlDescriptor(MultiChoiceControlDescriptor multiChoiceControlDescriptor) {
        this.multiChoiceControlDescriptor = multiChoiceControlDescriptor;
    }

    MultiChoiceControlDescriptor multiChoiceControlDescriptor;

    public RpcWhiteList() {
    }

    public ActionExecutionResult getActionExecutionResult() {
        return actionExecutionResult;
    }

    public void setActionExecutionResult(ActionExecutionResult actionExecutionResult) {
        this.actionExecutionResult = actionExecutionResult;
    }

    public AvailableProject getAvailableProject() {
        return availableProject;
    }

    public void setAvailableProject(AvailableProject availableProject) {
        this.availableProject = availableProject;
    }

    public ChoiceDescriptor getChoiceDescriptor() {
        return choiceDescriptor;
    }

    public void setChoiceDescriptor(ChoiceDescriptor choiceDescriptor) {
        this.choiceDescriptor = choiceDescriptor;
    }

    public SingleChoiceControlDescriptor getChoiceFieldDescriptor() {
        return choiceFieldDescriptor;
    }

    public void setChoiceFieldDescriptor(SingleChoiceControlDescriptor choiceFieldDescriptor) {
        this.choiceFieldDescriptor = choiceFieldDescriptor;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public GridColumnId getColumnId() {
        return columnId;
    }

    public void setColumnId(GridColumnId columnId) {
        this.columnId = columnId;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public DictionaryLanguage getDictionaryLanguage() {
        return dictionaryLanguage;
    }

    public void setDictionaryLanguage(DictionaryLanguage dictionaryLanguage) {
        this.dictionaryLanguage = dictionaryLanguage;
    }

    public DictionaryLanguageData getDictionaryLanguageData() {
        return dictionaryLanguageData;
    }

    public void setDictionaryLanguageData(DictionaryLanguageData dictionaryLanguageData) {
        this.dictionaryLanguageData = dictionaryLanguageData;
    }

    public DictionaryLanguageUsage getDictionaryLanguageUsage() {
        return dictionaryLanguageUsage;
    }

    public void setDictionaryLanguageUsage(DictionaryLanguageUsage dictionaryLanguageUsage) {
        this.dictionaryLanguageUsage = dictionaryLanguageUsage;
    }

    public DisplayNameSettings getDisplayNameSettings() {
        return displayNameSettings;
    }

    public void setDisplayNameSettings(DisplayNameSettings displayNameSettings) {
        this.displayNameSettings = displayNameSettings;
    }

    public EdgeCriteria getEdgeCriteria() {
        return edgeCriteria;
    }

    public void setEdgeCriteria(EdgeCriteria edgeCriteria) {
        this.edgeCriteria = edgeCriteria;
    }

    public EntityGraph getEntityGraph() {
        return entityGraph;
    }

    public void setEntityGraph(EntityGraph entityGraph) {
        this.entityGraph = entityGraph;
    }

    public EntityGraphFilter getEntityGraphFilter() {
        return entityGraphFilter;
    }

    public void setEntityGraphFilter(EntityGraphFilter entityGraphFilter) {
        this.entityGraphFilter = entityGraphFilter;
    }

    public EntityGraphSettings getEntityGraphSettings() {
        return entityGraphSettings;
    }

    public void setEntityGraphSettings(EntityGraphSettings entityGraphSettings) {
        this.entityGraphSettings = entityGraphSettings;
    }

    public EntityNameControlDescriptor getEntityNameFieldDescriptor() {
        return entityNameFieldDescriptor;
    }

    public void setEntityNameFieldDescriptor(EntityNameControlDescriptor entityNameFieldDescriptor) {
        this.entityNameFieldDescriptor = entityNameFieldDescriptor;
    }

    public EntityNameMatchResult getEntityNameMatchResult() {
        return entityNameMatchResult;
    }

    public void setEntityNameMatchResult(EntityNameMatchResult entityNameMatchResult) {
        this.entityNameMatchResult = entityNameMatchResult;
    }

    public EntityNameMatchType getEntityNameMatchType() {
        return entityNameMatchType;
    }

    public void setEntityNameMatchType(EntityNameMatchType entityNameMatchType) {
        this.entityNameMatchType = entityNameMatchType;
    }

    public FieldRun getFieldRun() {
        return fieldRun;
    }

    public void setFieldRun(FieldRun fieldRun) {
        this.fieldRun = fieldRun;
    }

    public FilterName getFilterName() {
        return filterName;
    }

    public void setFilterName(FilterName filterName) {
        this.filterName = filterName;
    }

    public FormControlData getFormControlData() {
        return formControlData;
    }

    public void setFormControlData(FormControlData formControlData) {
        this.formControlData = formControlData;
    }

    public FormFieldData getFormFieldData() {
        return formFieldData;
    }

    public void setFormFieldData(FormFieldData formFieldData) {
        this.formFieldData = formFieldData;
    }

    public FormFieldId getFormFieldId() {
        return formFieldId;
    }

    public void setFormFieldId(FormFieldId formFieldId) {
        this.formFieldId = formFieldId;
    }

    public GridCellData getGridCellData() {
        return gridCellData;
    }

    public void setGridCellData(GridCellData gridCellData) {
        this.gridCellData = gridCellData;
    }

    public GridColumnDescriptor getGridColumnDescriptor() {
        return gridColumnDescriptor;
    }

    public void setGridColumnDescriptor(GridColumnDescriptor gridColumnDescriptor) {
        this.gridColumnDescriptor = gridColumnDescriptor;
    }

    public GridControlData getGridControlData() {
        return gridControlData;
    }

    public void setGridControlData(GridControlData gridControlData) {
        this.gridControlData = gridControlData;
    }

    public GridControlDescriptor getGridFieldDescriptor() {
        return gridFieldDescriptor;
    }

    public void setGridFieldDescriptor(GridControlDescriptor gridFieldDescriptor) {
        this.gridFieldDescriptor = gridFieldDescriptor;
    }

    public GridRowData getGridRowData() {
        return gridRowData;
    }

    public void setGridRowData(GridRowData gridRowData) {
        this.gridRowData = gridRowData;
    }

    public HierarchyFilterType getHierarchyFilterType() {
        return hierarchyFilterType;
    }

    public void setHierarchyFilterType(HierarchyFilterType hierarchyFilterType) {
        this.hierarchyFilterType = hierarchyFilterType;
    }

    public ImageControlDescriptor getImageFieldDescriptor() {
        return imageFieldDescriptor;
    }

    public void setImageFieldDescriptor(ImageControlDescriptor imageFieldDescriptor) {
        this.imageFieldDescriptor = imageFieldDescriptor;
    }

    public ImmutableSetMultimap getImmutableSetMultimap() {
        return immutableSetMultimap;
    }

    public void setImmutableSetMultimap(ImmutableSetMultimap immutableSetMultimap) {
        this.immutableSetMultimap = immutableSetMultimap;
    }

    public InstanceRetrievalMode getInstanceRetrievalMode() {
        return instanceRetrievalMode;
    }

    public void setInstanceRetrievalMode(InstanceRetrievalMode instanceRetrievalMode) {
        this.instanceRetrievalMode = instanceRetrievalMode;
    }

    public IRI getIri() {
        return iri;
    }

    public void setIri(IRI iri) {
        this.iri = iri;
    }

    public IsAEdge getIsAEdge() {
        return isAEdge;
    }

    public void setIsAEdge(IsAEdge isAEdge) {
        this.isAEdge = isAEdge;
    }

    public LanguageMap getLanguageMap() {
        return languageMap;
    }

    public void setLanguageMap(LanguageMap languageMap) {
        this.languageMap = languageMap;
    }

    public OWLLiteral getLiteral() {
        return literal;
    }

    public void setLiteral(OWLLiteral literal) {
        this.literal = literal;
    }

    public OWLLiteralImplPlain getLiteralImplPlain() {
        return literalImplPlain;
    }

    public void setLiteralImplPlain(OWLLiteralImplPlain literalImplPlain) {
        this.literalImplPlain = literalImplPlain;
    }

    public MultiMatchType getMultiMatchType() {
        return multiMatchType;
    }

    public void setMultiMatchType(MultiMatchType multiMatchType) {
        this.multiMatchType = multiMatchType;
    }

    public NumberControlDescriptor getNumberFieldDescriptor() {
        return numberFieldDescriptor;
    }

    public void setNumberFieldDescriptor(NumberControlDescriptor numberFieldDescriptor) {
        this.numberFieldDescriptor = numberFieldDescriptor;
    }

    public ObjectPropertyCharacteristic getObjectPropertyCharacteristic() {
        return objectPropertyCharacteristic;
    }

    public void setObjectPropertyCharacteristic(ObjectPropertyCharacteristic objectPropertyCharacteristic) {
        this.objectPropertyCharacteristic = objectPropertyCharacteristic;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Optionality getOptionality() {
        return optionality;
    }

    public void setOptionality(Optionality optionality) {
        this.optionality = optionality;
    }

    public OwlBinding getOwlBinding() {
        return owlBinding;
    }

    public void setOwlBinding(OwlBinding owlBinding) {
        this.owlBinding = owlBinding;
    }

    public OwlClassBinding getOwlClassBinding() {
        return owlClassBinding;
    }

    public void setOwlClassBinding(OwlClassBinding owlClassBinding) {
        this.owlClassBinding = owlClassBinding;
    }

    public OwlPropertyBinding getOwlPropertyBinding() {
        return owlPropertyBinding;
    }

    public void setOwlPropertyBinding(OwlPropertyBinding owlPropertyBinding) {
        this.owlPropertyBinding = owlPropertyBinding;
    }

    public PrefixNameMatchType getPrefixNameMatchType() {
        return prefixNameMatchType;
    }

    public void setPrefixNameMatchType(PrefixNameMatchType prefixNameMatchType) {
        this.prefixNameMatchType = prefixNameMatchType;
    }

    public OWLPrimitiveData getPrimitiveData() {
        return primitiveData;
    }

    public void setPrimitiveData(OWLPrimitiveData primitiveData) {
        this.primitiveData = primitiveData;
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(ProjectDetails projectDetails) {
        this.projectDetails = projectDetails;
    }

    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }

    public void setProjectSettings(ProjectSettings projectSettings) {
        this.projectSettings = projectSettings;
    }

    public ProjectUserEntityGraphSettings getProjectUserEntityGraphSettings() {
        return projectUserEntityGraphSettings;
    }

    public void setProjectUserEntityGraphSettings(ProjectUserEntityGraphSettings projectUserEntityGraphSettings) {
        this.projectUserEntityGraphSettings = projectUserEntityGraphSettings;
    }

    public ProjectWebhookEventType getProjectWebhookEventType() {
        return projectWebhookEventType;
    }

    public void setProjectWebhookEventType(ProjectWebhookEventType projectWebhookEventType) {
        this.projectWebhookEventType = projectWebhookEventType;
    }

    public PropertyValue getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(PropertyValue propertyValue) {
        this.propertyValue = propertyValue;
    }

    public PropertyValueDescriptor getPropertyValueDescriptor() {
        return propertyValueDescriptor;
    }

    public void setPropertyValueDescriptor(PropertyValueDescriptor propertyValueDescriptor) {
        this.propertyValueDescriptor = propertyValueDescriptor;
    }

    public RelationshipEdge getRelationshipEdge() {
        return relationshipEdge;
    }

    public void setRelationshipEdge(RelationshipEdge relationshipEdge) {
        this.relationshipEdge = relationshipEdge;
    }

    public RelationshipPresence getRelationshipPresence() {
        return relationshipPresence;
    }

    public void setRelationshipPresence(RelationshipPresence relationshipPresence) {
        this.relationshipPresence = relationshipPresence;
    }

    public PrimitiveFormControlData getPrimitiveFormControlData() {
        return primitiveFormControlData;
    }

    private PrimitiveFormControlData primitiveFormControlData;

    public Repeatability getRepeatability() {
        return repeatability;
    }

    public void setRepeatability(Repeatability repeatability) {
        this.repeatability = repeatability;
    }

    public SingleChoiceControlType getSingleChoiceControlType() {
        return singleChoiceControlType;
    }

    public void setSingleChoiceControlType(SingleChoiceControlType singleChoiceControlType) {
        this.singleChoiceControlType = singleChoiceControlType;
    }

    public SlackIntegrationSettings getSlackIntegrationSettings() {
        return slackIntegrationSettings;
    }

    public void setSlackIntegrationSettings(SlackIntegrationSettings slackIntegrationSettings) {
        this.slackIntegrationSettings = slackIntegrationSettings;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public SubFormControlDescriptor getSubFormFieldDescriptor() {
        return subFormFieldDescriptor;
    }

    public void setSubFormFieldDescriptor(SubFormControlDescriptor subFormFieldDescriptor) {
        this.subFormFieldDescriptor = subFormFieldDescriptor;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public TextControlDescriptor getTextFieldDescriptor() {
        return textFieldDescriptor;
    }

    public void setTextFieldDescriptor(TextControlDescriptor textFieldDescriptor) {
        this.textFieldDescriptor = textFieldDescriptor;
    }

    public WebhookSetting getWebhookSetting() {
        return webhookSetting;
    }

    public void setWebhookSetting(WebhookSetting webhookSetting) {
        this.webhookSetting = webhookSetting;
    }

    public WebhookSettings getWebhookSettings() {
        return webhookSettings;
    }

    public void setWebhookSettings(WebhookSettings webhookSettings) {
        this.webhookSettings = webhookSettings;
    }


    public CompositeRelationshipValueCriteria getCompositeRelationshipValueCriteria() {
        return compositeRelationshipValueCriteria;
    }

    public void setCompositeRelationshipValueCriteria(CompositeRelationshipValueCriteria compositeRelationshipValueCriteria) {
        this.compositeRelationshipValueCriteria = compositeRelationshipValueCriteria;
    }

    public WhiteSpaceTreatment getWhiteSpaceTreatment() {
        return whiteSpaceTreatment;
    }

    public void setWhiteSpaceTreatment(WhiteSpaceTreatment whiteSpaceTreatment) {
        this.whiteSpaceTreatment = whiteSpaceTreatment;
    }

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
    UpdateObjectPropertyFrameAction get(UpdateObjectPropertyFrameAction action) { return null;}

    AddEntityCommentResult get(AddEntityCommentResult result) { return null; }
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

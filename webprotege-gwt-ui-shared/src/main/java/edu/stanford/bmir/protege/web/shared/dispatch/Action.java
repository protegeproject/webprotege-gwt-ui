package edu.stanford.bmir.protege.web.shared.dispatch;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.app.GetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.shared.auth.AuthenticateUserAction;
import edu.stanford.bmir.protege.web.shared.auth.ChangePasswordAction;
import edu.stanford.bmir.protege.web.shared.auth.GetChapSessionAction;
import edu.stanford.bmir.protege.web.shared.bulkop.EditAnnotationsAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.*;
import edu.stanford.bmir.protege.web.shared.entity.DeleteEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.GetDeprecatedEntitiesAction;
import edu.stanford.bmir.protege.web.shared.form.CopyFormDescriptorsFromProjectAction;
import edu.stanford.bmir.protege.web.shared.form.DeleteFormAction;
import edu.stanford.bmir.protege.web.shared.frame.CheckManchesterSyntaxFrameAction;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeAction;
import edu.stanford.bmir.protege.web.shared.merge_add.ExistingOntologyMergeAddAction;
import edu.stanford.bmir.protege.web.shared.merge_add.GetAllOntologiesAction;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsResult;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsWithPermissionAction;
import edu.stanford.bmir.protege.web.shared.project.LoadProjectAction;
import edu.stanford.bmir.protege.web.shared.tag.AddProjectTagAction;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountAction;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountResult;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserAction;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 * <p>
 *     The basic interface for actions that are sent to the dispatch service
 * </p>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "action")
@JsonSubTypes(value = {
        @Type(value = AddEntityCommentAction.class),
        @Type(value = AddProjectTagAction.class),
        @Type(value = AuthenticateUserAction.class),
        @Type(value = BatchAction.class),
        @Type(value = ChangePasswordAction.class),
        @Type(value = CheckManchesterSyntaxFrameAction.class),
        @Type(value = ComputeProjectMergeAction.class),
        @Type(value = CopyFormDescriptorsFromProjectAction.class),
        @Type(value = CreateAnnotationPropertiesAction.class),
        @Type(value = CreateClassesAction.class),
        @Type(value = CreateDataPropertiesAction.class),
        @Type(value = CreateNamedIndividualsAction.class),
        @Type(value = CreateObjectPropertiesAction.class),
        @Type(value = CreateEntityDiscussionThreadAction.class),
        @Type(value = CreateEntityFromFormDataAction.class),
        @Type(value = CreateNewProjectAction.class),
        @Type(value = GetChapSessionAction.class),
        @Type(value = LoadProjectAction.class),
        @Type(value = LogOutUserAction.class),
        @Type(value = RebuildPermissionsAction.class),
        @Type(value = CreateUserAccountAction.class),
        @Type(value = DeleteEntitiesAction.class),
        @Type(value = DeleteEntityCommentAction.class),
        @Type(value = DeleteFormAction.class),
        @Type(value = EditAnnotationsAction.class),
        @Type(value = EditCommentAction.class),
        @Type(value = ExistingOntologyMergeAddAction.class),
        @Type(value = GetAllOntologiesAction.class),
        @Type(value = GetApplicationSettingsAction.class),
        @Type(value = GetAvailableProjectsAction.class),
        @Type(value = GetCommentedEntitiesAction.class),
        @Type(value = GetAvailableProjectsWithPermissionAction.class),
        @Type(value = GetCurrentUserInSessionAction.class),
        @Type(value = GetDeprecatedEntitiesAction.class)

})
public interface Action<R extends Result> extends IsSerializable {
}

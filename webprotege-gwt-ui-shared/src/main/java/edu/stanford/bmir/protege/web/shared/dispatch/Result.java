package edu.stanford.bmir.protege.web.shared.dispatch;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.app.GetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.bulkop.EditAnnotationsResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.*;
import edu.stanford.bmir.protege.web.shared.entity.DeleteEntitiesResult;
import edu.stanford.bmir.protege.web.shared.form.CopyFormDescriptorsFromProjectResult;
import edu.stanford.bmir.protege.web.shared.form.DeleteFormResult;
import edu.stanford.bmir.protege.web.shared.frame.CheckManchesterSyntaxFrameResult;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.shared.merge_add.ExistingOntologyMergeAddAction;
import edu.stanford.bmir.protege.web.shared.merge_add.ExistingOntologyMergeAddResult;
import edu.stanford.bmir.protege.web.shared.merge_add.GetAllOntologiesAction;
import edu.stanford.bmir.protege.web.shared.merge_add.GetAllOntologiesResult;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsResult;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectResult;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsResult;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsWithPermissionResult;
import edu.stanford.bmir.protege.web.shared.project.LoadProjectResult;
import edu.stanford.bmir.protege.web.shared.tag.AddProjectTagResult;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountResult;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserAction;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserResult;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 * <p>
 *     The basic interface for results which are returned from the dispatch service
 * </p>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "action")
@JsonSubTypes({
        @Type(AddEntityCommentResult.class),
        @Type(AddProjectTagResult.class),
        @Type(AuthenticateUserResult.class),
        @Type(BatchResult.class),
        @Type(ChangePasswordResult.class),
        @Type(CheckManchesterSyntaxFrameResult.class),
        @Type(ComputeProjectMergeResult.class),
        @Type(CopyFormDescriptorsFromProjectResult.class),
        @Type(CreateAnnotationPropertiesResult.class),
        @Type(CreateClassesResult.class),
        @Type(CreateDataPropertiesResult.class),
        @Type(CreateNamedIndividualsResult.class),
        @Type(CreateObjectPropertiesResult.class),
        @Type(CreateEntityDiscussionThreadResult.class),
        @Type(CreateEntityFromFormDataResult.class),
        @Type(CreateNewProjectResult.class),
        @Type(GetChapSessionResult.class),
        @Type(LoadProjectResult.class),
        @Type(LogOutUserResult.class),
        @Type(RebuildPermissionsResult.class),
        @Type(CreateUserAccountResult.class),
        @Type(DeleteEntitiesResult.class),
        @Type(DeleteEntityCommentResult.class),
        @Type(DeleteFormResult.class),
        @Type(EditAnnotationsResult.class),
        @Type(EditCommentResult.class),
        @Type(ExistingOntologyMergeAddResult.class),
        @Type(GetAllOntologiesResult.class),
        @Type(GetApplicationSettingsResult.class),
        @Type(GetCommentedEntitiesResult.class),
        @Type(GetAvailableProjectsWithPermissionResult.class),
        @Type(GetCurrentUserInSessionResult.class)
})
public interface Result extends IsSerializable {

}

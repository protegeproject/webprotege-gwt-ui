package edu.stanford.bmir.protege.web.shared.dispatch;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.auth.AuthenticateUserAction;
import edu.stanford.bmir.protege.web.shared.auth.AuthenticateUserResult;
import edu.stanford.bmir.protege.web.shared.auth.ChangePasswordResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.*;
import edu.stanford.bmir.protege.web.shared.form.CopyFormDescriptorsFromProjectResult;
import edu.stanford.bmir.protege.web.shared.frame.CheckManchesterSyntaxFrameResult;
import edu.stanford.bmir.protege.web.shared.issues.AddEntityCommentResult;
import edu.stanford.bmir.protege.web.shared.issues.CreateEntityDiscussionThreadAction;
import edu.stanford.bmir.protege.web.shared.issues.CreateEntityDiscussionThreadResult;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.shared.project.CreateNewProjectResult;
import edu.stanford.bmir.protege.web.shared.tag.AddProjectTagResult;


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
        @Type(CreateNewProjectResult.class)
})
public interface Result extends IsSerializable {

}

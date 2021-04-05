package edu.stanford.bmir.protege.web.shared.dispatch;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.auth.AuthenticateUserAction;
import edu.stanford.bmir.protege.web.shared.auth.AuthenticateUserResult;
import edu.stanford.bmir.protege.web.shared.auth.ChangePasswordResult;
import edu.stanford.bmir.protege.web.shared.issues.AddEntityCommentResult;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "result")
@JsonSubTypes({
        @Type(AddEntityCommentResult.class),
        @Type(AddProjectTagResult.class),
        @Type(AuthenticateUserResult.class),
        @Type(BatchResult.class),
        @Type(ChangePasswordResult.class)
})
public interface Result extends IsSerializable {

}

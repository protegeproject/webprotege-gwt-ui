package edu.stanford.bmir.protege.web.shared.dispatch;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.issues.AddEntityCommentAction;
import edu.stanford.bmir.protege.web.shared.tag.AddProjectTagAction;


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
        @Type(value = AddProjectTagAction.class)
})
public interface Action<R extends Result> extends IsSerializable {
}

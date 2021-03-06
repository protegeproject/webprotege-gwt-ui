package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
@RemoteServiceRelativePath("dispatchservice")
public interface DispatchService extends RemoteService {

    DispatchServiceResultContainer executeAction(Action action) throws ActionExecutionException, PermissionDeniedException;

    RpcWhiteList getRpcWhiteList(RpcWhiteList list);
}

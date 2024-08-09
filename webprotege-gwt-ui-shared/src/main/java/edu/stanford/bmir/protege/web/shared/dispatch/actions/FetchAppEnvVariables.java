package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import java.io.Serializable;

@JsonTypeName("webprotege.app.GetAppEnvVariables")
public class FetchAppEnvVariables implements Action<AppEnvVariables>, Serializable, IsSerializable {
}

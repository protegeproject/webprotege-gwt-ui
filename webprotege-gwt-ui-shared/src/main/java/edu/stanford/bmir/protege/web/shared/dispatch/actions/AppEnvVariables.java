package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonTypeName("webprotege.app.GetAppEnvVariables")
public class AppEnvVariables implements Result, Serializable, IsSerializable {

    private String websocketUrl;

    private String logoutUrl;

    private String redirectAfterLogoutUrl;

    private String fileUploadUrl;


    @GwtSerializationConstructor
    public AppEnvVariables(){

    }


    @JsonCreator
    @NotNull
    public static AppEnvVariables create(@JsonProperty("logoutUrl") String logoutUrl, @JsonProperty("websocketUrl") String websocketUrl,
                                         @JsonProperty("redirectUrl") String redirectUrl,
                                         @JsonProperty("fileUploadUrl") String fileUploadUrl) {
        AppEnvVariables response = new AppEnvVariables();
        response.redirectAfterLogoutUrl = redirectUrl;
        response.logoutUrl = logoutUrl;
        response.websocketUrl = websocketUrl;
        response.fileUploadUrl = fileUploadUrl;
        return response;
    }

    public String getWebsocketUrl() {
        return websocketUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public String getRedirectAfterLogoutUrl() {
        return redirectAfterLogoutUrl;
    }

    public String getFileUploadUrl() {
        return fileUploadUrl;
    }
}

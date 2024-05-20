package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@JsonTypeName("webprotege.users.GetUserInfo")
public class GetUserInfoResult implements Result, Serializable, IsSerializable {


    private String token;

    private String websocketUrl;


    @GwtSerializationConstructor
    public GetUserInfoResult(){

    }


    @JsonCreator
    @NotNull
    public static GetUserInfoResult create(@JsonProperty("token") String token, @JsonProperty("websocketUrl") String websocketUrl) {
        GetUserInfoResult response = new GetUserInfoResult();
        response.token = token;
        response.websocketUrl = websocketUrl;
        return response;
    }


    public String getToken() {
        return token;
    }


    public String getWebsocketUrl() {
        return websocketUrl;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

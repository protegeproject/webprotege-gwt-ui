package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;

import java.util.logging.Logger;

public final class RedirectAwareRpcRequestBuilder extends RpcRequestBuilder {

    private static final Logger logger = Logger.getLogger(RedirectAwareRpcRequestBuilder.class.getName());

    @Override
    protected void doSetCallback(RequestBuilder rb, final RequestCallback callback) {
        rb.setCallback(new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                String redirect = response.getHeader("X-Redirect-To" );
                logger.info("Received response: " + redirect);
                if(redirect != null && !redirect.isEmpty()) {
                    com.google.gwt.user.client.Window.Location.assign(redirect);
                    return;
                }
                callback.onResponseReceived(request, response);
            }

            @Override
            public void onError(Request req, Throwable ex) {
                callback.onError(req, ex);
            }
        });
    }
}

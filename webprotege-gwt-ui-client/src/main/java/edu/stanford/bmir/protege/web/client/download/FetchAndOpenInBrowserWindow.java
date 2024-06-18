package edu.stanford.bmir.protege.web.client.download;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-14
 */
public class FetchAndOpenInBrowserWindow {

    /**
     * GET the specified path and open the result as a blob in a new window
     * @param path The path
     * @param token The access token to use.  This will be set in the authorization header.
     */
    public static native void fetchUrlAndOpenInWindow(@Nonnull String path,
                                                      @Nonnull String token,
                                                      @Nonnull FetchAndOpenInBrowserWindowErrorHandler errorHandler)/*-{
        var xhr = new XMLHttpRequest();

        xhr.open('GET', path);
        xhr.onreadystatechange = handler;
        xhr.responseType = 'blob';
        xhr.setRequestHeader('Authorization', 'Bearer ' + token);
        xhr.send();

        function handler() {
            if (this.readyState === this.DONE) {
                if (this.status === 200) {
                    // this.response is a Blob, because we set responseType above
                    var data_url = URL.createObjectURL(this.response);
                    $wnd.open(data_url, '_blank');
                } else {
                    console.error('Error');
                    errorHandler.@edu.stanford.bmir.protege.web.client.download.FetchAndOpenInBrowserWindowErrorHandler::handleError(*)();
                }
            }
        }
    }-*/;
}
